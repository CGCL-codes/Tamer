package com.android.alberthernandez.taksinow;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;

/**
 * Main Activity of "Taksi Now!". It displays a map interface
 * where the driving directions are shown.
 * 
 * @author		Albert Hernández López
 * @version		1.0
 * @since       1.0
 */
public class MainActivity extends MapActivity {

    private static final int MENU_ADD_NEW_RIDE = Menu.FIRST;

    private static final int MENU_REM_CUR_RIDE = Menu.FIRST + 1;

    private static final int MENU_LIST = Menu.FIRST + 2;

    private static final int MENU_EXIT = Menu.FIRST + 3;

    private static final int OPEN_ADD_ROUTE_IN = 1;

    private static final int OPEN_LIST_RIDES = 2;

    private MapController mapController;

    private RideManager rideManager;

    private Route currentRoute;

    private RouteOverlay routeOverlay;

    private GeoPoint currentPosition;

    private ProgressDialog pd;

    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            switch(msg.what) {
                case 0:
                    pd.dismiss();
                    break;
                case 1:
                    doCalculations = true;
                    break;
                case 2:
                    doCalculations = false;
                    break;
            }
        }
    };

    private int nextPlacemark;

    private boolean isConnectedToInet;

    private boolean doCalculations;

    private final LocationListener locationListener = new LocationListener() {

        public void onLocationChanged(final Location location) {
            Thread thread = new Thread() {

                public void run() {
                    try {
                        updateWithNewLocation(location);
                    } catch (Exception e) {
                    }
                }
            };
            thread.run();
        }

        public void onProviderDisabled(String provider) {
            updateWithNewLocation(null);
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            if (status == LocationProvider.AVAILABLE) {
                handler.sendEmptyMessage(1);
                displayToast(R.string.msg_err_gps_sign);
            } else if (status == LocationProvider.OUT_OF_SERVICE) {
                handler.sendEmptyMessage(2);
                displayToast(R.string.msg_err_gps_sign);
            } else if (status == LocationProvider.TEMPORARILY_UNAVAILABLE) {
                handler.sendEmptyMessage(2);
                displayToast(R.string.msg_ok_gps_sign);
            }
        }
    };

    private InetConnReceiver inetConnReceiver;

    private Runnable backgroundUpdateOverlay = new Runnable() {

        public void run() {
            doUpdateRouteOverlay();
        }
    };

    /**
	 * Updates the overlay list erasing the last one and adding the
	 * new route overlay. If the new route is null, it does not display
	 * anything. 
	 */
    private synchronized void doUpdateRouteOverlay() {
        MapView myMapView = (MapView) findViewById(R.id.city_map_view);
        rideManager.getCurrentRoute();
        List<Overlay> overlays = myMapView.getOverlays();
        overlays.remove(overlays.size() - 1);
        if (currentRoute != null) routeOverlay = new RouteOverlay(rideManager.getCurrentRoute()); else routeOverlay = new RouteOverlay();
        overlays.add(routeOverlay);
    }

    /**
	 * Creates a thread in order to update the overlay list.
	 */
    private void updateRouteOverlay() {
        Thread updateThread = new Thread(null, backgroundUpdateOverlay, "update_overlay");
        updateThread.start();
    }

    /**
	 * Called when the activity is first created.
	 * @param  savedInstanceState	If the activity is being re-initialized 
	 * 								after previously being shut down then this
	 * 								Bundle contains the data it most recently
	 * 								supplied in onSaveInstanceState(Bundle).
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        nextPlacemark = -1;
        currentRoute = null;
        doCalculations = true;
        rideManager = new RideManager(this.getApplicationContext());
        MapView myMapView = (MapView) findViewById(R.id.city_map_view);
        mapController = myMapView.getController();
        myMapView.displayZoomControls(true);
        mapController.setZoom(17);
        List<Overlay> overlays = myMapView.getOverlays();
        MyLocationOverlay myLocationOverlay = new MyLocationOverlay(this, myMapView);
        overlays.add(myLocationOverlay);
        myLocationOverlay.enableCompass();
        myLocationOverlay.enableMyLocation();
        routeOverlay = new RouteOverlay();
        overlays.add(routeOverlay);
        LocationManager locationManager;
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        String provider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(provider);
        updateWithNewLocation(location);
        locationManager.requestLocationUpdates(provider, 1000, 10, locationListener);
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        isConnectedToInet = connectivityManager.getNetworkInfo(0).isConnected() || connectivityManager.getNetworkInfo(1).isConnected();
        updateInternetConnection();
    }

    /**
	 * Called when the activity will start interacting with the user.
	 */
    @Override
    public void onResume() {
        IntentFilter filter;
        filter = new IntentFilter(InetConnService.INET_CONN_CHG);
        inetConnReceiver = new InetConnReceiver();
        registerReceiver(inetConnReceiver, filter);
        if (rideManager.isCurrentRideNotNull() && (currentPosition != null)) {
            obtainDirections();
        }
        super.onResume();
    }

    /**
	 * Calculates the driving directions.
	 */
    private void obtainDirections() {
        rideManager.startCurrentRide();
    }

    /**
	 * Called when the system is about to start resuming a previous activity.
	 */
    @Override
    public void onPause() {
        unregisterReceiver(inetConnReceiver);
        super.onPause();
    }

    /**
	 * The final call you receive before your activity is destroyed.
	 */
    @Override
    public void onDestroy() {
        rideManager.closeDB();
        super.onDestroy();
    }

    /**
	 * Synchronized method (threading) that updates everything in the UI and
	 * manages the rideManager.
	 * 
	 * @param location	The new location	
	 */
    private synchronized void updateWithNewLocation(Location location) {
        if (isConnectedToInet && doCalculations) {
            if (location != null) {
                currentPosition = new GeoPoint((int) (location.getLatitude() * 1E6), (int) (location.getLongitude() * 1E6));
                mapController.animateTo(currentPosition);
                rideManager.setCurrentPosition(currentPosition);
                Geocoder gc = new Geocoder(this, Locale.US);
                String addressString = "No address found";
                TextView myLocationText = (TextView) findViewById(R.id.txt_currentAddress);
                try {
                    List<Address> addresses = gc.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    StringBuilder sb = new StringBuilder();
                    if (addresses.size() > 0) {
                        sb.append(addresses.get(0).getAddressLine(0));
                        addressString = sb.toString();
                    }
                } catch (IOException e) {
                }
                myLocationText.setText(addressString);
                if (currentRoute != rideManager.getCurrentRoute()) {
                    currentRoute = rideManager.getCurrentRoute();
                    updateRouteOverlay();
                    if (currentRoute != null) {
                        updateRouteOverlay();
                        TransparentPanel tp = (TransparentPanel) findViewById(R.id.transparent_panel_bottom_left);
                        tp.setVisibility(View.VISIBLE);
                    }
                }
                if (currentRoute != null) {
                    if (nextPlacemark != rideManager.getGoingToPlacemarkIndex()) {
                        nextPlacemark = rideManager.getGoingToPlacemarkIndex();
                        updateTrafficSign();
                    }
                    if ((rideManager.getCurrentRideStatus() != 'p') && rideManager.isOutOfRoute()) {
                        rideManager.pauseCurrentRide();
                        obtainDirections();
                    }
                    updateDistanceNextPlacemark();
                } else if (rideManager.nextRide()) {
                    TransparentPanel tp = (TransparentPanel) findViewById(R.id.transparent_panel_bottom_left);
                    rideManager.startCurrentRide();
                    tp.setVisibility(View.VISIBLE);
                } else {
                    TransparentPanel tp = (TransparentPanel) findViewById(R.id.transparent_panel_bottom_left);
                    tp.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    /**
	 * Starts the InetConnService Service.
	 */
    private void updateInternetConnection() {
        startService(new Intent(this, InetConnService.class));
    }

    /**
	 * Updates the traffic sign of the UI.
	 */
    private void updateTrafficSign() {
        ImageView imgArrow = (ImageView) findViewById(R.id.img_arrow);
        String name = currentRoute.getPlacemarks().get(rideManager.getGoingToPlacemarkIndex()).getName();
        String nameSubStr = name.substring(0, 4);
        if (nameSubStr.equalsIgnoreCase("head")) {
            imgArrow.setImageResource(R.drawable.img_continue);
        } else if (nameSubStr.equalsIgnoreCase("cont")) {
            imgArrow.setImageResource(R.drawable.img_continue);
            imgArrow.setImageDrawable(getResources().getDrawable(R.drawable.img_continue));
        } else if (nameSubStr.equalsIgnoreCase("turn")) {
            nameSubStr = name.substring(5, 6);
            if (nameSubStr.equalsIgnoreCase("r")) imgArrow.setImageResource(R.drawable.img_right); else imgArrow.setImageResource(R.drawable.img_left);
        } else if (nameSubStr.equalsIgnoreCase("arri")) {
            imgArrow.setImageResource(R.drawable.img_arrive);
        } else if (nameSubStr.equalsIgnoreCase("at t")) {
            nameSubStr = name.substring(24, 25);
            if (nameSubStr.equalsIgnoreCase("1")) imgArrow.setImageResource(R.drawable.img_roundabout_1); else if (nameSubStr.equalsIgnoreCase("2")) imgArrow.setImageResource(R.drawable.img_roundabout_2); else if (nameSubStr.equalsIgnoreCase("3")) imgArrow.setImageResource(R.drawable.img_roundabout_3); else if (nameSubStr.equalsIgnoreCase("4")) imgArrow.setImageResource(R.drawable.img_roundabout_4); else if (nameSubStr.equalsIgnoreCase("5")) imgArrow.setImageResource(R.drawable.img_roundabout_5); else imgArrow.setImageResource(R.drawable.img_roundabout_6);
        } else if (nameSubStr.equalsIgnoreCase("take")) {
            imgArrow.setImageResource(R.drawable.img_take);
        } else if (nameSubStr.equalsIgnoreCase("slig")) {
            nameSubStr = name.substring(7, 8);
            if (nameSubStr.equalsIgnoreCase("r")) imgArrow.setImageResource(R.drawable.img_slight_right); else imgArrow.setImageResource(R.drawable.img_slight_left);
        } else if (nameSubStr.equalsIgnoreCase("make")) {
            imgArrow.setImageResource(R.drawable.img_uturn);
        } else {
            imgArrow.setImageResource(R.drawable.img_stop);
        }
    }

    /**
	 * Update the distance until the next placemark in the UI.
	 */
    private void updateDistanceNextPlacemark() {
        TextView distNextPlacemarkText = (TextView) findViewById(R.id.txt_distanceArrow);
        int distNextPlacemark = rideManager.getDistanceNextPlacemark();
        String txt;
        if (distNextPlacemark >= 300 && distNextPlacemark < 1000) {
            txt = "0." + distNextPlacemark / 100 + " Km";
        } else if (distNextPlacemark > 1000) {
            txt = distNextPlacemark / 1000 + " Km";
        } else {
            txt = distNextPlacemark + " m";
        }
        distNextPlacemarkText.setText(txt);
    }

    /**
	 * Indicates if a route is displayed.
	 * 
	 * @return If the route is displayed.
	 */
    @Override
    protected boolean isRouteDisplayed() {
        return true;
    }

    /**
	 * Called when a key was pressed down.
	 * 
	 * @param keyCode	The value in event.getKeyCode().
	 * @param event		Description of the key event.
	 * @return	its super result.
	 */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    /**
	 * Initialize the contents of the Activity's standard options menu.
	 * 
	 * @param	menu	The options menu in which you place your items.
	 * @return			true for the menu displayed.
	 */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuItem itemAdd = menu.add(0, MENU_ADD_NEW_RIDE, Menu.NONE, R.string.menu_add_ride);
        MenuItem itemRem = menu.add(0, MENU_REM_CUR_RIDE, Menu.NONE, R.string.menu_rem_ride);
        MenuItem itemList = menu.add(0, MENU_LIST, Menu.NONE, R.string.menu_list);
        MenuItem itemExit = menu.add(0, MENU_EXIT, Menu.NONE, R.string.menu_exit);
        itemAdd.setIcon(R.drawable.icn_add);
        itemRem.setIcon(R.drawable.icn_rem);
        itemList.setIcon(R.drawable.icn_list);
        itemExit.setIcon(R.drawable.icn_poweroff);
        itemAdd.setShortcut('0', 'a');
        itemRem.setShortcut('1', 'r');
        itemList.setShortcut('2', 'l');
        itemExit.setShortcut('3', 'q');
        return true;
    }

    /**
	 * Prepare the Screen's standard options menu to be displayed.
	 * 
	 * @param menu	The options menu as last shown or first initialized
	 * 				by onCreateOptionsMenu().
	 * @return		true for the menu displayed.
	 */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item;
        item = menu.findItem(MENU_ADD_NEW_RIDE);
        item.setEnabled(isConnectedToInet);
        item = menu.findItem(MENU_REM_CUR_RIDE);
        item.setEnabled(isConnectedToInet && rideManager.isCurrentRideNotNull());
        item = menu.findItem(MENU_LIST);
        item.setEnabled(isConnectedToInet);
        return true;
    }

    /**
	 * This hook is called whenever an item in your options menu is selected.
	 * 
	 * @param	item	The menu item that was selected.
	 * @return	false to allow normal menu processing to proceed
	 */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case (MENU_ADD_NEW_RIDE):
                if (isConnectedToInet) {
                    handler.sendEmptyMessage(2);
                    Intent addIntent = new Intent(MainActivity.this, AddRouteActivity.class);
                    startActivityForResult(addIntent, OPEN_ADD_ROUTE_IN);
                } else {
                    displayToast(R.string.msg_err_inet_conn);
                }
                break;
            case (MENU_REM_CUR_RIDE):
                if (isConnectedToInet) {
                    rideManager.abortCurrentRide();
                    displayToast(R.string.msg_ok_ride_erased);
                    TransparentPanel tp = (TransparentPanel) findViewById(R.id.transparent_panel_bottom_left);
                    if (!rideManager.nextRide()) tp.setVisibility(View.INVISIBLE);
                } else {
                    displayToast(R.string.msg_err_inet_conn);
                }
                break;
            case (MENU_LIST):
                if (isConnectedToInet) {
                    handler.sendEmptyMessage(2);
                    Intent addIntent = new Intent(MainActivity.this, RideListActivity.class);
                    startActivityForResult(addIntent, OPEN_LIST_RIDES);
                } else {
                    displayToast(R.string.msg_err_inet_conn);
                }
                break;
            case (MENU_EXIT):
                finish();
        }
        return false;
    }

    /**
	 * Called when an activity you launched exits.
	 * 
	 * @param requestCode	the code supplied to startActivityForResult().
	 * @param resultCode	the result code returned by the child activity.
	 * @param data			an Intent, which can return result data to the 
	 * 						caller.
	 */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (OPEN_ADD_ROUTE_IN):
                {
                    handler.sendEmptyMessage(1);
                    if (resultCode == Activity.RESULT_OK) {
                        try {
                            if (!rideManager.addRide(null, new Date(), data.getStringExtra("ADDRESS_OK"), new Date())) {
                                Builder alertDialog = new AlertDialog.Builder(this);
                                alertDialog.setMessage(R.string.msg_war_addr_nf);
                                alertDialog.setTitle(R.string.msg_warning);
                                alertDialog.setPositiveButton(R.string.ok, null);
                                alertDialog.setCancelable(true);
                                alertDialog.create().show();
                                break;
                            }
                        } catch (IOException e) {
                            Builder alertDialog = new AlertDialog.Builder(this);
                            alertDialog.setMessage(R.string.msg_err_inet_conn);
                            alertDialog.setTitle(R.string.msg_warning);
                            alertDialog.setPositiveButton(R.string.ok, null);
                            alertDialog.setCancelable(true);
                            alertDialog.create().show();
                            break;
                        }
                        rideManager.nextRide();
                        if (rideManager.getDistanceUntilEndingLocation() > 100000) {
                            Builder alertDialog = new AlertDialog.Builder(this);
                            alertDialog.setMessage(R.string.msg_war_addr_far);
                            alertDialog.setTitle(R.string.msg_warning);
                            alertDialog.setPositiveButton(R.string.ok, null);
                            alertDialog.setCancelable(true);
                            alertDialog.create().show();
                            break;
                        }
                        if (isConnectedToInet) {
                            if (rideManager.isCurrentRideNotNull() && (currentPosition != null)) {
                                obtainDirections();
                            } else {
                                displayToast(R.string.msg_ok_ride_added);
                            }
                        } else {
                            displayToast(R.string.msg_err_inet_conn);
                        }
                    }
                    break;
                }
            case (OPEN_LIST_RIDES):
                {
                    handler.sendEmptyMessage(1);
                    if (!rideManager.isCurrentRideNotNull()) this.currentRoute = null;
                    break;
                }
        }
    }

    /**
	 * Displays a Toast with the text supplied.
	 * 
	 * @param text	The text to be displayed by the Toast.
	 */
    private void displayToast(int text) {
        Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
	 * A receiver to that processes the InetConnService.
	 * 
	 * @author		Albert Hernández López
	 * @version		1.0
	 * @since       1.0
	 */
    public class InetConnReceiver extends BroadcastReceiver {

        /**
	 * Called when the BroadcastReceiver is receiving an Intent broadcast.
	 * 
	 * @param	context	The Context in which the receiver is running.
	 * @param	The Intent being received.
	 */
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equalsIgnoreCase(InetConnService.INET_CONN_CHG)) {
                if (isConnectedToInet != intent.getBooleanExtra("inetconn", isConnectedToInet)) {
                    isConnectedToInet = intent.getBooleanExtra("inetconn", isConnectedToInet);
                    if (isConnectedToInet) displayToast(R.string.msg_ok_inet_conn); else displayToast(R.string.msg_err_inet_conn);
                }
                String address = intent.getStringExtra("address");
                if (address.equalsIgnoreCase("") == false) try {
                    rideManager.addRide(null, new Date(), address, new Date());
                    displayToast(R.string.msg_ok_ride_added);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
