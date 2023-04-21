package data.coordinate;

import org.jdesktop.swingx.mapviewer.GeoPosition;

public class LatLon {

    public static final double MAX_LAT = 85.05112877980659;

    public static final double MAX_LON = 180;

    private double latitude;

    private double longitude;

    public LatLon(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public GeoPosition getGeoPosition() {
        return new GeoPosition(latitude, longitude);
    }
}
