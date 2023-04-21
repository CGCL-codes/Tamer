package mhh.hsnr.de;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MyActivity extends Activity {

    public static final int FINISH_ALL = 1000;

    public static final int FINISH_UNTIL_MAIN = 1001;

    public void setStatus(String status) {
        SharedPreferences preferences = getSharedPreferences("mobilehitchhiker", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Status", status);
        editor.commit();
        getStatus();
    }

    public void getStatus() {
        SharedPreferences preferences = getSharedPreferences("mobilehitchhiker", MODE_PRIVATE);
        TextView t = (TextView) findViewById(R.id.MainTextStatus);
        String s = getString(R.string.MainTextStatus);
        t.setText(s + " " + preferences.getString("Status", ""));
    }

    public void gpsStatus() {
        SharedPreferences preferences = getSharedPreferences("mobilehitchhiker", MODE_PRIVATE);
        String GpsFix = preferences.getString("GpsFix", "-");
        if (GpsFix == "-") {
            setStatus("GPS-Position wird ermittelt");
        } else setStatus("Bereit");
    }

    public void clearPreferences() {
        SharedPreferences preferences = getSharedPreferences("mobilehitchhiker", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }

    public void clearGps() {
        SharedPreferences preferences = getSharedPreferences("mobilehitchhiker", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("GpsLat");
        editor.remove("GpsLng");
        editor.remove("GpsAcc");
        editor.remove("GpsFix");
        editor.commit();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuItem item = menu.add(R.string.MenuPreferences);
        item.setIcon(android.R.drawable.ic_menu_preferences);
        item = menu.add(R.string.MenuClear);
        item.setIcon(android.R.drawable.ic_menu_delete);
        item = menu.add(R.string.MenuAbout);
        item.setIcon(android.R.drawable.ic_menu_info_details);
        item = menu.add(R.string.MenuExit);
        item.setIcon(android.R.drawable.ic_menu_close_clear_cancel);
        item = menu.add("DEV: Offer");
        item = menu.add("DEV: Withdraw");
        item = menu.add("DEV: fake GPS");
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.hasSubMenu() == false) {
            if (item.getTitle() == getString(R.string.MenuExit)) {
                setResult(FINISH_ALL);
                finish();
            }
            if (item.getTitle() == getString(R.string.MenuClear)) {
                clearPreferences();
                Intent i = new Intent();
                i.setClass(MyActivity.this, Main.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
            if (item.getTitle() == "DEV: Offer") {
                SharedPreferences preferences = getSharedPreferences("mobilehitchhiker", MODE_PRIVATE);
                double lat = Double.valueOf(preferences.getString("GpsLat", "0.0"));
                double lng = Double.valueOf(preferences.getString("GpsLng", "0.0"));
                double destLat = Double.valueOf(preferences.getString("destLat", "0.0"));
                double destLng = Double.valueOf(preferences.getString("destLng", "0.0"));
                String dest = preferences.getString("dest", "-");
                Comm c = new Comm();
                c.offer(preferences.getString("username", "none"), preferences.getString("password", "none"), lat, lng, dest, destLat, destLng);
            }
            if (item.getTitle() == "DEV: Withdraw") {
                SharedPreferences preferences = getSharedPreferences("mobilehitchhiker", MODE_PRIVATE);
                Comm c = new Comm();
                c.withdraw(preferences.getString("username", "none"), preferences.getString("password", "none"));
            }
            if (item.getTitle() == "DEV: fake GPS") {
                SharedPreferences preferences = getSharedPreferences("mobilehitchhiker", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("GpsLat", "51.35");
                editor.putString("GpsLng", "6.55");
                editor.putString("GpsFix", "manual");
                editor.commit();
                gpsStatus();
            }
        }
        return true;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(resultCode) {
            case FINISH_ALL:
                this.setResult(FINISH_ALL);
                this.finish();
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
