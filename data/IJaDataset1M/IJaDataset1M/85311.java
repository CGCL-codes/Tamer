package mhh.hsnr.de;

import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Offer extends MyActivity {

    private double destLat, destLng;

    private String address;

    private ProgressDialog pg;

    private Geocoder gc;

    SocketClient sc;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offer);
        TextView t = (TextView) findViewById(R.id.MainTextHead);
        t.setTextColor(Color.WHITE);
        t.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        gc = new Geocoder(this);
        Button b = (Button) findViewById(R.id.OfferButtonOffer);
        b.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                getLocation();
            }
        });
        gpsStatus();
    }

    @Override
    public void onResume() {
        super.onResume();
        gpsStatus();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void getLocation() {
        TextView t = (TextView) findViewById(R.id.OfferEditDest);
        address = t.getText().toString();
        pg = ProgressDialog.show(Offer.this, getString(R.string.OfferPgTitle), getString(R.string.OfferPgMsg), true);
        Thread thread = new Thread() {

            public void run() {
                try {
                    List<Address> foundAdresses = gc.getFromLocationName(address, 5);
                    Address x = foundAdresses.get(0);
                    destLat = x.getLatitude();
                    destLng = x.getLongitude();
                    SharedPreferences preferences = getSharedPreferences("mobilehitchhiker", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("dest", address);
                    editor.putString("destLat", String.valueOf(destLat));
                    editor.putString("destLng", String.valueOf(destLng));
                    editor.commit();
                    handler.sendEmptyMessage(0);
                } catch (Exception e) {
                    handler.sendEmptyMessage(1);
                }
            }
        };
        thread.start();
    }

    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                pg.dismiss();
                Toast t = Toast.makeText(Offer.this, getString(R.string.OfferToastFailed), Toast.LENGTH_SHORT);
                t.setGravity(Gravity.CENTER, t.getXOffset() / 2, t.getYOffset() / 2);
                t.show();
            } else {
                SharedPreferences preferences = getSharedPreferences("mobilehitchhiker", MODE_PRIVATE);
                String GpsFix = preferences.getString("GpsFix", "-");
                if (GpsFix == "-") {
                    pg.setMessage(getString(R.string.OfferPgMsg2));
                    GpsService.registerOfferActivity(Offer.this);
                } else gpshandler();
            }
        }
    };

    public void gpshandler() {
        SharedPreferences preferences = getSharedPreferences("mobilehitchhiker", MODE_PRIVATE);
        double lat = Double.valueOf(preferences.getString("GpsLat", "0.0"));
        double lng = Double.valueOf(preferences.getString("GpsLng", "0.0"));
        pg.setMessage(getString(R.string.OfferPgMsg3));
        Comm c = new Comm();
        c.handler = conntesthandler;
        c.offer(preferences.getString("username", "none"), preferences.getString("password", "none"), lat, lng, address, destLat, destLng);
    }

    private Handler conntesthandler = new Handler() {

        public void handleMessage(Message msg) {
            Bundle bundle = new Bundle();
            bundle = msg.getData();
            String s = bundle.getString("response");
            String errormsg = "";
            int error = 0;
            try {
                JSONObject json = new JSONObject(s);
                error = json.getInt("error");
                errormsg = json.getString("errormsg");
            } catch (JSONException e) {
                error = bundle.getInt("error");
            }
            if (pg.isShowing() == true) if (error != 0) {
                pg.dismiss();
                Toast t;
                if (errormsg == "") {
                    t = Toast.makeText(Offer.this, getString(R.string.ErrorHttp) + " " + String.valueOf(error), Toast.LENGTH_SHORT);
                } else {
                    t = Toast.makeText(Offer.this, getString(R.string.ErrorHttp) + " " + errormsg, Toast.LENGTH_SHORT);
                }
                t.setGravity(Gravity.CENTER, t.getXOffset() / 2, t.getYOffset() / 2);
                t.show();
                finish();
            } else {
                SharedPreferences preferences = getSharedPreferences("mobilehitchhiker", MODE_PRIVATE);
                String user = preferences.getString("username", "");
                String pw = preferences.getString("password", "");
                pg.setMessage(getString(R.string.OfferPgMsg4));
                SocketClient.registerOfferActivity(Offer.this);
                sc = new SocketClient();
                sc.handler = sockethandler;
                sc.setLogin(user, pw);
                sc.start();
            }
            return;
        }
    };

    public Handler sockethandler = new Handler() {

        public void handleMessage(Message msg) {
            Bundle bundle = new Bundle();
            bundle = msg.getData();
            String s = bundle.getString("response");
            int error = bundle.getInt("error");
            pg.dismiss();
            Toast t = null;
            if (error != 0) {
                SharedPreferences preferences = getSharedPreferences("mobilehitchhiker", MODE_PRIVATE);
                String user = preferences.getString("username", "");
                String pw = preferences.getString("password", "");
                Comm c = new Comm();
                c.withdraw(user, pw);
                if (error == 1) {
                    t = Toast.makeText(Offer.this, getString(R.string.ErrorSocket) + " " + getString(R.string.ErrorSocketTimeout), Toast.LENGTH_SHORT);
                }
                if (error == 2) {
                }
                t.setGravity(Gravity.CENTER, t.getXOffset() / 2, t.getYOffset() / 2);
                t.show();
            } else {
                if (s.equals("LOGINOK")) {
                    Drive.sc = sc;
                    setStatus("Warte auf Mitfahrer...");
                    Intent myIntent = new Intent(Offer.this, Drive.class);
                    startActivityForResult(myIntent, 0);
                } else {
                    if (error != 2) {
                        t = Toast.makeText(Offer.this, getString(R.string.ErrorSocket) + " " + s, Toast.LENGTH_SHORT);
                        t.setGravity(Gravity.CENTER, t.getXOffset() / 2, t.getYOffset() / 2);
                        t.show();
                    }
                }
            }
        }
    };
}
