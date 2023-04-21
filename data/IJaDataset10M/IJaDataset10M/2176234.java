package com.myzuku.getmylocation.pro;

import java.util.ArrayList;
import android.graphics.drawable.Drawable;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class MapViewOverlay extends ItemizedOverlay<OverlayItem> {

    private ArrayList<OverlayItem> overlayItems = new ArrayList<OverlayItem>();

    public MapViewOverlay(Drawable drawable) {
        super(boundCenterBottom(drawable));
    }

    public int addOverlayItem(OverlayItem overlay) {
        overlayItems.add(overlay);
        populate();
        return overlayItems.size() - 1;
    }

    public void removeOverlayItem(int i) {
        overlayItems.remove(i);
        populate();
    }

    public void changeOverlayItem(int i, OverlayItem item) {
        overlayItems.set(i, item);
        populate();
    }

    @Override
    protected OverlayItem createItem(int i) {
        return overlayItems.get(i);
    }

    @Override
    public int size() {
        return overlayItems.size();
    }
}
