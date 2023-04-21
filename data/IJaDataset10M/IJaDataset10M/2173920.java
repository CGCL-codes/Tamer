package com.michael.gps.view;

import java.util.Timer;
import java.util.TimerTask;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.michael.common.Factory;
import com.michael.gps.R;
import com.michael.gps.service.GpsService;

/**
 * @author Administrator
 * 
 */
public class MyMapView extends MapActivity {

    private MapView mapView;

    private MapController mc;

    private MyLocationOverlay mo;

    private ItemizedOverlay io;

    private long[] friends;

    private static final long period = 30000;

    private GpsService gpsService = (GpsService) Factory.getInstance(GpsService.class);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        Bundle b = this.getIntent().getBundleExtra("bundle");
        friends = b.getLongArray("friends");
        long phone = b.getLong("phone");
        mapView = (MapView) findViewById(R.id.map);
        mapView.setStreetView(true);
        mapView.setBuiltInZoomControls(true);
        mapView.setEnabled(true);
        mapView.setClickable(true);
        mc = mapView.getController();
        mc.setZoom(13);
        mc.setCenter(new GeoPoint(39916319, 116390683));
        this.reloadLocation(phone);
    }

    private void reloadLocation(final long phone) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            long lastTime = System.currentTimeMillis() - 86400000;

            @Override
            public void run() {
                String[] locations = gpsService.findLocation(phone, lastTime);
                lastTime = System.currentTimeMillis();
                if (locations != null) {
                    for (String string : locations) {
                        String[] location = string.split(",");
                        GeoPoint gp = new GeoPoint(((Double) (Double.parseDouble(location[0]) * 1E6)).intValue(), ((Double) (Double.parseDouble(location[1]) * 1E6)).intValue());
                        mc.animateTo(gp);
                        Log.d("reload location:", gp.toString());
                    }
                }
            }
        }, period);
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.map_menu, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
            case R.id.traffic:
                mapView.setSatellite(false);
                mapView.setStreetView(false);
                mapView.setTraffic(true);
                break;
            case R.id.satellite:
                mapView.setTraffic(false);
                mapView.setStreetView(false);
                mapView.setSatellite(true);
                break;
            case R.id.streetView:
                mapView.setTraffic(false);
                mapView.setSatellite(false);
                mapView.setStreetView(true);
                break;
            case R.id.reutrnTab:
                Intent intent = new Intent();
                intent.setClass(this, TabView.class);
                Bundle b = new Bundle();
                b.putLongArray("friends", friends);
                intent.putExtra("bundle", b);
                this.startActivity(intent);
                this.finish();
                break;
        }
        return true;
    }
}
