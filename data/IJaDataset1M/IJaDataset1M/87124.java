package opl.textc;

import opl.textc.util.AppRater;
import opl.textc.util.Util;
import opl.textc.util.Util.GetPrediction;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MoreActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more);
    }

    public void startSubwayMap(View button) {
        Intent i = new Intent(this, SubwayMap.class);
        startActivity(i);
    }

    public void launchBrowserSubwayTimes(View button) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www3.ttc.ca/Subway/Stations_A-Z.jsp"));
        startActivity(browserIntent);
    }

    public void startSettings(View button) {
        Intent settingsActivity = new Intent(getBaseContext(), Preferences.class);
        startActivity(settingsActivity);
    }

    public void startAlertsDialog(View button) {
        if (Constants.IS_PRO) {
            Util.UTIL_INSTANCE.new GetAlerts(false, this).execute();
        } else {
            Toast.makeText(this, "This feature can only be used in the donation version of this app, which can be purchased for only $1. You remove adds, gain more features and support development.", Toast.LENGTH_LONG).show();
            AppRater.showRateDialog(this, null);
        }
    }
}
