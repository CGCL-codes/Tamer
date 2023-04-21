package org.androidsoft.poi.service;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import com.jwetherell.augmented_reality.data.DataSource;
import com.jwetherell.augmented_reality.ui.IconMarker;
import com.jwetherell.augmented_reality.ui.Marker;
import java.util.ArrayList;
import java.util.List;
import org.androidsoft.poi.model.POI;

/**
 * Data Source of POIs
 * @author pierre
 */
public class POIDataSource extends DataSource {

    private List<Marker> mCachedMarkers = new ArrayList<Marker>();

    private static Bitmap icon = null;

    private static POIService mServicePOI;

    private static Context mContext;

    public POIDataSource(Context context, POIService service, int iconRes) {
        mContext = context;
        Resources res = context.getResources();
        if (res == null) {
            throw new NullPointerException();
        }
        createIcon(res, iconRes);
        mServicePOI = service;
    }

    protected final void createIcon(Resources res, int iconRes) {
        if (res == null) {
            throw new NullPointerException();
        }
        icon = BitmapFactory.decodeResource(res, iconRes);
    }

    public List<Marker> getMarkers() {
        List<POI> listPOIs = mServicePOI.getPOIs(mContext);
        for (POI poi : listPOIs) {
            Marker atl = new IconMarker(poi.getTitle(), poi.getLatitude(), poi.getLongitude(), 0, Color.DKGRAY, icon);
            mCachedMarkers.add(atl);
        }
        return mCachedMarkers;
    }
}
