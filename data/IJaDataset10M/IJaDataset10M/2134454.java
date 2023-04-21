package virtualpostit.com;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import virtualpostit.com.R;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.*;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.google.android.maps.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Waypoint;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

/**
 * Attività che gestirà l'interazione dell'utente con la mappa. In paricolare gestirà
 * la visualizzazione degli overlay e il click su di essi; presenterà a video
 * il menù col quale si navigherà tra le varie funzionalità messe a disposizione
 * dall'applicazione.
 * 
 * @author Gruppo Capo, De Notaris, Pastore e Vento
 * @since Luglio 2011
 */
public class GenericMapActivity extends MapActivity implements Observer {

    private class MyItemizedOverlay extends ItemizedOverlay<OverlayItem> {

        private String cathegory;

        private ArrayList<Integer> idWaypoint = new ArrayList<Integer>();

        private Context mContext;

        private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();

        private double chosenRadius;

        /**
		 * Costruttore che permette di aggiungere un riferimento al contesto corrente. Ciò rende 
		 * supportabile l'interazione con l'utente. Inoltre imposta il marcatore a quello di default.
		 * @param defaultMarker marcatore di default
		 * @param context contesto nel quale bisogna gestire l'interazione con l'utente
		 * @param cathegory categoria dell'overlay
		 */
        public MyItemizedOverlay(Drawable defaultMarker, Context context, String cathegory) {
            super(boundCenterBottom(defaultMarker));
            mContext = context;
            this.cathegory = cathegory;
            chosenRadius = -1;
        }

        /**
		 * Costruttore di classe che definirà il marcatore di default di ogni punto 
		 * saliente della mappa. Il marcatore sarà centrato nelle coordinate del punto
		 * saliente rispetto al lato in basso dell'immagine.
		 * @param defaultMarker marcatore di default
		 * @param cathegory categoria dell'overlay
		 */
        public MyItemizedOverlay(Drawable defaultMarker, String cathegory) {
            super(boundCenterBottom(defaultMarker));
            this.cathegory = cathegory;
            chosenRadius = -1;
        }

        /**
		 * Metodo che imposta il raggio della circonferenza da visualizzare su mappa
		 * al raggio passato in input.
		 * @param radius double raggio della circonferenza
		 */
        public void setRadius(double radius) {
            chosenRadius = radius;
        }

        @Override
        protected OverlayItem createItem(int index) {
            return mOverlays.get(index);
        }

        @Override
        protected boolean onTap(int index) {
            Intent myIntent = new Intent(mContext, PostItListViewActivity.class);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            myIntent.putExtra(getPackageName() + ".cathegory", cathegory);
            myIntent.putExtra(getPackageName() + ".id", idWaypoint.get(index));
            float results[] = new float[3];
            Location.distanceBetween(Double.parseDouble(tvLatitudine.getText().toString()), Double.parseDouble(tvLongitudine.getText().toString()), (mOverlays.get(index).getPoint().getLatitudeE6() / 1E6), (mOverlays.get(index).getPoint().getLongitudeE6() / 1E6), results);
            DecimalFormat df = new DecimalFormat("#.##");
            myIntent.putExtra(getPackageName() + ".distance", String.valueOf(df.format(results[0] / 1E3)));
            Log.i("distance passata da onTap", String.valueOf(results[0] / 1E3));
            Log.i("index nell'ontap", String.valueOf(index));
            Log.i("Id nell'ontap", String.valueOf(idWaypoint.get(index)));
            mContext.startActivity(myIntent);
            return true;
        }

        /**
		 * Metodo che aggiunge l'id del punto d'interessa alla lista di id.
		 * @param id identificativo da aggiungere
		 */
        public void addId(Integer id) {
            idWaypoint.add(id);
        }

        /**
		 * Metodo che aggiunge un nuovo elemento overlay alla lista.
		 * @param overlay overlay da aggiungere alla lista
		 */
        public void addOverlay(OverlayItem overlay) {
            mOverlays.add(overlay);
            populate();
        }

