package dbaccess.cosm;

import java.sql.*;
import java.util.*;
import dbaccess.util.*;

/*** This class retrieves a vector of cosmic rays data from the cosmdb database.* <p>* The search uses a lat/lon boundary box, and a date range.* <ul>* <li>The lat/lon box is specified by a <i>from</i> and <i>to</i> lat/lon* point on the globe.  Only stations between these points will match.* <li>The date range limits the search in time.* <li>By default all stations will be included in the search unless the* station property has been set to a specific station in which case only* that station is included in the search,* </ul>* Use the <i>constructor</i> to initialize the object (to a null vector).* Use the <i>set()</i> methods to set the properties defining the search.* Use the <i>get()</i> methods to retrieve the vector of stations.  For* convenience, variations on the <i>get()</i> method have been provided to* allow you to pass search criteria combining the set search properties* and the retrievals into a single method call.  All retrievals return the* number of stations found.  Method <i>getData()</i> returns a single* station from the vector based on the index into the vector that is passed* as a parameter.* <p>* Use the <i>print()</i> method to print the list of stations to System out.*/
public class InventoryList extends Vector {

    DBConnect connection;

    Statement stmt;

    String stn;

    int yr;

    int mo;

    String source;

    LatLon SWLatLon;

    LatLon NELatLon;

    DateTime fromDate;

    DateTime toDate;

    LatLon ll;

    boolean multipleYears;

    int found;

    /**     * Constructor that creates the object and sets default search criteria.     */
    public InventoryList() {
        found = 0;
        multipleYears = false;
        stn = null;
    }

    /**     * Constructor that creates the object and sets default search criteria.     * @param c DBConnect object for JDBC     */
    public InventoryList(DBConnect c) {
        connection = c;
        stmt = c.getStatement();
        found = 0;
        multipleYears = false;
        stn = null;
    }

    /**     * Reset search variables     */
    public void reset() {
        stn = null;
        yr = 0;
        mo = 0;
        found = 0;
    }

    /**     * Set database connection.     * @param c Database connection     */
    public void setConnection(DBConnect c) {
        connection = c;
        stmt = c.getStatement();
    }

    /**     * Set station for searching.     * @param station Station to search for.  If <i>null</i> then search     * for all stations.     */
    public void setStn(String station) {
        stn = station;
    }

    /**     * Set year for searching.     * @param y Year to search for.     */
    public void setYr(int y) {
        yr = y;
    }

    /**     * Set month for searching.     * @param m Month to search for.     */
    public void setMo(int m) {
        mo = m;
    }

    /**     * Set source for searching.     * @param source Source to search for.  If the source is not set, the     * default will be General Format.     */
    public void setSource(String src) {
        source = src;
    }

    /**     * Set SW corner of lat/lon box for searching.     * @param swLL SW Corner of Latitude/Longitude point on the boundary box     */
    public void setSWLatLon(LatLon swLL) {
        SWLatLon = swLL;
    }

    /**     * Set NE corner of lat/lon box for searching.     * @param neLL NE Corner of Latitude/Longitude point on the boundary box     */
    public void setNELatLon(LatLon neLL) {
        NELatLon = neLL;
    }

    /**     * Set SW corner of lat/lon box for searching.     * @param lat Latitude of SW corner of the boundary box     * @param lon Longitude of SW corner of the boundary box     */
    public void setSWLatLon(float lat, float lon) {
        SWLatLon = new LatLon(lat, lon);
    }

    /**     * Set NE corner of lat/lon box for searching.     * @param lat Latitude of NE corner of the boundary box     * @param lon Longitude of NE corner of the boundary box     */
    public void setNELatLon(float lat, float lon) {
        NELatLon = new LatLon(lat, lon);
    }

    /**     * Set start of date/time range.     * @param fromDT From date/time to search for.     */
    public void setFromDate(DateTime fromDT) {
        fromDate = fromDT;
    }

    /**     * Set end of date/time range.     * @param toDT To Date/time to search for.  Use a time of     * <i>23:59:59</i> to include everything in the to date.     */
    public void setToDate(DateTime toDT) {
        toDate = toDT;
    }

    /**     * Set start of date/time range.     * @param yr From year to search for.     * @param mo From mo to search for.     * @param day From day to search for.     */
    public void setFromDate(int yr, int mo, int day) {
        fromDate = new DateTime(yr, mo, day, 0, 0, 0);
    }

