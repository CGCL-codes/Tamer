package jme3tools.navigation;

/**
 * This class represents the position of an entity in the world.
 * 
 * @author Benjamin Jakobus (based on JMarine by Cormac Gebruers and Benjamin Jakobus)
 * @version 1.0
 * @since 1.0
 */
public class Position {

    private Coordinate lat;

    private Coordinate lng;

    private String utcTimeStamp;

    private double degree;

    /**
     * A new position expressed in decimal format
     * @param dblLat
     * @param dblLng
     * @since 1.0
     */
    public Position(double dblLat, double dblLng) throws InvalidPositionException {
        lat = new Coordinate(dblLat, Coordinate.LAT);
        lng = new Coordinate(dblLng, Coordinate.LNG);
    }

    /**
     * A new position expressed in DegMin format
     * @param latDeg
     * @param latMin
     * @param lngDeg
     * @param lngMin
     * @since 1.0
     */
    public Position(int latDeg, float latMin, int latQuad, int lngDeg, float lngMin, int lngQuad) throws InvalidPositionException {
        lat = new Coordinate(latDeg, latMin, Coordinate.LAT, latQuad);
        lng = new Coordinate(lngDeg, lngMin, Coordinate.LNG, lngQuad);
    }

    /**
     * A new position expressed in ALRS format
     * @param lat
     * @param lng
     * @since 1.0
     */
    public Position(String lat, String lng) throws InvalidPositionException {
        this.lat = new Coordinate(lat);
        this.lng = new Coordinate(lng);
    }

    /**
     * A new position expressed in NMEA GPS message format:
     * 4807.038,N,01131.000,E
     * @param
     * @param
     * @param
     * @param
     * @since  12.0
     */
    public Position(String latNMEAGPS, String latQuad, String lngNMEAGPS, String lngQuad, String utcTimeStamp) {
        int quad;
        if (latQuad.compareTo("N") == 0) {
            quad = Coordinate.N;
        } else {
            quad = Coordinate.S;
        }
        try {
            this.lat = new Coordinate(Integer.valueOf(latNMEAGPS.substring(0, 2)), Float.valueOf(latNMEAGPS.substring(2)), Coordinate.LAT, quad);
        } catch (InvalidPositionException e) {
            e.printStackTrace();
        }
        if (lngQuad.compareTo("E") == 0) {
            quad = Coordinate.E;
        } else {
            quad = Coordinate.W;
        }
        try {
            this.lng = new Coordinate(Integer.valueOf(lngNMEAGPS.substring(0, 3)), Float.valueOf(lngNMEAGPS.substring(3)), Coordinate.LNG, quad);
        } catch (InvalidPositionException e) {
            e.printStackTrace();
        }
        this.associateUTCTime(utcTimeStamp);
    }

    /**
     * Add a reference time for this position - useful for historical tracking
     * @param data
     * @since 1.0
     */
    public void associateUTCTime(String data) {
        utcTimeStamp = data;
    }

    /**
     * Returns the UTC time stamp
     * @return str the UTC timestamp
     * @since 1.0
     */
    public String utcTimeStamp() {
        return utcTimeStamp;
    }

    /**
     * Prints out position using decimal format
     * @return the position in decimal format
     */
    public String toStringDec() {
        return lat.toStringDec() + " " + lng.toStringDec();
    }

    /**
     * Return the position latitude in decimal format
     * @return the latitude in decimal format
     * @since 1.0
     */
    public double getLatitude() {
        return lat.decVal();
    }

    /**
     * Return the position longitude in decimal format
     * @return the longitude in decimal format
     * @since 1.0
     */
    public double getLongitude() {
        return lng.decVal();
    }

    /**
     * Prints out position using DegMin format
     * @return the position in DegMin Format
     * @since 1.0
     */
    public String toStringDegMin() {
        String output = "";
        output += lat.toStringDegMin();
        output += "   " + lng.toStringDegMin();
        return output;
    }

    /**
     * Prints out the position latitude
     * @return the latitude as a string for display purposes
     * @since 1.0
     */
    public String toStringDegMinLat() {
        return lat.toStringDegMin();
    }

    /**
     * Prints out the position longitude
     * @return the longitude as a string for display purposes
     * @since 1.0
     */
    public String toStringDegMinLng() {
        return lng.toStringDegMin();
    }

    /**
     * Prints out the position latitude
     * @return the latitude as a string for display purposes
     * @since 1.0
     */
    public String toStringDecLat() {
        return lat.toStringDec();
    }

    /**
     * Prints out the position longitude
     * @return the longitude as a string for display purposes
     * @since 1.0
     */
    public String toStringDecLng() {
        return lng.toStringDec();
    }

    public static void main(String[] argsc) {
        Position p = new Position("4807.038", "N", "01131.000", "W", "123519");
        System.out.println(p.toStringDegMinLat());
        System.out.println(p.getLatitude());
        System.out.println(p.getLongitude());
        System.out.println(p.toStringDegMinLng());
        System.out.println(p.utcTimeStamp());
    }
}
