package org.openintents.wifiqr.android.activity;

import java.util.ArrayList;
import java.util.List;
import org.openintents.wifiqr.WifiQR;
import org.openintents.wifiqr.util.WifiConfigHelper;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ChooseNetworkConfigActivity extends ListActivity {

    public void onCreate(Bundle savedstate) {
        super.onCreate(savedstate);
        WifiManager wifimanager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        List<WifiConfiguration> wifiList = wifimanager.getConfiguredNetworks();
        setListAdapter(new ArrayAdapter<WifiConfiguration>(this, android.R.layout.simple_list_item_1, android.R.id.text1, wifiList));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        WifiConfiguration wifiConf = (WifiConfiguration) getListAdapter().getItem(position);
        Intent intent = new Intent();
        intent.putExtra(WifiQR.EXTRA_WIFI_CONFIG_STRING, WifiConfigHelper.writeToString(wifiConf));
        setResult(RESULT_OK, intent);
        finish();
    }
}