    /**     * Set end of date/time range.  Time defaults to 23:59:59.     * @param yr To year to search for.     * @param mo To mo to search for.     * @param day To day to search for.     */
    public void setToDate(int yr, int mo, int day) {
        toDate = new DateTime(yr, mo, day, 23, 59, 59);
    }

    /**     * Set from/to date range to a year/month.  The <i>from date</i> will be     * set to the beginning of the month and the <i>to date</i> will be set     * to the end of the month.     * @param yr Year to search for.     * @param mo Month to search for.     */
    public void setDateRange(int yr, int mo) {
        setFromDate(yr, mo, 1);
        int d = fromDate.daysInMonth();
        setToDate(yr, mo, d);
    }

    /**     * Set flag indicating that search should proceed across multiple years     * ignoring the year in the search except for setting the range of years     * to be included in the search.     * @param multiYrs True if multiple years; False if single year.     */
    public void setMultipleYears(boolean multiYrs) {
        multipleYears = multiYrs;
    }

    /**     * Get a vector of station data within a date range and within a     * lat/lon box.     * <p>     * The lat/lon box is defined using the futhermost SW (lower left) and     * NE (upper right) geographical points.  For example:     * <p>     * Northern Hemisphere:     * <ul>     *   <li>SW corner (40.0,100.0) or (40.0,-110.0)     *   <li>NE corner (45.0,110.0) or (45.0,-100.0)     * </ul>     * <p>     * Southern Hemisphere:     * <ul>     *   <li>SW corner (-35.0,100.0) or (-35.0,-110.0)     *   <li>NE corner (-30.0,110.0) or (-30.0,-100.0)     * </ul>     * <p>     * The date range define the minimum and maximum dates (optionally time     * also) of the search.  If the multipleYears property is set then the     * search is done for each year between the minimum and maximum years in     * the date range.     * <p>     * For example, if the <i>from</i> date/time is set to 6/5/1992     * and the <i>to</i> date/time is set to 6/9/1998 then all     * stations falling between Jun 5 and Jun 9 in years 1992 -&gt; 1998 and     * within the lat/lon range requested will be included in the station     * vector.     * <p>     * If the date/time range is 12/28/1992 -&gt; 1/3/1998, then data included     * will be:     * <ul>     *      year 1992: all data after  Dec 28<br>     *      years 1993-&gt;1997: all data before Jan 3 and after Dec 28<br>     *      year 1998: all data before Jan 3     * </ul>     * @return The number of stations found matching the search criteria     */
    public int get() {
        removeAllElements();
        if (multipleYears) return findMultipleYears(); else return find(fromDate, toDate);
    }

