package at.the.gogo.parkoid.map;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.OverlayItem;
import android.graphics.drawable.Drawable;

public class ParkingCarItem extends OverlayItem {

    public ParkingCarItem(final String aTitle, final String aDescription, final GeoPoint aGeoPoint, final Drawable marker) {
        super(aTitle, aDescription, aGeoPoint);
        setMarker(marker);
        setMarkerHotspot(HotspotPlace.BOTTOM_CENTER);
    }
}
