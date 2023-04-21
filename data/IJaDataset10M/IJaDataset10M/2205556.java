package org.routenova.utils.zipcode;

import java.util.*;
import java.sql.*;
import java.lang.Math;

/**
 * Zip code geo location search class.
 * For zip code geo calculations you need database table called "zipcode".
 * With following fields:</br>
 * 1. zipcode - VARCHAR (zip code id)</br>
 * 2. lat - FLOAT8 (zip code point latitude)</br>
 * 3. lon - FLOAT8 (zip code point longitude)</br>
 * 4. city - VARCHAR (city name where zip point located)</br>
 * 
 * @author  Mikhail Branicki <mikhail.branicki@gmail.com>
 * @param   db          Database connection.
 * @param   dbScheme     Database sheme where zipcode tables and procedures located.
 * @param   isMetric    True if distance in kilometres, False if in Miles. 
 */
public class Zipcode {

    /**
     * Database connection
     */
    private Connection db;

    /**
     * Database scheme name 
     */
    private String dbScheme = null;

    /**
     * Measure system switcher. True if you want to search in kilometres, false
     * standart miles. 
     */
    private boolean isMetric = true;

    /**
     * Zip codes list.
     */
    private List<String> zipcodes = new ArrayList<String>();

    public Zipcode(Connection db, String dbScheme, boolean isMetric) {
        this.db = db;
        this.dbScheme = dbScheme;
        this.isMetric = isMetric;
    }

    /**
     * Check for zipcode in database.
     * @param   zipcode     Zip code to search. 
     * @return              True if given zip code presents, False if not.
     */
    public boolean isZipcode(String zipcode) {
        boolean isDone = false;
        PreparedStatement stmt = null;
        try {
            stmt = db.prepareStatement("SELECT * FROM " + dbScheme + ".zipcode WHERE zipcode = ?");
            stmt.setString(1, zipcode);
            ResultSet rs = stmt.executeQuery();
            db.commit();
            while (rs.next()) {
                isDone = true;
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isDone;
    }

    /**
     * Calculate distance between two zip points. 
     * @param   fromZip     Source zip point
     * @param   toZip       Destination zip point
     * @return              Distance, measurent units depend on isMetric variable.
     */
    public Double findDistance(String fromZip, String toZip) {
        PreparedStatement stmt = null;
        Double lat_src = 0.0;
        Double lon_src = 0.0;
        Double lat_dst = 0.0;
        Double lon_dst = 0.0;
        Double distance = 0.0;
        try {
            stmt = db.prepareStatement("SELECT lat,lon FROM " + dbScheme + ".zipcode WHERE zipcode = ?");
            stmt.setString(1, fromZip);
            ResultSet rs = stmt.executeQuery();
            db.commit();
            if (rs.next()) {
                lat_src = rs.getDouble("lat");
                lon_src = rs.getDouble("lon");
            } else {
                return -1.0;
            }
            stmt.setString(1, toZip);
            rs = stmt.executeQuery();
            db.commit();
            if (rs.next()) {
                lat_dst = rs.getDouble("lat");
                lon_dst = rs.getDouble("lon");
            } else {
                return -1.0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        lat_src = Math.toRadians(lat_src);
        lon_src = Math.toRadians(lon_src);
        lat_dst = Math.toRadians(lat_dst);
        lon_dst = Math.toRadians(lon_dst);
        distance = Math.acos(Math.sin(lat_src) * Math.sin(lat_dst) + Math.cos(lat_src) * Math.cos(lat_dst) * Math.cos(lon_dst - lon_src)) * 6372.797;
        if (isMetric == false) {
            distance = toMiles(distance);
        }
        return distance;
    }

    /**
     * Calculate zip code radius distance search.
     * @param   zipcode         Zip point
     * @param   distance        Distance
     * @return                  true if zip point exists and zipcodes araund it
     *                          was found, false if zip code don't exists or search
     *                          return null result.
     */
    public boolean findRadius(String zipcode, Integer distance) {
        ResultSet rs = null;
        CallableStatement stmt = null;
        boolean isDone = false;
        double radiusDistance = 0;
        Integer i = 0;
        if (isMetric == false) {
            radiusDistance = toKilometres(distance);
        } else {
            radiusDistance = distance.doubleValue();
        }
        try {
            db.setAutoCommit(false);
            stmt = db.prepareCall("{ ? = call " + dbScheme + " .get_zipradius(?,?) }", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(2, zipcode);
            stmt.setDouble(3, radiusDistance);
            stmt.registerOutParameter(1, Types.OTHER);
            stmt.execute();
            rs = (ResultSet) stmt.getObject(1);
            while (rs.next()) {
                isDone = true;
                zipcodes.add(rs.getString(1));
                i++;
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isDone;
    }

    /**
     * Retrive zip code array list generated by findRadius() search.
     * @return  Zip code names string array.
     */
    public List getZipcodes() {
        return this.zipcodes;
    }

    /**
     * Convert miles (Integer) to kilometres.
     * 
     * @param distance_ml   Distance in miles.
     * @return              Distance in kilometres
     */
    private Double toKilometres(Integer distance_ml) {
        return distance_ml.floatValue() * 1.60934;
    }

    /**
     * Convert miles (Double) to kilometres.
     * 
     * @param distance_ml   Distance in miles.
     * @return              Distance in kilometres
     */
    private Double toKilometres(Double distance_ml) {
        return distance_ml * 1.60934;
    }

    /**
     * Convert kilometres (Integer) to miles.
     * 
     * @param distance_km   Distance in kilometres.
     * @return              Distance in miles.
     */
    private Double toMiles(Integer distance_km) {
        return distance_km.doubleValue() / 1.60934;
    }

    /**
     * Convert kilometres (Double) to miles.
     * 
     * @param distance_km   Distance in kilometres.
     * @return              Distance in miles.
     */
    private Double toMiles(Double distance_km) {
        return distance_km / 1.60934;
    }
}
