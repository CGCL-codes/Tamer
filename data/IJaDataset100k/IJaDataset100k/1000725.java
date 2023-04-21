package net.sf.magicmap.client.model.node;

/**
 * Data structure for geo positions. Provides transformation 
 * facilities to  
 * 
 * @author Johannes Zapotoczky (johannes@zapotoczky.com)
 */
public class GeoPos {

    /**
     * standard deviation (1m), most likely too optimistic
     */
    public static final int GEO_STD_DEVIATION = 1000;

    /**
     * factor to transform ms into degrees
     */
    private static final int GEO_MS2DEGREES_FACTOR = 3600000;

    /**
     * factor to transfrom internal resolution to displayed values
     */
    private static final int GEO_M2N_FACTOR = 1000;

    /**
     * longitude in milliseconds
     */
    private int longitude;

    /**
     * latitude in milliseconds
     */
    private int latitude;

    /**
     * altitude in mm
     */
    private int altitude;

    /**
     * accuracy in mm
     */
    private int exactitude;

    /**
     * Constructor (with standard deviation for accuracy)
     * @param longitude - longitude in ms
     * @param latitude - latitude in ms
     * @param altitude - altitude in mm
     */
    public GeoPos(int longitude, int latitude, int altitude) {
        this(longitude, latitude, altitude, GeoPos.GEO_STD_DEVIATION);
    }

    /**
     * Constructor
     * @param longitude - longitude in ms
     * @param latitude - latitude in ms
     * @param altitude - altitude in mm
     * @param exactitude - accuracy in mm
     */
    public GeoPos(int longitude, int latitude, int altitude, int exactitude) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.altitude = altitude;
        this.exactitude = exactitude;
    }

    /**
     * Constructor
     * @param east - <code>true</code> if the longitude is east, <code>false</code> if it is west
     * @param longDegrees - the longitude's degrees
     * @param longMinutes - the longitude's minutes
     * @param longSeconds - the longitude's seconds
     * @param north - <code>true</code> if the latitude is north, <code>false</code> if it is south
     * @param latDegrees - the latitude's degrees
     * @param latMinutes - the latitude's minutes
     * @param latSeconds - the latitude's seconds
     * @param altitude - the altitude in m
     * @param exactitude - the exactitude in m
     */
    public GeoPos(boolean east, Integer longDegrees, Integer longMinutes, Double longSeconds, boolean north, Integer latDegrees, Integer latMinutes, Double latSeconds, Integer altitude, Integer exactitude) {
        int tmpLongMs = longDegrees.intValue() * 3600000;
        tmpLongMs += longMinutes.intValue() * 60000;
        tmpLongMs += longSeconds.doubleValue() * 1000;
        if (east) this.longitude = tmpLongMs; else this.longitude = -tmpLongMs;
        int tmpLatMs = latDegrees.intValue() * 3600000;
        tmpLatMs += latMinutes.intValue() * 60000;
        tmpLatMs += latSeconds.doubleValue() * GeoPos.GEO_M2N_FACTOR;
        if (north) this.latitude = tmpLatMs; else this.latitude = -tmpLatMs;
        this.altitude = altitude.intValue() * GeoPos.GEO_M2N_FACTOR;
        this.exactitude = exactitude.intValue() * GeoPos.GEO_M2N_FACTOR;
    }

    /**
     * Constructor
     * @param longitude - longitude in degrees
     * @param latitude - latitude in degrees
     * @param altitude - altitude in m
     * @param exactitude - exactitude in m
     */
    public GeoPos(Double longitude, Double latitude, Integer altitude, Integer exactitude) {
        this.longitude = (int) longitude.doubleValue() * GeoPos.GEO_MS2DEGREES_FACTOR;
        this.latitude = (int) latitude.doubleValue() * GeoPos.GEO_MS2DEGREES_FACTOR;
        this.altitude = altitude.intValue() * GeoPos.GEO_M2N_FACTOR;
        this.exactitude = exactitude.intValue() * GeoPos.GEO_M2N_FACTOR;
    }

    /**
     * Constructor
     * @param longitude - longitude in degrees
     * @param latitude - latitude in degrees
     * @param altitude - altitude in m
     * @param exactitude - exactitude in m
     */
    public GeoPos(double longitude, double latitude, double altitude, double exactitude) {
        this.longitude = (int) (longitude * GeoPos.GEO_MS2DEGREES_FACTOR);
        this.latitude = (int) (latitude * GeoPos.GEO_MS2DEGREES_FACTOR);
        this.altitude = (int) (altitude * GeoPos.GEO_M2N_FACTOR);
        this.exactitude = (int) (exactitude * GeoPos.GEO_M2N_FACTOR);
    }

    /**
     * Get the whole longitude in degrees
     * @return - the longitude in degrees
     */
    public Double getWholeLongitudeInDegrees() {
        return new Double(this.longitude / (double) GeoPos.GEO_MS2DEGREES_FACTOR);
    }

    /**
     * Get the whole latitude in degrees
     * @return - the latitude in degrees
     */
    public Double getWholeLatitudeInDegrees() {
        return new Double(this.latitude / (double) GeoPos.GEO_MS2DEGREES_FACTOR);
    }

    /**
     * Get the altitude in m
     * @return altitude in m
     */
    public Double getAltitudeInM() {
        return new Double(this.altitude / (double) GeoPos.GEO_M2N_FACTOR);
    }

    /**
     * Get the accuracy in m
     * @return accuracy in m
     */
    public Double getExactitude() {
        return new Double(this.exactitude / (double) GeoPos.GEO_M2N_FACTOR);
    }

    public Integer getLongitudeDegrees() {
        return new Integer(this.longitude / GeoPos.GEO_MS2DEGREES_FACTOR);
    }

    public Integer getLongitudeMinutes() {
        int minutes = this.longitude % GeoPos.GEO_MS2DEGREES_FACTOR;
        return new Integer(minutes / 60 / GeoPos.GEO_M2N_FACTOR);
    }

    public Integer getLongitudeSeconds() {
        int minutes = this.longitude % GeoPos.GEO_MS2DEGREES_FACTOR;
        int seconds = minutes % 60000;
        return new Integer(seconds / GeoPos.GEO_M2N_FACTOR);
    }

    public Integer getLatitudeDegrees() {
        return new Integer(this.latitude / GeoPos.GEO_MS2DEGREES_FACTOR);
    }

    public Integer getLatitudeMinutes() {
        int minutes = this.latitude % GeoPos.GEO_MS2DEGREES_FACTOR;
        return new Integer(minutes / 60 / GeoPos.GEO_M2N_FACTOR);
    }

    public Integer getLatitudeSeconds() {
        int minutes = this.latitude % GeoPos.GEO_MS2DEGREES_FACTOR;
        int seconds = minutes % 60000;
        return new Integer(seconds / GeoPos.GEO_M2N_FACTOR);
    }

    @Override
    public String toString() {
        return "longitude: " + this.longitude + ", latitude: " + this.latitude + ", altitude: " + this.altitude + ", exactitude: " + this.exactitude;
    }

    public String getStringRepresentation() {
        return getLongitudeDegrees().intValue() + "� " + getLongitudeMinutes().intValue() + "' " + getLongitudeSeconds().intValue() + "'' :: " + getLatitudeDegrees().intValue() + "� " + getLatitudeMinutes().intValue() + "' " + getLatitudeSeconds().intValue() + "''";
    }

    public int getLatitude() {
        return this.latitude;
    }

    public int getLongitude() {
        return this.longitude;
    }

    public int getAltitude() {
        return this.altitude;
    }
}
