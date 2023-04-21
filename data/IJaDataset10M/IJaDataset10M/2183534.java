package org.androidsoft.poi.model;

import android.graphics.drawable.Drawable;

/**
 * POI Point of interest Interface
 * @author pierre
 */
public interface POI {

    int getId();

    String getTitle();

    String getDescription();

    double getLatitude();

    double getLongitude();

    long getDistance();

    void setDistance(long distance);

    public Drawable getIcon();
}