    /**    * Get a vector of station data between the dates and a lat/lon box.    * @return The number of records found.    */
    private int find(DateTime fromDT, DateTime toDT) {
        float Wlon, Elon;
        Format f7 = new Format("%7.3f");
        Format f8 = new Format("%8.3f");
        String query = "SELECT datagf.*,stations.lat,stations.lon";
        query += " FROM datagf,stations";
        if (stn != null && stn != "") {
            query += " WHERE datagf.stn='" + stn + "'";
        } else {
            query += " WHERE lat>=" + f7.form(SWLatLon.getLat());
            query += " AND lat<=" + f7.form(NELatLon.getLat());
            Wlon = SWLatLon.getLon();
            Elon = NELatLon.getLon();
            boolean crossMeridian = false;
            if (Elon < Wlon) crossMeridian = true;
            if (crossMeridian) {
                query += " AND (lon>=" + f8.form(SWLatLon.getLon());
                query += " OR lon<=" + f8.form(NELatLon.getLon()) + ")";
            } else {
                query += " AND lon>=" + f8.form(SWLatLon.getLon());
                query += " AND lon<=" + f8.form(NELatLon.getLon());
            }
        }
        query += " AND obsdate>=" + fromDT.toCustomString("yyyyMMdd");
        query += " AND obsdate<=" + toDT.toCustomString("yyyyMMdd");
        query += " AND datagf.stn=stations.stn";
        query += " ORDER BY datagf.stn,obsdate";
        try {
            ResultSet rs = stmt.executeQuery(query);
            boolean First = true;
            while (rs.next()) {
                CosmData gd = new CosmData(connection);
                gd.extractData(rs);
                addElement(gd);
                found++;
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println(" *** " + e.getSQLState());
            e.printStackTrace();
        }
        if (!multipleYears) trimToSize();
        return found;
    }

    /**    * Get a vector of data between the dates for one (or all) station(s).    * This method assumes the search criteria has been set.    * @return The number of records found.    */
    public int getStnYrMo() {
        removeAllElements();
        String query = "SELECT * FROM ";
        if (source.equals("4")) query += "data4096"; else query += "datagf";
        query += " WHERE obsdate>=" + fromDate.toCustomString("yyyyMMdd");
        query += " AND obsdate<=" + toDate.toCustomString("yyyyMMdd");
        if (stn != null && stn != "") query += " AND stn='" + stn + "'";
        query += " ORDER BY obsdate";
        try {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                if (source.equals("G")) {
                    CosmData cd = new CosmData(connection);
                    cd.extractData(rs);
                    addElement(cd);
                } else if (source.equals("4")) {
                    CosmData4 cd4 = new CosmData4(connection);
                    cd4.extractData(rs);
                    addElement(cd4);
                }
                found++;
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println(" *** " + e.getSQLState());
            e.printStackTrace();
        }
        trimToSize();
        return found;
    }

    /**    * Get inventory info for a station/year.    * Assumes the search criteria has been previously set with    * the <i>setStn</i> and <i>setYear</i> methods.    * @return True if inventory Info was found; False otherwise    * @see InventoryList#setStn    * @see InventoryList#setYr    */
    public int getByStnYr() {
        found = 0;
        String query = "SELECT * FROM inventory";
        query += " WHERE stn='" + stn + "'";
        query += " and yr=" + yr;
        if (source != null) query += " and source='" + source + "'";
        query += " ORDER BY stn,yr";
        try {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                Inventory inv = new Inventory(connection);
                inv.extractData(rs);
                addElement(inv);
                found++;
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return found;
    }

    /**    * Get inventory info for a station.    * Assumes the search criteria has been previously set with    * the <i>setStn</i> methods.    * @return True if inventory Info was found; False otherwise    * @see InventoryList#setStn    */
    public int getByStn() {
        found = 0;
        String query = "SELECT stn,yr,source,";
        query += "sum(numCorrected) as numCorrected,";
        query += "sum(numUncorrected) as numUncorrected,";
        query += "sum(numPressure) as numPressure,";
        query += "sum(avgCorrected) as avgCorrected,";
        query += "sum(avgUncorrected) as avgUncorrected,";
        query += "sum(avgPressure) as avgPressure,";
        query += "sum(minCorrected) as minCorrected,";
        query += "sum(minUncorrected) as minUncorrected,";
        query += "sum(minPressure) as minPressure,";
        query += "sum(maxCorrected) as maxCorrected,";
        query += "sum(maxUncorrected) as maxUncorrected,";
        query += "sum(maxPressure) as maxPressure";
        query += " FROM inventory";
        query += " WHERE stn='" + stn + "'";
        query += " GROUP BY yr,source";
        query += " ORDER BY yr,source";
        try {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                Inventory inv = new Inventory(connection);
                inv.extractData(rs);
                addElement(inv);
                found++;
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return found;
    }

    /**    * Get inventory info for a year.    * Assumes the search criteria has been previously set with    * the <i>setYr</i> method.    * @return True if inventory Info was found; False otherwise    * @see InventoryList#setYr    */
    public int getByYr() {
        found = 0;
        String query = "SELECT stn,yr,source,";
        query += "sum(numCorrected) as numCorrected,";
        query += "sum(numUncorrected) as numUncorrected,";
        query += "sum(numPressure) as numPressure,";
        query += "sum(avgCorrected) as avgCorrected,";
        query += "sum(avgUncorrected) as avgUncorrected,";
        query += "sum(avgPressure) as avgPressure,";
        query += "sum(minCorrected) as minCorrected,";
        query += "sum(minUncorrected) as minUncorrected,";
        query += "sum(minPressure) as minPressure,";
        query += "sum(maxCorrected) as maxCorrected,";
        query += "sum(maxUncorrected) as maxUncorrected,";
        query += "sum(maxPressure) as maxPressure";
        query += " FROM inventory";
        query += " WHERE yr=" + yr;
        query += " GROUP BY stn,source";
        query += " ORDER BY stn,source";
        try {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                Inventory inv = new Inventory(connection);
                inv.extractData(rs);
                addElement(inv);
                found++;
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return found;
    }

    /**    * Get a vector of station data between the dates and a lat/lon box but    * ignore year so data is retrieved across all years in the database.    */
    private int findMultipleYears() {
        int fyr, tyr;
        DateTime ftm, ttm, tmptm;
        multipleYears = true;
        fyr = fromDate.get(Calendar.YEAR);
        tyr = toDate.get(Calendar.YEAR);
        ftm = (DateTime) fromDate.clone();
        ttm = (DateTime) toDate.clone();
        for (int y = fyr; y <= tyr; y++) {
            ftm.set(Calendar.YEAR, y);
            ttm.set(Calendar.YEAR, y);
            if (y < tyr && ttm.before(ftm)) ttm.add(Calendar.YEAR, 1);
            if (y == tyr && y > fyr && ttm.before(ftm)) ftm.add(Calendar.YEAR, -1);
            found += find(ftm, ttm);
        }
        return found;
    }

    /**     * Get a vector of cosmic rays data for a station/year/month.     * @param stn Station     * @param yr Year     * @param mo Month     * @return The number of lines found matching the search criteria     */
    public int get(String station, int year, int month) {
        stn = station;
        fromDate = new DateTime(year, month, 1);
        int d = fromDate.getMaximum(Calendar.DAY_OF_MONTH);
        toDate = new DateTime(year, month, d, 23, 59, 59);
        return getStnYrMo();
    }

    /**     * Get a vector of station data between the dates and a lat/lon box.  The     * box is defined using the lower left (SW Corner) and upper right     * (NE Corner) points.  Unless the station property has been set     * to a specific station with the <i>setStn</i> method all satellites     * will be included in the search.  You can also use the     * <i>setMultipleYears()</i> method to search across multiple years,     * if desired.     * @param swLL SW Corner of Latitude/Longitude point on the boundary box     * @param neLL NE Corner of Latitude/Longitude point on the boundary box     * @param fromDT From Date/time to search for.     * @param toDT To Date/time to search for.  Use a time of     * <i>23:59:59</i> to include everything in the to date.     * @return The number of lines found matching the search criteria     */
    public int get(LatLon swLL, LatLon neLL, DateTime fromDT, DateTime toDT) {
        SWLatLon = swLL;
        NELatLon = neLL;
        fromDate = fromDT;
        toDate = toDT;
        return get();
    }

    /**     * Get a vector of station data between dates for a single lat/lon point.     * <p>     * This is a convenience method which assumes the start and end lat/lon     * are the same.  Unless the station property has been set to a specific     * station with the <i>setStn</i> method all stations will be included     * in the search.  You can also use the <i>setMultipleYears()</i> method     * to search across multiple years if desired.     * @param ll Latitude/Longitude point to search for     * @param fromDT From Date/time to search for.     * @param toDT To Date/time to search for.  Use a time of     * <i>23:59:59</i> to include everything in the to date.     * @return The number of lines found matching the search criteria     */
    public int get(LatLon ll, DateTime fromDT, DateTime toDT) {
        SWLatLon = ll;
        NELatLon = ll;
        fromDate = fromDT;
        toDate = toDT;
        return get();
    }

    /**     * Get a vector of station data within a lat/lon box for a single date.     * <p>     * This is a convenience method which assumes the start and end lat/lon     * are the same.  Unless the station property has been set to a specific     * station with the <i>setSat</i> method all stations will be included     * in the search.     * @param swLL SW Corner of Latitude/Longitude point on the boundary box     * @param neLL NE Corner of Latitude/Longitude point on the boundary box     * @param dttm Date/time to search for     * @return The number of Data objects found matching the search criteria     */
    public int get(LatLon swLL, LatLon neLL, DateTime dttm) {
        SWLatLon = swLL;
        NELatLon = neLL;
        fromDate = dttm;
        toDate = dttm;
        return get();
    }

    /**     * Print the cosmic rays information for a vector of inventories.     * @see InventoryList#get     */
    public void print() {
        int l = size();
        if (l < 1) {
            System.out.println("*** No data found\n");
            System.out.flush();
            return;
        }
        for (int i = 0; i < l; i++) {
            Inventory inv = (Inventory) elementAt(i);
            inv.printline();
            System.out.println("\n");
        }
        System.out.flush();
    }

    /**     * Print the cosmic rays information for a vector of inventories.     * @see InventoryList#get     */
    public void printSize() {
        int l = size();
        System.out.println(l + " data records");
        System.out.flush();
    }
}
