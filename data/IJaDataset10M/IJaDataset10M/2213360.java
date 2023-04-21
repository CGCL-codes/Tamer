package edu.uab.project.truckfleetcontrol;

import java.util.ArrayList;
import java.util.List;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Projection;

/**
 * TruckItemizedOverlay class. The purpose of this class is offer the methods for render
 * the GPS navigation.
 *
 */
public class TruckItemizedOverlay extends ItemizedOverlay {

    private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();

    private List<GeoPoint> geoPointList = new ArrayList<GeoPoint>();

    /**
	 * Basic Constructor
	 * @param defaultMarker
	 */
    public TruckItemizedOverlay(Drawable defaultMarker) {
        super(boundCenterBottom(defaultMarker));
    }

    /**
	 * Adds a GeoPoint to the member variable geoPointList
	 * @param p
	 */
    public void addGeoPoint(GeoPoint p) {
        geoPointList.add(p);
    }

    /**
	 * Cleans the geoPoint list
	 */
    public void cleanGeoPoints() {
        geoPointList.clear();
    }

    /**
	 * Returns the member variable geoPointList with all of its points
	 * @return List<GeoPoint>
	 */
    public List<GeoPoint> getAllGeoPoints() {
        return geoPointList;
    }

    /**
	 * Adds an overlay item to the current array of overlays
	 * @param overlay
	 */
    public void addOverlay(OverlayItem overlay) {
        mOverlays.add(overlay);
        populate();
    }

    /**
	 * Deletes all overlays
	 */
    public void deleteOverlays() {
        mOverlays.clear();
    }

    /**
	 * return the overlay item i
	 * @param i
	 */
    protected OverlayItem createItem(int i) {
        return mOverlays.get(i);
    }

    /**
	 * Return the number of items in the overlay
	 */
    public int size() {
        return mOverlays.size();
    }

    /**
	 *  Draws the truck path rendering a line joining the geo points stored in the geoPoint list
	 */
    public void draw(Canvas canvas, MapView mapView, boolean shadow) {
        super.draw(canvas, mapView, shadow);
        Projection projection = mapView.getProjection();
        Paint paint = new Paint();
        paint.setColor(Color.CYAN);
        paint.setStrokeWidth(4.0f);
        if (geoPointList.size() > 1) {
            GeoPoint gpTmp = geoPointList.get(0);
            Point pTmp = projection.toPixels(gpTmp, null);
            for (GeoPoint gp : geoPointList) {
                Point p = projection.toPixels(gp, null);
                canvas.drawLine(pTmp.x, pTmp.y, p.x, p.y, paint);
                pTmp = p;
            }
        }
    }
}
