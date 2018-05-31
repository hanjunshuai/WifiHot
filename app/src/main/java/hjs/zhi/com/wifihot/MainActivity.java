package hjs.zhi.com.wifihot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import hjs.zhi.com.wifihot.util.WifiHotUtil;

public class MainActivity extends AppCompatActivity {
    private static String TAG = "StartActivity";

    private final String defaultHotName = "hjs";
    private final String defaultHotPwd = "0175@..h";

    private boolean isWifiOpen = false;
    private WifiHotUtil wifiHotUtil;
    private Button btnWifiHot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //start WiFiAPService
        WiFiAPService.startService(this);
        //init WifiHotUtil
        wifiHotUtil = new WifiHotUtil(this);

        btnWifiHot = findViewById(R.id.btnOpenWifiHot);
        btnWifiHot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "TAG");
                if (!isWifiOpen) {
                    openWifiHot();
                } else {
                    closeWifiHot();
                }
            }
        });

        WiFiAPService.addWiFiAPListener(new WiFiAPListener() {

            @Override
            public void stateChanged(int state) {
                Log.i(TAG, "state= " + state);
                switch (state) {
                    case WiFiAPListener.WIFI_AP_OPENING:
                        btnWifiHot.setText(getResources().getString(R.string.opening_wifi_hot));
                        break;
                    case WiFiAPListener.WIFI_AP_OPEN_SUCCESS:
                        isWifiOpen = true;
                        Toast.makeText(MainActivity.this, "WiFi热点" + defaultHotName + "已开启！",
                                Toast.LENGTH_LONG).show();
                        btnWifiHot.setText(getResources().getString(R.string.close_wifi_hot));
                        break;
                    case WiFiAPListener.WIFI_AP_CLOSEING:
                        btnWifiHot.setText(getResources().getString(R.string.closeing_wifi_hot));
                        break;
                    case WiFiAPListener.WIFI_AP_CLOSE_SUCCESS:
                        isWifiOpen = false;
                        Toast.makeText(MainActivity.this, "WiFi热点" + defaultHotName + "已关闭！",
                                Toast.LENGTH_LONG).show();
                        btnWifiHot.setText(getResources().getString(R.string.open_wifi_hot));
                        break;
                    default:
                        break;
                }
            }
        });

    }


    /**
     * open the wifi hot with the setted hotName and password
     */
    private void openWifiHot() {
        wifiHotUtil.startWifiAp(defaultHotName, defaultHotPwd);
    }

    /**
     * close wifi hot
     */
    private void closeWifiHot() {
        wifiHotUtil.closeWifiAp();
    }
}
