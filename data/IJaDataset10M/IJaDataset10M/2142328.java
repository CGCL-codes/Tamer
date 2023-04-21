package org.androidsoft.poi.ar;

import android.graphics.Bitmap;
import com.jwetherell.augmented_reality.ui.IconMarker;

/**
 *
 * @author pierre
 */
public class POIMarker extends IconMarker {

    private int mId;

    public POIMarker(String name, double lat, double lon, double alt, int color, Bitmap bitmap, int id) {
        super(name, lat, lon, alt, color, bitmap);
        mId = id;
    }

    public int getId() {
        return mId;
    }
}
