package com.cs2340.practice;

import java.util.ArrayList;
import java.util.List;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

public class Map extends MapActivity {

    MapView map_view;

    MapController mc;

    GeoPoint gp;

    LocationDataBaseAdapter locDB;

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapview);
        map_view = (MapView) findViewById(R.id.mapView);
        map_view.setBuiltInZoomControls(true);
        map_view.setSatellite(true);
        mc = map_view.getController();
        String coordinates[] = { "33.7489", "-84.3881" };
        double lat = Double.parseDouble(coordinates[0]);
        double lng = Double.parseDouble(coordinates[1]);
        gp = new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));
        mc.animateTo(gp);
        mc.setZoom(5);
        map_view.invalidate();
        Toast.makeText(getBaseContext(), "To select a point, press down for at least 5 seconds.", Toast.LENGTH_LONG).show();
        ;
        MapsOverlay mapOverlay = new MapsOverlay(this);
        List<Overlay> listOfOverlays = map_view.getOverlays();
        listOfOverlays.clear();
        listOfOverlays.add(mapOverlay);
        map_view.invalidate();
    }

    public GeoPoint getGP() {
        return gp;
    }

    public void storeLocation(String lat, String lon) {
        locDB = new LocationDataBaseAdapter(this);
        locDB.openToWrite();
        EditText tname;
        tname = (EditText) findViewById(R.id.locationFinder);
        String tnamestr = tname.getText().toString();
        locDB.insert(tnamestr, lon, lat);
        locDB.close();
    }
}
