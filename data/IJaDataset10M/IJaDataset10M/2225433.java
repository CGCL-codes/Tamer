package org.androidsoft.poi.model;

import android.graphics.drawable.Drawable;

/**
 * Abstract POI
 * @author pierre
 */
public abstract class AbstractPOI implements POI {

    private int mId;

    private double mLat;

    private double mLon;

    private long mDistance;

    /**
     * @return the id
     */
    @Override
    public int getId() {
        return mId;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        mId = id;
    }

    /**
     * @return the _lat
     */
    @Override
    public double getLatitude() {
        return mLat;
    }

    /**
     * @param lat the _lat to set
     */
    public void setLatitude(double lat) {
        mLat = lat;
    }

    /**
     * @return the _lon
     */
    @Override
    public double getLongitude() {
        return mLon;
    }

    /**
     * @param lon the _lon to set
     */
    public void setLongitude(double lon) {
        mLon = lon;
    }

    @Override
    public Drawable getIcon() {
        return null;
    }

    @Override
    public long getDistance() {
        return mDistance;
    }

    @Override
    public void setDistance(long distance) {
        mDistance = distance;
    }

    protected final Integer getInt(String src) {
        try {
            return Integer.parseInt(src);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    protected final Double getDouble(String src) {
        try {
            return Double.parseDouble(src);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
