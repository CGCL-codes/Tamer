package de.fhkl.mHelloWorld.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Contacts.People;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class FriendFinderMap extends MapActivity {

    private static final String MY_LOCATION_CHANGED_ACTION = new String("android.intent.action.LOCATION_CHANGED");

    /** Minimum distance in meters for a friend 
	 * to be recognize as a Friend to be drawn */
    protected static final int NEARFRIEND_MAX_DISTANCE = 10000000;

    final Handler mHandler = new Handler();

    protected boolean doUpdates = true;

    protected MapView myMapView = null;

    protected MapController myMapController = null;

    protected List<Overlay> overlays = null;

    protected LocationManager myLocationManager = null;

    protected Location myLocation = null;

    protected MyIntentReceiver myIntentReceiver = null;

    protected final IntentFilter myIntentFilter = new IntentFilter(MY_LOCATION_CHANGED_ACTION);

    /** List of friends in */
    protected ArrayList<Friend> nearFriends = new ArrayList<Friend>();

    protected boolean satelliteviewIsSet = true;

    /**
	 * This tiny IntentReceiver updates
	 * our stuff as we receive the intents 
	 * (LOCATION_CHANGED_ACTION) we told the 
	 * myLocationManager to send to us. 
	 */
    class MyIntentReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (FriendFinderMap.this.doUpdates) FriendFinderMap.this.updateView();
        }
    }

    class myLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            if (FriendFinderMap.this.doUpdates) FriendFinderMap.this.updateView();
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

    /**
	 * This method is so huge, 
	 * because it does a lot of FANCY painting.
	 * We could shorten this method to a few lines.
	 * But as users like eye-candy apps ;) ...
	 */
    protected class MyLocationOverlay extends com.google.android.maps.Overlay {

        @Override
        public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) {
            super.draw(canvas, mapView, shadow);
            Paint paint = new Paint();
            paint.setTextSize(14);
            Double lat = FriendFinderMap.this.myLocation.getLatitude() * 1E6;
            Double lng = FriendFinderMap.this.myLocation.getLongitude() * 1E6;
            GeoPoint point = new GeoPoint(lat.intValue(), lng.intValue());
            Point myScreenCoords = new Point();
            mapView.getProjection().toPixels(point, myScreenCoords);
            RectF oval = new RectF(myScreenCoords.x - 7, myScreenCoords.y + 7, myScreenCoords.x + 7, myScreenCoords.y - 7);
            paint.setStyle(Style.FILL);
            paint.setARGB(255, 80, 150, 30);
            canvas.drawText(getString(R.string.map_overlay_own_name), myScreenCoords.x + 9, myScreenCoords.y, paint);
            paint.setARGB(80, 156, 192, 36);
            paint.setStrokeWidth(1);
            canvas.drawOval(oval, paint);
            paint.setARGB(255, 0, 0, 0);
            paint.setStyle(Style.STROKE);
            canvas.drawCircle(myScreenCoords.x, myScreenCoords.y, 7, paint);
            Point friendScreenCoords = new Point();
            for (Friend aFriend : FriendFinderMap.this.nearFriends) {
                lat = aFriend.itsLocation.getLatitude() * 1E6;
                lng = aFriend.itsLocation.getLongitude() * 1E6;
                point = new GeoPoint(lat.intValue(), lng.intValue());
                mapView.getProjection().toPixels(point, friendScreenCoords);
                if (Math.abs(friendScreenCoords.x) < 2000 && Math.abs(friendScreenCoords.y) < 2000) {
                    oval = new RectF(friendScreenCoords.x - 7, friendScreenCoords.y + 7, friendScreenCoords.x + 7, friendScreenCoords.y - 7);
                    paint.setStyle(Style.FILL);
                    paint.setARGB(255, 255, 0, 0);
                    canvas.drawText(aFriend.itsName, friendScreenCoords.x + 9, friendScreenCoords.y, paint);
                    paint.setARGB(80, 255, 0, 0);
                    paint.setStrokeWidth(2);
                    canvas.drawLine(myScreenCoords.x, myScreenCoords.y, friendScreenCoords.x, friendScreenCoords.y, paint);
                    paint.setStrokeWidth(1);
                    canvas.drawOval(oval, paint);
                    paint.setARGB(255, 0, 0, 0);
                    paint.setStyle(Style.STROKE);
                    canvas.drawCircle(friendScreenCoords.x, friendScreenCoords.y, 7, paint);
                }
            }
            return true;
        }
    }

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        Log.i("Class FriendFinderMap", "onCreate");
        this.myMapView = new MapView(this, "0dTS7pWjetgQ8VSAx6f0PreVQ_5pnFeJsb0SeTg");
        myMapView.setClickable(true);
        this.setContentView(myMapView);
        this.overlays = this.myMapView.getOverlays();
        MyLocationOverlay myLocationOverlay = new MyLocationOverlay();
        this.overlays.add(myLocationOverlay);
        myMapController = this.myMapView.getController();
        myMapController.setZoom(2);
        this.myLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        this.updateView();
        this.setupForGPSAutoRefreshing();
        this.refreshFriendsList(NEARFRIEND_MAX_DISTANCE);
    }

    /**
	 * Restart the receiving, when we are back on line.
	 */
    @Override
    public void onResume() {
        super.onResume();
        this.doUpdates = true;
        registerReceiver(this.myIntentReceiver, this.myIntentFilter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean supRetVal = super.onCreateOptionsMenu(menu);
        menu.add(0, 0, 0, getString(R.string.map_menu_zoom_in));
        menu.add(0, 1, 0, getString(R.string.map_menu_zoom_out));
        menu.add(0, 2, 0, getString(R.string.map_menu_toggle_street_satellite));
        menu.add(0, 3, 0, getString(R.string.map_menu_back_to_list));
        return supRetVal;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Double lat = FriendFinderMap.this.myLocation.getLatitude() * 1E6;
        Double lng = FriendFinderMap.this.myLocation.getLongitude() * 1E6;
        GeoPoint point = new GeoPoint(lat.intValue(), lng.intValue());
        switch(item.getItemId()) {
            case 0:
                this.myMapController.zoomIn();
                this.myMapController.setCenter(point);
                return true;
            case 1:
                this.myMapController.zoomOut();
                this.myMapController.setCenter(point);
                return true;
            case 2:
                myMapView.setSatellite(satelliteviewIsSet);
                satelliteviewIsSet = !satelliteviewIsSet;
                return true;
            case 3:
                this.finish();
                return true;
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_I) {
            this.myMapController.zoomIn();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_O) {
            this.myMapController.zoomOut();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_T && satelliteviewIsSet == false) {
            myMapView.setSatellite(true);
            Log.i("FriendFinderMap-onKeyDown()", "switch-to-satelliteview");
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_T && satelliteviewIsSet == true) {
            myMapView.setStreetView(true);
            Log.i("FriendFinderMap-onKeyDown()", "switch-to-satelliteview");
            return true;
        }
        return false;
    }

    private void setupForGPSAutoRefreshing() {
        final long MINIMUM_DISTANCECHANGE_FOR_UPDATE = 25;
        final long MINIMUM_TIME_BETWEEN_UPDATE = 5000;
        List<String> providers = this.myLocationManager.getAllProviders();
        String strProvider = providers.get(0);
        LocationListener listener = new myLocationListener();
        this.myLocationManager.requestLocationUpdates(strProvider, MINIMUM_TIME_BETWEEN_UPDATE, MINIMUM_DISTANCECHANGE_FOR_UPDATE, listener);
        this.myIntentReceiver = new MyIntentReceiver();
    }

    private void updateView() {
        this.myLocation = myLocationManager.getLastKnownLocation("gps");
        this.myMapView.invalidate();
    }

    private void refreshFriendsList(long maxDistanceInMeter) {
        Cursor c = getContentResolver().query(People.CONTENT_URI, null, null, null, People.NAME + " ASC");
        startManagingCursor(c);
        c.moveToFirst();
        int notesColumn = c.getColumnIndex(People.NOTES);
        int nameColumn = c.getColumnIndex(People.NAME);
        if (c.isFirst()) {
            do {
                String notesString = c.getString(notesColumn);
                Location friendLocation = null;
                if (notesString != null) {
                    final String geoPattern = "(geo:[\\-]?[0-9]{1,3}\\.[0-9]{1,6}\\,[\\-]?[0-9]{1,3}\\.[0-9]{1,6}\\#)";
                    Pattern pattern = Pattern.compile(geoPattern);
                    CharSequence inputStr = notesString;
                    Matcher matcher = pattern.matcher(inputStr);
                    boolean matchFound = matcher.find();
                    if (matchFound) {
                        String groupStr = matcher.group(0);
                        friendLocation = new Location(geoPattern);
                        String latid = groupStr.substring(groupStr.indexOf(":") + 1, groupStr.indexOf(","));
                        String longit = groupStr.substring(groupStr.indexOf(",") + 1, groupStr.indexOf("#"));
                        friendLocation.setLongitude(Float.parseFloat(longit));
                        friendLocation.setLatitude(Float.parseFloat(latid));
                    }
                }
                if (friendLocation != null && this.myLocation.distanceTo(friendLocation) < maxDistanceInMeter) {
                    String friendName = c.getString(nameColumn);
                    nearFriends.add(new Friend(friendLocation, friendName));
                }
            } while (c.moveToNext());
        }
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
}
