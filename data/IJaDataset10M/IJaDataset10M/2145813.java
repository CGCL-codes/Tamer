package com.android.footmap.locationservice;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import com.android.footmap.common.Constants;
import com.android.footmap.dbservice.DBManager;
import com.android.footmap.dbservice.DBUtil;

public class GPSService implements LocationService {

    MyLocationListener locationListener = new MyLocationListener();

    LocationManager myLocationManager;

    Context m_ctx;

    public GPSService(Context ctx) {
        init(ctx);
    }

    private void init(Context ctx) {
        m_ctx = ctx;
        myLocationManager = (LocationManager) m_ctx.getSystemService(Context.LOCATION_SERVICE);
    }

    public boolean isServiceAvailble() {
        if (myLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) return true; else return false;
    }

    public int resumeService() {
        Log.d("GPS", "Resume GPS Service");
        myLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        return 0;
    }

    public int suspendService() {
        Log.d("GPS", "Suspend GPS listener");
        myLocationManager.removeUpdates(locationListener);
        return 0;
    }

    public int stopService() {
        Log.d("GPS", "Remove GPS Service");
        myLocationManager.removeUpdates(locationListener);
        return 0;
    }

    public int startService() {
        Log.d("GPS", "Start GPS Service");
        resumeService();
        return 0;
    }

    public int getCurrentLocation(double[] location) {
        int result = Constants.DAO_OK;
        DBManager dbmanager = DBManager.getDBManager();
        SQLiteDatabase con = dbmanager.openDBConnetion();
        String[] columns = new String[] { "value" };
        Cursor cursor = con.query(Constants.TABLE_GLOBAL_SETTING, columns, "section='location_service' and key='current_location'", null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            String location_str = cursor.getString(0);
            location[0] = Double.parseDouble(location_str.substring(0, location_str.indexOf(';')));
            location[1] = Double.parseDouble(location_str.substring(location_str.indexOf(';') + 1));
            dbmanager.closeDBConnetion();
            Log.d("GPS latitude", "" + location[0]);
            Log.d("GPS longitude", "" + location[1]);
        } else result = Constants.DAO_ERROR;
        return result;
    }

    private class MyLocationListener implements LocationListener {

        public void onLocationChanged(Location loc) {
            if (loc != null) {
                Log.d("GPS Latitude", loc.getLatitude() + "");
                Log.d("GPS Longitude", loc.getLongitude() + "");
                DBUtil.traceLocationInDB(loc.getLatitude(), loc.getLongitude());
                DBUtil.updateGlobalSetting("location_service", "current_location", loc.getLatitude() + ";" + loc.getLongitude(), null);
            }
        }

        public void onProviderDisabled(String provider) {
            Log.d("GPS", "GPS hardware disable");
        }

        public void onProviderEnabled(String provider) {
            Log.d("GPS", "GPS hardware enable");
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }
}
