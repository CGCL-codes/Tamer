package com.Gpslocation.www;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Theodoros Kaloumenos and Konstantinos Valais
 * 
 */
public class MyLocation extends Activity {

    public static final int GO_TO_MAP = Menu.FIRST;

    public static final int STOP_GENIE = Menu.FIRST + 1;

    String generatorName = "AndroidTestGenerator";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        String aText = "Thank you for using our application." + "\nMake sure you turned on GPS.\n" + "\nYOUR LOCATION WILL BE SEND TO GENIE HUB.\n" + "\nYOUR PERMISSION IS REQUIRED TO CONTINUE.\n";
        TextView myLocationText;
        myLocationText = (TextView) findViewById(R.id.welcomeText);
        myLocationText.setText(aText);
        final Button createGeneratorButton = (Button) findViewById(R.id.createGen);
        final Button buttonStart = (Button) findViewById(R.id.startEP);
    }

    public void createGenerator(View view) throws Exception {
        List<NameValuePair> frm = new ArrayList<NameValuePair>();
        JSONArray ar = new JSONArray();
        ar.put("mobileOS_id");
        ar.put("type");
        ar.put("terminal.btmac");
        ar.put("currentLatitude");
        ar.put("currentLongitude");
        ar.put("accuracy");
        ar.put("timestamp");
        ar.put("ptime");
        frm.add(new BasicNameValuePair("specs", ar.toString()));
        frm.add(new BasicNameValuePair("url", "http://android-test-gps.appspot.com"));
        HttpClient client = new DefaultHttpClient();
        try {
            HttpPut put = new HttpPut("http://tiger.itu.dk:8004/informationbus/register/generator/" + generatorName);
            put.setEntity(new UrlEncodedFormEntity(frm));
            client.execute(put);
        } catch (IOException e) {
            Log.e("Generator", e.toString());
            Context context = getApplicationContext();
            Toast.makeText(context, "A network connection error. Generator has not created", 10).show();
            client.getConnectionManager().shutdown();
            throw new RuntimeException("Cannot create generator", e);
        }
    }

    public void startPublishing(View view) {
        Intent startEventPubService = new Intent("com.Gpslocation.www.EventPublishingService.SERVICE");
        startService(startEventPubService);
        Intent intent = new Intent(this, ShowMapActivity.class);
        startActivity(intent);
    }

    public void stopPublishing(View view) {
        Intent stopEventPubService = new Intent("com.Gpslocation.www.EventPublishingService.SERVICE");
        stopService(stopEventPubService);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, MyLocation.GO_TO_MAP, 0, R.string.gotomap).setIcon(android.R.drawable.ic_dialog_map);
        menu.add(0, MyLocation.STOP_GENIE, 1, R.string.stop_genie).setIcon(android.R.drawable.ic_menu_close_clear_cancel);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        Intent intent = null;
        switch(item.getItemId()) {
            case GO_TO_MAP:
                intent = new Intent(this, ShowMapActivity.class);
                startActivity(intent);
                return true;
            case STOP_GENIE:
                intent = new Intent("com.Gpslocation.www.EventPublishingService.SERVICE");
                stopService(intent);
                finish();
                return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }
}
