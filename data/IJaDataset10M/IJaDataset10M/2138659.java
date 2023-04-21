package dbaccess.geom;

import java.sql.*;
import java.util.*;
import java.awt.*;
import java.io.*;
import dbaccess.util.*;

/**
* This class retrieves and stores geomagnetic 1 minute data from the geomdb
* database for a single station, date and element.
* <p>
* The <i>constructor</i> only creates the object and stores connection
* information.  Set search parameters using the <i>set</i> methods and
* use the <i>get()</i> method to do the search.  The <i>getXxxx</i>
* methods provide access to the retrieved data elements.
* <p>
* Use the <i>print()</i> method to print Geomagnetic data to System out.
*/
public class GeomDataMin {

    Statement stmt;

    DBConnect connection;

    boolean found;

    /** Station ID. */
    public String stn;

    /** Origin used in search. */
    public String origin;

    /** Lat/lon of station used in search. */
    LatLon latlon;

    /** Lat/lon of station found. */
    LatLon stnLatLon;

    /** Date of the observation. */
    public DateTime obsdate;

    /**
     * Element for the observation.
     * Possible values are: D, H, Z, F, I, X, Y.
     */
    public String element;

    /** Tabular base of the observation. */
    public int base;

    /**
     * Number of non-missing observations in the data array.
     */
    public int Nobs;

    /** Data array; one for each data value before normalization. */
    public short[][] rawData;

    /** Data array to hold the normalized values calculated using
     * the tabular base.
     */
    public int[][] data;

    /**
    * Prepare to retrieve information and establsish a connection to the
    * database.
    * @param c JDBC DBConnect object for access to the database
    */
    public GeomDataMin(DBConnect c) {
        connection = c;
        stmt = c.getStatement();
        found = false;
    }

    /**
  * Set station for searching.
  * @param station Station to search for.  If <i>null</i> then search
  * for all stations.
  */
    public void setStn(String station) {
        stn = station;
    }

    /**
  * Set origin for searching.  If <i>null</i> then search
  * for all origins.  Set to the String, <i>"Final"</i>,  to search
  * only for final, absolute data.
  * @param orgn Origin to search for.
  */
    public void setOrigin(String orgn) {
        origin = orgn;
    }

    /**
  * Set element for searching.
  * Possible values are: D, H, Z, F, I, X, Y.
  * @param elmnt Element to search for.  If <i>null</i> then search
  * for all elements.
  */
    public void setElement(String elmnt) {
        element = elmnt;
    }

    /**
   * Set lat/lon for searching.
   * @param ll Latitude/Longitude point to search for
   */
    public void setLatlon(LatLon ll) {
        latlon = ll;
    }

    /**
   * Set start of date/time range.
   * @param dt Date to search for
   */
    public void setObsdate(DateTime dt) {
        obsdate = dt;
    }

    /**
   * Get station code.
   * @return Station code
   */
    public String getStn() {
        return stn;
    }

    /**
   * Get origin.
   * @return Origin
   */
    public String getOrigin() {
        return origin;
    }

    /**
   * Get observation date.
   * @return Observation date
   */
    public DateTime getObsdate() {
        return obsdate;
    }

    /**
   * Get element.
   * @return Element
   */
    public String getElement() {
        return element;
    }

    /**
   * Get Number of non-missing observations.
   * @return Get Number of non-missing observations
   */
    public int getNobs() {
        return Nobs;
    }

    /**
   * Get the tabular base.
   * @return the tabular base
   */
    public int getBase() {
        return base;
    }

    /**
   * Get the lat/lon for the station found.
   * @return the station's LatLon
   */
    public LatLon getStnLatLon() {
        return stnLatLon;
    }

    /**
   * 
   */
    public Float getLatitude() {
        return stnLatLon.getLat();
    }

    /**
   * 
   */
    public Float getLongitude() {
        return stnLatLon.getLon();
    }

    /**
   * Get observations.  If the data has not yet been normalized, it
   * will be done automatically.
   * @return An array holding the normalized value of each observation
   * @see GeomDataMin#normalize
   */
    public int[][] getData() {
        if (data == null) normalize();
        return data;
    }

    /**
   * Get observations.
   * @return An array holding the raw data values of each observation.
   * @see GeomDataMin#normalize
   */
    public short[][] getRawData() {
        return rawData;
    }

