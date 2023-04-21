package pe.edu.upc.dsd.epica.movil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import pe.edu.upc.dsd.epica.R;
import pe.edu.upc.dsd.epica.model.Establecimiento;
import pe.edu.upc.dsd.epica.model.ListaEstablecimientos;
import pe.edu.upc.dsd.epica.util.Constants;
import pe.edu.upc.dsd.epica.util.CustomItemizedOverlay;
import pe.edu.upc.dsd.epica.util.GPSEvent;
import pe.edu.upc.dsd.epica.util.GPSEventListener;
import pe.edu.upc.dsd.epica.util.RequestMethod;
import pe.edu.upc.dsd.epica.util.RestClient;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.MapView.LayoutParams;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class mapa extends MapActivity {

    private MapView mapView;

    LocationManager locationManager = null;

    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 10;

    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 5000;

    final String TAG = getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapa);
        mapView = (MapView) findViewById(R.id.map_view_prom);
        mapView.setBuiltInZoomControls(true);
        LinearLayout zoomLayout = (LinearLayout) findViewById(R.id.zoom_prom);
        View zoomView = mapView.getZoomControls();
        zoomLayout.addView(zoomView, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        mapView.displayZoomControls(true);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            ShowEstablecimientosLocation(location.getLatitude(), location.getLongitude());
        }
        GpsLocationListener locationListener = new GpsLocationListener();
        locationListener.addEventListener(new GPSEventListener() {

            public void GPSEventOccurred(GPSEvent evt) {
                ShowEstablecimientosLocation(evt.getLatitudeE6(), evt.getLongitudeE6());
            }
        });
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATES, MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, locationListener);
    }

    private void ShowEstablecimientosLocation(double latitude, double longitude) {
        RestClient client = new RestClient(Constants.UrlRest + "establecimiento/buscarLatLong/" + Double.toString(longitude) + "/" + Double.toString(latitude));
        try {
            client.Execute(RequestMethod.GET);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        String response = client.getResponse();
        Gson gson = new Gson();
        ListaEstablecimientos responseObject = null;
        try {
            responseObject = gson.fromJson(response, ListaEstablecimientos.class);
        } catch (JsonParseException e) {
            Log.e(TAG, e.getMessage());
        }
        for (Establecimiento establecimiento : responseObject.getListaEstablecimientos()) {
            ShowLocationMap(establecimiento, "Ubicacion Establecimiento");
        }
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

    private void ShowLocationMap(Establecimiento establecimiento, String title) {
        GeoPoint point = new GeoPoint((int) (establecimiento.getLatitud() * 1E6), (int) (establecimiento.getLongitud() * 1E6));
        OverlayItem overlayitem = new OverlayItem(point, title, "");
        Drawable drawable = this.getResources().getDrawable(R.drawable.pushpin);
        CustomItemizedOverlay itemizedOverlay = new CustomItemizedOverlay(drawable, this, establecimiento);
        itemizedOverlay.addOverlay(overlayitem);
        List<Overlay> mapOverlays = null;
        mapOverlays = mapView.getOverlays();
        mapOverlays.add(itemizedOverlay);
        MapController mapController = mapView.getController();
        mapController.animateTo(point);
        mapController.setZoom(27);
        mapView.invalidate();
    }

    private class GpsLocationListener implements LocationListener {

        private List<GPSEventListener> listenerList = new ArrayList<GPSEventListener>();

        public synchronized void addEventListener(GPSEventListener listener) {
            listenerList.add(listener);
        }

        public synchronized void removeEventListener(GPSEventListener listener) {
            listenerList.remove(listener);
        }

        public void onStatusChanged(String s, int i, Bundle b) {
            Toast.makeText(mapa.this, "El proveedor de estado ha cambiado.", Toast.LENGTH_LONG).show();
        }

        public void onProviderDisabled(String s) {
            Toast.makeText(mapa.this, "El Proveedor ha sido desabilitado por el usuario. GPS turned off", Toast.LENGTH_LONG).show();
        }

        public void onProviderEnabled(String s) {
            Toast.makeText(mapa.this, "El Proveedor ha sido habilitado por el usuario. GPS turned on", Toast.LENGTH_LONG).show();
        }

        public void onLocationChanged(Location location) {
            GPSEvent event = new GPSEvent(this, location.getLatitude(), location.getLongitude());
            Iterator<GPSEventListener> i = listenerList.iterator();
            while (i.hasNext()) {
                i.next().GPSEventOccurred(event);
            }
            listenerList.removeAll(listenerList);
        }
    }
}