        @Override
        public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) {
            super.draw(canvas, mapView, shadow);
            Paint paint = new Paint();
            Projection projection = mapView.getProjection();
            Point point = new Point();
            double lat = locationManager.getLastKnownLocation("gps").getLatitude() * 1E6;
            double longit = locationManager.getLastKnownLocation("gps").getLongitude() * 1E6;
            GeoPoint myLocPoint = new GeoPoint((int) lat, (int) longit);
            projection.toPixels(myLocPoint, point);
            float radius;
            if (chosenRadius == -1) radius = -1; else radius = (float) (projection.metersToEquatorPixels((float) (chosenRadius * 1E3)) * (1 / Math.cos(Math.toRadians(lat / 1E6))));
            paint.setStrokeWidth(2);
            paint.setARGB(255, 255, 255, 255);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.RED);
            paint.setStyle(Style.STROKE);
            canvas.drawCircle(point.x, point.y, radius, paint);
            return true;
        }

        @Override
        public int size() {
            return mOverlays.size();
        }
    }

    private static final int MAX_LEVEL_ZOOM = 21;

    private static final int DEFAULT_ZOOM = 13;

    private List<Waypoint> listWaypoint;

    private static LocationManager locationManager;

    private MapController mapController;

    private MapView mapView;

    private MyLocationOverlay myLocationOverlay;

    private TextView tvLatitudine;

    private TextView tvLongitudine;

    protected boolean clickedPush = false;

    protected static final double SHORT_RADIUS = 10;

    protected static final double MEDIUM_RADIUS = 30;

    protected static final double LONG_RADIUS = 50;

    protected static final double VERYLONG_RADIUS = 100;

    LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            mapView.invalidate();
            Double geoLat = location.getLatitude() * 1E6;
            Double geoLng = location.getLongitude() * 1E6;
            GeoPoint point = new GeoPoint(geoLat.intValue(), geoLng.intValue());
            mapController.animateTo(point);
            tvLatitudine.setText(Double.toString(getRound(location.getLatitude(), 5)));
            tvLongitudine.setText(Double.toString(getRound(location.getLongitude(), 5)));
        }

        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(GenericMapActivity.this, "onProviderDisabled " + provider, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(GenericMapActivity.this, "onProviderEnabled " + provider, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    private boolean checkStatusGPS() {
        String lat = tvLatitudine.getText().toString();
        String longit = tvLongitudine.getText().toString();
        if (lat.equals("---") || longit.equals("---")) {
            new MyDialog(this).showDialog("Attenzione", "Il GPS è disabilitato, prima di procedere è necessario abilitarlo attraverso il menu Impostazioni del telefono", R.drawable.warning);
            return false;
        }
        return true;
    }

    private void display(ArrayList<GeoPoint> gpl, String cathegory, double ray) {
        int markerId;
        OverlayItem overlayitem = null;
        List<Overlay> overlays = mapView.getOverlays();
        overlays.clear();
        mapView.invalidate();
        overlays.add(myLocationOverlay);
        myLocationOverlay.enableMyLocation();
        markerId = getMarkerByCathegory(cathegory);
        Drawable drawable = this.getResources().getDrawable(markerId);
        MyItemizedOverlay itemizedoverlay = new MyItemizedOverlay(drawable, this, cathegory);
        itemizedoverlay.setRadius(ray);
        for (int i = 0; i < gpl.size(); i++) {
            overlayitem = new OverlayItem(gpl.get(i), "", "");
            itemizedoverlay.addOverlay(overlayitem);
            itemizedoverlay.addId(searchIdWaypointByGeopoint(gpl.get(i)));
        }
        overlays.add(itemizedoverlay);
    }

    private void displayByCathegory(String cathegory, double ray) {
        MyDialog md = new MyDialog(this);
        String response = "";
        WebService search = new WebService(LoginActivity.SERVER_HOST + ":" + LoginActivity.SERVER_PORT);
        if (!cathegory.equals("Tutte")) {
            ArrayList<NameValuePair> params = new ArrayList<org.apache.http.NameValuePair>();
            params.add(new BasicNameValuePair("cathegory", cathegory));
            params.add(new BasicNameValuePair("myLatitude", String.valueOf(getRound(locationManager.getLastKnownLocation("gps").getLatitude(), 5))));
            params.add(new BasicNameValuePair("myLongitude", String.valueOf(getRound(locationManager.getLastKnownLocation("gps").getLongitude(), 5))));
            params.add(new BasicNameValuePair("radius", String.valueOf(ray)));
            Log.i("Parametro della ricerca:", cathegory);
            response = search.webPost("/searchByCathegory.php", params);
        } else {
            ArrayList<NameValuePair> params = new ArrayList<org.apache.http.NameValuePair>();
            params.add(new BasicNameValuePair("cathegory", ""));
            params.add(new BasicNameValuePair("myLatitude", String.valueOf(getRound(locationManager.getLastKnownLocation("gps").getLatitude(), 5))));
            params.add(new BasicNameValuePair("myLongitude", String.valueOf(getRound(locationManager.getLastKnownLocation("gps").getLongitude(), 5))));
            params.add(new BasicNameValuePair("radius", String.valueOf(ray)));
            response = search.webPost("/search.php", params);
        }
        try {
            Log.i("response", response);
            String r = response.replace("\n", "").trim();
            if (!r.equals("null")) {
                ArrayList<GeoPoint> gpl = new ArrayList<GeoPoint>();
                Type collectionType = new TypeToken<ArrayList<Waypoint>>() {
                }.getType();
                listWaypoint = new Gson().fromJson(response, collectionType);
                for (Waypoint l : listWaypoint) {
                    Double geoLat = l.getLat() * 1E6;
                    Double geoLong = l.getLongit() * 1E6;
                    GeoPoint g = new GeoPoint(geoLat.intValue(), geoLong.intValue());
                    gpl.add(g);
                }
                Toast.makeText(GenericMapActivity.this, "Trovati " + gpl.size() + " punti di interesse ", Toast.LENGTH_SHORT).show();
                display(gpl, cathegory, ray);
                mapController.setZoom(DEFAULT_ZOOM - (int) (ray / MAX_LEVEL_ZOOM + 1));
                GeoPoint myPos = new GeoPoint((int) (locationManager.getLastKnownLocation("gps").getLatitude() * 1E6), (int) (locationManager.getLastKnownLocation("gps").getLongitude() * 1E6));
                mapController.setCenter(myPos);
            } else {
                md.showDialog("Attenzione", "Nessuna corrispondenza trovata.", R.drawable.warning);
            }
        } catch (Exception e) {
            Log.i("Errore", e.getMessage());
            md.showDialog("Attenzione", "Errore del server, riprovare più tardi", R.drawable.error);
        }
    }

    private void displayLastInsertedWaypoint(GeoPoint g, String cathegory, int idWaypoint) {
        int markerId;
        OverlayItem overlayitem = null;
        List<Overlay> overlays = mapView.getOverlays();
        overlays.clear();
        mapView.invalidate();
        overlays.add(myLocationOverlay);
        myLocationOverlay.enableMyLocation();
        markerId = getMarkerByCathegory(cathegory);
        Drawable drawable = this.getResources().getDrawable(markerId);
        MyItemizedOverlay itemizedoverlay = new MyItemizedOverlay(drawable, this, "Tutte");
        overlayitem = new OverlayItem(g, "", "");
        itemizedoverlay.addOverlay(overlayitem);
        itemizedoverlay.addId(idWaypoint);
        overlays.add(itemizedoverlay);
    }

    private int getMarkerByCathegory(String cathegory) {
        int marketId;
        Resources res = getResources();
        String[] cathegoryList = new String[4];
        cathegoryList = res.getStringArray(R.array.cathegoryList);
        if (cathegory.equals(cathegoryList[0])) marketId = R.drawable.cutlery; else if (cathegory.equals(cathegoryList[1])) marketId = R.drawable.hotel; else if (cathegory.equals(cathegoryList[2])) marketId = R.drawable.store; else if (cathegory.equals(cathegoryList[3])) marketId = R.drawable.fuel; else marketId = R.drawable.pushpin;
        return marketId;
    }

    private void newPostIt() {
        Intent intent = new Intent(getBaseContext(), NewPostitActivity.class);
        Log.i("latitude", Double.toString(getRound(locationManager.getLastKnownLocation("gps").getLatitude(), 5)));
        Log.i("longitude", Double.toString(getRound(locationManager.getLastKnownLocation("gps").getLongitude(), 5)));
        intent.putExtra(getPackageName() + ".latitude", getRound(locationManager.getLastKnownLocation("gps").getLatitude(), 5));
        intent.putExtra(getPackageName() + ".longitude", getRound(locationManager.getLastKnownLocation("gps").getLongitude(), 5));
        startActivityForResult(intent, 1);
    }

    private int searchIdWaypointByGeopoint(GeoPoint g) {
        int id = -1;
        boolean founded = false;
        for (int i = 0; i < listWaypoint.size() && !founded; i++) if (listWaypoint.get(i).getLat() == (g.getLatitudeE6() / 1E6) && listWaypoint.get(i).getLongit() == (g.getLongitudeE6() / 1E6)) {
            founded = true;
            id = listWaypoint.get(i).getId();
        }
        return id;
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

    protected void onActivityResult(int resultCode, int requestCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1 && requestCode == 1) {
            Toast.makeText(this, "Post it " + data.getStringExtra(getPackageName() + ".postItName") + " inserito correttamente ", Toast.LENGTH_LONG).show();
            String insertedLat = data.getStringExtra(getPackageName() + ".insertedLat");
            String insertedLongit = data.getStringExtra(getPackageName() + ".insertedLongit");
            int idWaypoint = Integer.parseInt(data.getStringExtra(getPackageName() + ".idWaypoint"));
            GeoPoint g = new GeoPoint((int) (Double.parseDouble(insertedLat) * 1E6), (int) (Double.parseDouble(insertedLongit) * 1E6));
            displayLastInsertedWaypoint(g, data.getStringExtra(getPackageName() + ".cathegory"), idWaypoint);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        myLocationOverlay.enableMyLocation();
    }

    /**
	 * Approssima un numero decimale con una specificata precisione, intese come cifre
	 * dopo la virgola.
	 * @param x numero da approssimare
	 * @param digits cifre dopo la virgola
	 * @return double numero approssimato
	 */
    public static double getRound(double x, int digits) {
        double powerOfTen = Math.pow(10, digits);
        return ((double) Math.round(x * powerOfTen) / powerOfTen);
    }

    /**
	 * Metodo che sposta l'utente sulla mappa riportandolo all'ultimo luogo 
	 * conosciuto dal location provider.
	 */
    public void myCurrentPosition() {
        mapView.invalidate();
        GeoPoint point = new GeoPoint((int) (locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude() * 1E6), (int) (locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude() * 1E6));
        mapController.animateTo(point);
    }

    /** 
	 * Metodo chiamato all'atto della creazione dell'attività 
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        tvLatitudine = (TextView) this.findViewById(R.id.tvLatitudine);
        tvLongitudine = (TextView) this.findViewById(R.id.tvLongitudine);
        mapView = (MapView) findViewById(R.id.mapView);
        mapController = mapView.getController();
        mapView.setClickable(true);
        mapView.setBuiltInZoomControls(true);
        mapController.setZoom(DEFAULT_ZOOM);
        mapView.setStreetView(true);
        myLocationOverlay = new MyLocationOverlay(this, mapView);
        List<Overlay> overlays = mapView.getOverlays();
        overlays.add(myLocationOverlay);
        myLocationOverlay.enableMyLocation();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled("gps")) {
            Toast.makeText(this, "Il GPS è attualmente disabilitato. E' possibile abilitarlo dal menu impostazioni.", Toast.LENGTH_LONG).show();
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_add:
                if (checkStatusGPS()) newPostIt();
                return true;
            case R.id.menu_search:
                if (checkStatusGPS()) {
                    MyDialog md = new MyDialog(this);
                    md.addObserver(this);
                    md.search();
                }
                return true;
            case R.id.menu_mylocation:
                if (checkStatusGPS()) myCurrentPosition();
                return true;
            case R.id.menu_info:
                new MyDialog(this).info();
                return true;
            case R.id.menu_logout:
                new MyDialog(this).exit();
                return true;
            case R.id.menu_notification:
                Intent myIn = new Intent(this, PushNotificationService.class);
                if (checkStatusGPS()) {
                    if (!clickedPush) {
                        item.setTitleCondensed("Disattiva Push");
                        clickedPush = true;
                        startService(myIn);
                    } else {
                        item.setTitleCondensed("Attiva Push");
                        clickedPush = false;
                        stopService(myIn);
                    }
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
            myLocationOverlay.disableMyLocation();
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        MyDialog md = (MyDialog) observable;
        String cathegoryChoice = md.getItemChoice()[0];
        String radiusChoice = md.getItemChoice()[1];
        if (radiusChoice.equals("0")) displayByCathegory(cathegoryChoice, SHORT_RADIUS); else if (radiusChoice.equals("1")) displayByCathegory(cathegoryChoice, MEDIUM_RADIUS); else if (radiusChoice.equals("2")) displayByCathegory(cathegoryChoice, LONG_RADIUS); else if (radiusChoice.equals("3")) displayByCathegory(cathegoryChoice, VERYLONG_RADIUS);
    }

    /**
	 * Metodo che ritorna il Location Manager dell'attività
	 * @return LocationManager location manager dell'attività
	 */
    public static LocationManager getLocationManager() {
        return locationManager;
    }
}