    /**
   * Get the data for a station, origin and element for a day.
   * This is a * convenience method which sets search parameters
   * and does the search.
   * @param station Station id
   * @param orgn Origin code.  Use "Final" to get only final data.
   * @param elmnt element (D, H, Z, F, I, X, Y)
   * @param dttm Data date/time
   * @return True if Data was found; False otherwise
   */
    public boolean get(String station, String orgn, String elmnt, DateTime dttm) {
        setStn(station);
        setOrigin(orgn);
        setElement(elmnt);
        setObsdate(dttm);
        return get();
    }

    /**
   * Get the data for a station, origin and element for a day.
   * Assumes the search criteria have been previously set with
   * the <i>set</i> methods.
   * @return True if Data was found; False otherwise
   * @see GeomDataMin#setStn
   * @see GeomDataMin#setOrigin
   * @see GeomDataMin#setElement
   * @see GeomDataMin#setObsdate
   */
    public boolean get() {
        found = false;
        String dt = obsdate.toCustomString("yyyyMMdd");
        String table = "min" + obsdate.toCustomString("yyyy");
        String query = "SELECT " + table + ".*,stations.lat,stations.lon";
        query += " FROM " + table + ",stations";
        query += " WHERE " + table + ".stn='" + stn + "' AND element='" + element + "'";
        if (origin != null && !origin.equals("Final")) query += " AND origin='" + origin + "'"; else if (origin != null && origin.equals("Final")) query += " AND origin!='A' AND origin!='V'";
        query += " AND obsdate='" + dt + "'";
        query += " AND " + table + ".stn=stations.stn";
        System.out.println("SQL=" + query);
        try {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                found = true;
                extractData(table, rs);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return found;
    }

    /**
     * Retrieve data elements from the JDBC ResultSet.
     * @param table The table to extract the data from
     * @param rs JDBC ResultSet object containing observation data
     */
    public void extractData(String table, ResultSet rs) {
        DateTime tmptm;
        byte[] bdata;
        ByteArrayInputStream bais;
        DataInputStream dis;
        try {
            stn = rs.getString(table + ".stn");
            String cdate = rs.getString("obsdate") + " 00:00:00";
            obsdate = new DateTime(cdate);
            origin = rs.getString("origin");
            element = rs.getString("element");
            base = rs.getInt("base");
            Nobs = rs.getShort("Nobs");
            stnLatLon = new LatLon(rs.getFloat("lat"), rs.getFloat("lon"));
            stnLatLon.setFormat("%7.3f", "%7.3f");
            stnLatLon.setDelimiter("");
            bdata = rs.getBytes("gdata");
            rawData = new short[24][60];
            bais = new ByteArrayInputStream(bdata);
            dis = new DataInputStream(bais);
            for (int h = 0; h < 24; h++) for (int m = 0; m < 60; m++) rawData[h][m] = dis.readShort();
            found = true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Normalize the Geomagnetic data readings using the tabular
     * base to calculate their values.  Assumes the data has already
     * been retrieved using the get() method.
     * @see GeomDataMin#get
     */
    public void normalize() {
        if (found) {
            data = new int[24][60];
            for (int h = 0; h < 24; h++) for (int m = 0; m < 60; m++) {
                if (rawData[h][m] == 9999) data[h][m] = 999999; else data[h][m] = rawData[h][m] + base;
            }
        } else System.out.println("\n*** Geomagnetic data not found");
    }

    /**
     * Get the data closest to a lat/lon and an element for a day.
     * @param latlon Lat/lon to find for searching for closest data
     * @param orgn origin code.  Use <i>"Final"</i> to get only final data.
     * @param elmnt element (D, H, Z, F, I, X, Y).
     * @param dttm Data date/time
     * @returns True if Data was found; False otherwise
     */
    public boolean getClosest(LatLon latlon, String orgn, String elmnt, DateTime dttm) {
        Station s = new Station(connection);
        setOrigin(orgn);
        setElement(elmnt);
        setObsdate(dttm);
        String table = "min" + dttm.toCustomString("yyyy");
        setStn(s.getClosestStation(table, elmnt, dttm, latlon));
        return get();
    }

    /**
     * Get the data closest to a lat/lon and an element for a day.
     * Assumes the element and date have been previously set with the
     * <i>set</i> methods.
     * @return True if Data was found; False otherwise
     */
    public boolean getClosest(LatLon latlon) {
        Station s = new Station(connection);
        String table = "min" + obsdate.toCustomString("yyyy");
        setStn(s.getClosestStation(table, element, obsdate, latlon));
        return get();
    }

    /**
    * Updates the origin code for a station within a date range.
    * @param stn Station ID
    * @param fdate From date in date range
    * @param tdate To date in date range
    * @param code Origin code
    * @return Number of rows changed, or 0 if nothing found.
    */
    public int updateOrigin(String stn, DateTime fdate, DateTime tdate, String code) {
        String fromDate = fdate.toCustomString("yyyyMMdd");
        String toDate = tdate.toCustomString("yyyyMMdd");
        int fyr = fdate.get(Calendar.YEAR);
        int tyr = tdate.get(Calendar.YEAR);
        int Nrows = 0;
        for (int y = fyr; y <= tyr; y++) {
            String table = "min" + y;
            String query = "UPDATE " + table + " SET origin='" + code + "' WHERE stn='" + stn + "' and obsdate>=" + fromDate + " and obsdate<=" + toDate;
            ResultSet rs;
            try {
                Nrows = stmt.executeUpdate(query);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
        return Nrows;
    }

    /**
    * Count the origin code for a station within a date range.
    * @param stn Station ID
    * @param fdate From date in date range
    * @param tdate To date in date range
    * @param code Origin code
    * @return Number of rows changed, or 0 if nothing found.
    */
    public int countOrigin(String stn, DateTime fdate, DateTime tdate, String code) {
        String fromDate = fdate.toCustomString("yyyyMMdd");
        String toDate = tdate.toCustomString("yyyyMMdd");
        int fyr = fdate.get(Calendar.YEAR);
        int tyr = tdate.get(Calendar.YEAR);
        int count = 0;
        for (int y = fyr; y <= tyr; y++) {
            String table = "min" + y;
            String query = "SELECT count(*) FROM " + table + " WHERE stn='" + stn + "' and obsdate>=" + fromDate + " and obsdate<=" + toDate + " and origin='" + code + "'";
            ResultSet rs;
            try {
                rs = stmt.executeQuery(query);
                for (int i = 0; rs.next(); i++) {
                    count += rs.getInt(1);
                }
                rs.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
        return count;
    }

    /**
     * Determine if the Data information has been found.
     * @return True if the Data info was found; False if not found
     */
    public boolean isFound() {
        return found;
    }

    /**
     * Print the GeomDataMin information.  (No actual data values printed).
     */
    public void print() {
        if (!found) {
            System.out.println("\n*** Geomagnetic data not found");
            return;
        }
        System.out.println("\n    *** Geomagnetic data ***");
        System.out.println("Station=" + stn);
        System.out.println("  Element: " + element + "  Date: " + obsdate);
        System.out.println("    #Obs: " + Nobs + "  Origin: " + origin);
        System.out.println("    #data: " + rawData.length * rawData[0].length);
    }

    /**
    * Print the data with a  header line followed by a line for
    * each hour requested.  Defaults to printing the first four
    * 1 minute data values of the first hour.  This method exists
    * so that you can print observations in a loop.
    * @param idx index in the vector holding the line
    */
    public void printdata(int idx) {
        printdata(idx, 0, 1, 0, 4);
    }

    /**
    * Print the data with a header line and an additional line for each
    * hour requested.
    * @param idx index in the vector holding the line
    * @param startHR Index of the hour in the data array to start printing.
    *        0 is the first data value, 1 is the second, etc.
    * @param numHR The number of hour values to print starting at <i>startHR</i>
    * @param startMIN Index of the minute in the data array to start printing.
    *        0 is the first data value, 1 is the second, etc.
    * @param numMIN The number of minute values to print starting at
    *        <i>startMIN</i>
    */
    public void printdata(int idx, int startHR, int numHR, int startMIN, int numMIN) {
        Format f2 = new Format("%2.2d");
        Format f7 = new Format("%7d");
        int tmp;
        if (!found) {
            System.out.println("\n*** Geomagnetic data not found");
            return;
        }
        System.out.println("[" + f2.form(idx) + "] " + stn + " " + element + " " + origin + " " + obsdate.toDateString() + "  " + Nobs + "  " + base);
        if (data == null) normalize();
        if (startHR > 23) startHR = 23;
        if (startMIN > 59) startMIN = 55;
        if (numHR > (data.length - startHR)) numHR = data.length - startHR;
        if (numMIN > (data[0].length - startMIN)) numMIN = data[0].length - startMIN;
        for (int h = startHR; h < startHR + numHR; h++) {
            System.out.print("  HOUR[" + h + "] ");
            for (int m = startMIN; m < startMIN + numMIN; m++) {
                if (data[h][m] == 999999) {
                    System.out.print(" N/A  ");
                    continue;
                }
                System.out.print(" " + f7.form(data[h][m]));
            }
            if (!((startMIN + numMIN) == 60)) System.out.print("...");
            System.out.println();
        }
    }
}
