package park.pack;

import java.util.List;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import classes.pack.LogRecord;
import classes.pack.Logging;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class ShowMap extends MapActivity implements LocationListener {

    String provider;

    MapView mapView;

    List<Overlay> mapOverlays;

    MapController mc;

    GeoPoint gCenter = new GeoPoint((int) (52.132633 * 1E6), (int) (5.291266 * 1E6));

    GeoPoint gP;

    Location loc;

    public GeoPoint loc2geo(Location loc) {
        GeoPoint Gp = new GeoPoint((int) (loc.getLatitude() * 1E6), (int) (loc.getLongitude() * 1E6));
        return Gp;
    }

    public void ShowLocalPos(GeoPoint point, String longitude, String latitude) {
        try {
            DrawIcon(this.getResources().getDrawable(R.drawable.man), point.getLatitudeE6(), point.getLongitudeE6(), "Man Positie", "U bevind zich op \n " + latitude + ", " + longitude);
        } catch (Exception e) {
            Toast toast = Toast.makeText(this, "Locatie bepaling werkt niet", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public void ShowCarPos(GeoPoint point, String longitude, String latitude) {
        try {
            DrawIcon(this.getResources().getDrawable(R.drawable.car), point.getLatitudeE6(), point.getLongitudeE6(), "Car Positie", "U bevind zich op \n " + latitude + ", " + longitude);
        } catch (Exception e) {
            Toast toast = Toast.makeText(this, "Locatie bepaling werkt niet", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public void DrawIcon(Drawable dwIcon, int latitude, int longitude, String strTitle, String strLable) {
        MapsOverlay itemizedoverlay = new MapsOverlay(dwIcon, this);
        GeoPoint p = new GeoPoint(latitude, longitude);
        OverlayItem overlayitem = new OverlayItem(p, strTitle, strLable);
        itemizedoverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedoverlay);
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.maps);
        mapView = (MapView) findViewById(R.id.mapview);
        mapOverlays = mapView.getOverlays();
        mapView.setBuiltInZoomControls(true);
        mc = mapView.getController();
        mc.setCenter(gCenter);
        mc.setZoom(8);
        Thread thr1 = new Thread(r1);
        thr1.start();
    }

    Runnable r1 = new Runnable() {

        public void run() {
            try {
                while (true) {
                    mapOverlays.clear();
                    LogRecord record = Logging.getLatestPosition();
                    LogRecord carRecord = Logging.getParkingSpot();
                    ShowLocalPos(record.getLocation(), Integer.toString(record.getLocation().getLatitudeE6()), Integer.toString(record.getLocation().getLongitudeE6()));
                    if (carRecord != null) {
                        ShowCarPos(carRecord.getLocation(), Integer.toString(carRecord.getLocation().getLatitudeE6()), Integer.toString(carRecord.getLocation().getLongitudeE6()));
                        Log.i("Car", carRecord.toString());
                    }
                    Thread.sleep(20000);
                }
            } catch (Exception ex) {
                Toast toast = Toast.makeText(ShowMap.this, "test", Toast.LENGTH_LONG);
                toast.show();
            }
        }
    };

    public void onLocationChanged(Location location) {
    }

    public void onProviderDisabled(String provider) {
    }

    public void onProviderEnabled(String provider) {
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
}
