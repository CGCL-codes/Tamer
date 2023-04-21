package gpsmate.geodata;

/**
 * Point
 * 
 * Represents a geographical location (lat/lon/ele).
 * 
 * @author longdistancewalker
 */
public class Point {

    private double latitude;

    private double longitude;

    private double elevation;

    public Point() {
        latitude = 0.0;
        longitude = 0.0;
        elevation = 0.0;
    }

    public Point(double latitude, double longitude, double elevation) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.elevation = elevation;
    }

    public Point(Point other) {
        latitude = other.latitude;
        longitude = other.longitude;
        elevation = other.elevation;
    }

    /**
   * @return the latitude
   */
    public double getLatitude() {
        return latitude;
    }

    /**
   * @param latitude the latitude to set
   */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
   * @return the longitude
   */
    public double getLongitude() {
        return longitude;
    }

    /**
   * @param longitude the longitude to set
   */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
   * @return the elevation
   */
    public double getElevation() {
        return elevation;
    }

    /**
   * @param elevation the elevation to set
   */
    public void setElevation(double elevation) {
        this.elevation = elevation;
    }

    @Override
    public String toString() {
        StringBuilder self = new StringBuilder();
        self.append("(");
        self.append(latitude);
        self.append(", ");
        self.append(longitude);
        self.append(", ");
        self.append(elevation);
        self.append(")");
        return self.toString();
    }
}
