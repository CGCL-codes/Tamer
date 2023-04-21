package com.luzan.common.geomap;

/**
 * LatLngPoint
 *
 * @author Alexander Bondar
 */
public class LatLngPoint {

    private double lat;

    private double lng;

    public LatLngPoint(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
