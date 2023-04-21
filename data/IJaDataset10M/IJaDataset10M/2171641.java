package agrobot.navigo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import android.R.integer;
import android.draw.GeoUtils;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.draw.RadarView;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class Navigation extends Activity implements LocationListener {

    public static final String LOG_TAG = "Navigation";

    private static final String MOCK_GPS_PROVIDER_INDEX = "NavigationIndex";

    private MockGpsProvider mMockGpsProviderTask = null;

    private Integer mMockGpsProviderIndex = 0;

    private static final int MENU_STANDARD = Menu.FIRST + 1;

    private static final int MENU_METRIC = Menu.FIRST + 2;

    private static final String RADAR = "radar";

    private static final String PREF_METRIC = "metric";

    private RadarView radarView;

    private SharedPreferences mPrefs;

    private double mTargetLat;

    private double mTargetLon;

    private double mMyLocationLat;

    private double mMyLocationLon;

    private double mDistance;

    @Override
    public void onBackPressed() {
        NavigoActivity ParentActivity;
        ParentActivity = (NavigoActivity) this.getParent();
        ParentActivity.onBackPressed_Activity();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation);
        if (savedInstanceState instanceof Bundle) {
            mMockGpsProviderIndex = savedInstanceState.getInt(MOCK_GPS_PROVIDER_INDEX, 0);
        }
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        } else if (!locationManager.isProviderEnabled(MockGpsProvider.GPS_MOCK_PROVIDER)) {
            locationManager.addTestProvider(MockGpsProvider.GPS_MOCK_PROVIDER, false, false, false, false, true, false, false, 0, 5);
            locationManager.setTestProviderEnabled(MockGpsProvider.GPS_MOCK_PROVIDER, true);
        }
        if (locationManager.isProviderEnabled(MockGpsProvider.GPS_MOCK_PROVIDER)) {
            locationManager.requestLocationUpdates(MockGpsProvider.GPS_MOCK_PROVIDER, 0, 0, this);
            try {
                List<String> data = new ArrayList<String>();
                InputStream is = getAssets().open("mock_gps_data.csv");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    data.add(line);
                }
                String[] coordinates = new String[data.size()];
                data.toArray(coordinates);
                mMockGpsProviderTask = new MockGpsProvider();
                mMockGpsProviderTask.execute(coordinates);
            } catch (Exception e) {
            }
        }
        radarView = (RadarView) findViewById(R.id.satView);
        mPrefs = getSharedPreferences(RADAR, MODE_PRIVATE);
        boolean useMetric = mPrefs.getBoolean(PREF_METRIC, false);
        radarView.setUseMetric(useMetric);
        Intent i = getIntent();
        if (targetPoints.getTargetPoint() != null) {
            mTargetLat = targetPoints.getTargetPoint().getLatitudeE6() / (double) GeoUtils.MILLION;
            mTargetLon = targetPoints.getTargetPoint().getLongitudeE6() / (double) GeoUtils.MILLION;
        } else {
            int latE6 = (int) (i.getFloatExtra("latitude", (float) -25.113324) * GeoUtils.MILLION);
            int lonE6 = (int) (i.getFloatExtra("longitude", (float) -50.144996) * GeoUtils.MILLION);
            mTargetLat = latE6 / (double) GeoUtils.MILLION;
            mTargetLon = lonE6 / (double) GeoUtils.MILLION;
        }
    }

    /** Define a mock GPS provider as an asynchronous task of this Activity. */
    private class MockGpsProvider extends AsyncTask<String, Integer, Void> {

        public static final String LOG_TAG = "GpsMockProvider";

        public static final String GPS_MOCK_PROVIDER = "GpsMockProvider";

        /** Keeps track of the currently processed coordinate. */
        public Integer index = 0;

        @Override
        protected Void doInBackground(String... data) {
            for (String str : data) {
                if (index < mMockGpsProviderIndex) {
                    index++;
                    continue;
                }
                publishProgress(index);
                Double latitude = null;
                Double longitude = null;
                Double altitude = null;
                try {
                    String[] parts = str.split(",");
                    latitude = Double.valueOf(parts[0]);
                    longitude = Double.valueOf(parts[1]);
                    altitude = Double.valueOf(parts[2]);
                } catch (NullPointerException e) {
                    break;
                } catch (Exception e) {
                    continue;
                }
                Location location = new Location(GPS_MOCK_PROVIDER);
                location.setLatitude(latitude);
                location.setLongitude(longitude);
                location.setAltitude(altitude);
                location.setTime(System.currentTimeMillis());
                Log.d(LOG_TAG, location.toString());
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                locationManager.setTestProviderLocation(GPS_MOCK_PROVIDER, location);
                try {
                    Thread.sleep(1000);
                    if (Thread.currentThread().isInterrupted()) throw new InterruptedException("");
                } catch (InterruptedException e) {
                    break;
                }
                index++;
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            Log.d(LOG_TAG, "onProgressUpdate():" + values[0]);
            mMockGpsProviderIndex = values[0];
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (targetPoints.getTargetPoint() != null) {
            mTargetLat = targetPoints.getTargetPoint().getLatitudeE6() / (double) GeoUtils.MILLION;
            mTargetLon = targetPoints.getTargetPoint().getLongitudeE6() / (double) GeoUtils.MILLION;
        }
        mMyLocationLat = location.getLatitude();
        mMyLocationLon = location.getLongitude();
        int mMyLocationLatDeg = (int) Math.abs(mMyLocationLat);
        int mMyLocationLatMin = (int) Math.abs((mMyLocationLat % 1) * 60);
        int mMyLocationLatSec = (int) Math.abs(Math.round(((((mMyLocationLat % 1) * 60) % 1) * 60)));
        String StrMyLocationLat = String.format(Locale.getDefault(), "%d�%02d'%02d\"", mMyLocationLatDeg, mMyLocationLatMin, mMyLocationLatSec);
        String south = mMyLocationLat < 0 ? "S" : "N";
        int mMyLocationLonDeg = (int) Math.abs(mMyLocationLon);
        int mMyLocationLonMin = (int) Math.abs((mMyLocationLon % 1) * 60);
        int mMyLocationLonSec = (int) Math.abs(Math.round(((((mMyLocationLon % 1) * 60) % 1) * 60)));
        String StrMyLocationLon = String.format(Locale.getDefault(), "%d�%02d'%02d\"", mMyLocationLonDeg, mMyLocationLonMin, mMyLocationLonSec);
        String west = mMyLocationLon < 0 ? "O" : "L";
        mDistance = GeoUtils.distanceKm(mMyLocationLat, mMyLocationLon, mTargetLat, mTargetLon);
        double ang = GeoUtils.bearing(mMyLocationLat, mMyLocationLon, mTargetLat, mTargetLon);
        radarView.updateDistance(mDistance);
        Typeface LCDTypeface = Typeface.createFromAsset(this.getAssets(), "DS-DIGII.TTF");
        TextView textDistance = (TextView) findViewById(R.id.distanceView);
        textDistance.setText(radarView.getDistanceView());
        TextView textAngle = (TextView) findViewById(R.id.angleView);
        textAngle.setText(String.valueOf(Math.round(ang)));
        TextView viewLatitude = (TextView) findViewById(R.id.latitude);
        viewLatitude.setTypeface(LCDTypeface);
        viewLatitude.setTextSize(25);
        viewLatitude.setText(StrMyLocationLat);
        TextView viewSouth = (TextView) findViewById(R.id.south);
        viewSouth.setText(south);
        TextView viewLongitude = (TextView) findViewById(R.id.longitude);
        viewLongitude.setTypeface(LCDTypeface);
        viewLongitude.setTextSize(25);
        viewLongitude.setText(StrMyLocationLon);
        TextView viewWest = (TextView) findViewById(R.id.west);
        viewWest.setText(west);
        TextView viewInfo = (TextView) findViewById(R.id.information);
        viewInfo.setTypeface(LCDTypeface);
        viewInfo.setTextSize(25);
        if (targetPoints.getTargetPoint() != null) {
            viewInfo.setText("TESTE.: " + targetPoints.getTargetPoint().getLatitudeE6());
            if ((targetPoints.getFirstAngle() == 0)) {
                targetPoints.setFirstAngle(ang);
                Toast.makeText(getBaseContext(), "entrou" + ang, Toast.LENGTH_SHORT).show();
            }
        } else viewInfo.setText("Nenhum alvo marcado");
        TextView viewAng = (TextView) findViewById(R.id.firstAngle);
        viewAng.setTypeface(LCDTypeface);
        viewAng.setTextSize(25);
        viewAng.setText(String.valueOf(Math.round(targetPoints.getFirstAngle())));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, MENU_STANDARD, 0, R.string.menu_standard).setIcon(R.drawable.ic_menu_standard).setAlphabeticShortcut('A');
        menu.add(0, MENU_METRIC, 0, R.string.menu_metric).setIcon(R.drawable.ic_menu_metric).setAlphabeticShortcut('C');
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case MENU_STANDARD:
                {
                    setUseMetric(false);
                    return true;
                }
            case MENU_METRIC:
                {
                    setUseMetric(true);
                    return true;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUseMetric(boolean useMetric) {
        SharedPreferences.Editor e = mPrefs.edit();
        e.putBoolean(PREF_METRIC, useMetric);
        e.commit();
        radarView.setUseMetric(useMetric);
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
}
