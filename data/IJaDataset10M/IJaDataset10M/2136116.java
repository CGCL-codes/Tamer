package cn.chengdu.in.android;

import java.util.List;
import android.content.Intent;
import android.os.Bundle;
import cn.chengdu.in.android.app.AbstractMapAct;
import cn.chengdu.in.android.app.MapOverlay;
import cn.chengdu.in.android.config.Config;
import cn.chengdu.in.android.location.LocationUtils;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.mobclick.android.MobclickAgent;

/**
 * 
 * @author Declan.Z(declan.zhang@gmail.com)
 * @date 2011-5-27
 */
public class PlaceMapAct extends AbstractMapAct {

    private static final boolean DEBUG = Config.DEBUG;

    private static final String TAG = "PlaceMapAct";

    private MapView mMapView;

    private App mApp;

    private MapOverlay mPlaceOverlay;

    private MapOverlay mMyOverlay;

    private List<Overlay> mOverlays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_map);
        Intent intent = getIntent();
        GeoPoint point = LocationUtils.getGeoPoint(intent.getStringExtra("lat"), intent.getStringExtra("lng"));
        mMapView = (MapView) findViewById(R.id.mapView);
        mApp = (App) getApplicationContext();
        mMapView.setBuiltInZoomControls(true);
        mOverlays = mMapView.getOverlays();
        mPlaceOverlay = new MapOverlay(R.drawable.ic_location, this);
        showPlaceOverlay(point);
        if (!getIntent().getBooleanExtra("fromPlaceList", false) && mApp.getLastLocation() != null) {
            showMyOverlay();
        }
        MapController mapController = mMapView.getController();
        mapController.animateTo(point);
        mapController.setZoom(15);
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

    private void showPlaceOverlay(GeoPoint point) {
        OverlayItem overlayitem = new OverlayItem(point, null, null);
        mPlaceOverlay.addOverlay(overlayitem);
        mOverlays.add(mPlaceOverlay);
    }

    private void showMyOverlay() {
        if (mMyOverlay == null) {
            mMyOverlay = new MapOverlay(R.drawable.ic_menu_emoticons, this);
            mOverlays.add(mMyOverlay);
        }
        GeoPoint myPoint = LocationUtils.getGeoPoint(mApp.getLastLocation());
        OverlayItem myLocation = new OverlayItem(myPoint, null, null);
        mMyOverlay.resetOverlay(myLocation);
    }
}
