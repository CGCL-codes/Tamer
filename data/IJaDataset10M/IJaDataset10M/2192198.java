package dbaccess.geom;

import java.sql.*;
import java.util.*;
import dbaccess.util.*;
import org.apache.log4j.*;
import spidr.webapp.GeomMinInventoryAction;

/**
* This class creates and retrieves Geomagnetic Inventory information for
* both the 1 minute and hourly data from the geomdb database.  You must
* specify which inventory you wish to use in the constructor.  Once the
* object is created, the inventory type my be changed with the
* <i>setInventory</i> method, but it is recommended to create a separate
* object for the minute and hourly inventory.  The inventory is displayed
* in a matrix with the values set to the maximum number of non-missing
* observations for any one of the possible elements, or as the number of
* days when displayed by year.  The information can be displayed in a
* table in the following forms:
* <ul>
* <li>stations by years for all stations and years
* <li>stations by years for all stations and years in a given date range
* <li>stations by months for a given year
* <li>stations by days for a given year and month
* <li>years by months for a given station
* <li>months by days for a given station and year
* </ul>
* <p>
* The inventory is kept in a Vector, <i>xValues</i>, and is displayed by
* observation or day count as described above. Each element of the Vector
* contains an array of the data values.  This setup represents a 2
* dimensional matrix: xValues[Nobs/Ndays][Nelem]
* <ul>
* Where:
* <li>Nobs...... number of observations for the respective time period
* <li>Ndays..... number of days data exists for the respective years
* <li>Nelem..... number of elements for each row of data
* </ul>
* After instantiating the Inventory object you can update the 1 minute
* or hourly inventory table with one of the <i>set</i> methods.  These
* give you the option to recreate the inventory table for a given year,
* for a given year and month, or for all years and months,  Updating the
* 1 minute inventory for all years is not recommended because of the
* size and number of all the minute tables.  The <i>updateStats</i>
* method will print summary statistics for the inventory update done
* with the <i>set</i> methods.
* <p>
* <b>NOTE:</b>  The inventory table in the database is not updated as new
* data is loaded so the set methods need to be run after data is loaded to
* keep it up to date.
* <p>
* The <i>get</i> methods allow you to retrieve the inventory into the
* inventory matrix for any of the reports described above.  The <i>print</i>
* method will print the inventory retrieved with the <i>get</i> method.
* Accessor methods are available to make the results from the <i>get</i>
* methods easy to use,
* <p>
* <b>NOTE:</b>  All of the data stored in the Vectors is stored as
* a <b>String</b> to make it easy to display.
*/
public class GeomInventory {

    /**
    * A Vector containing the data for the left most column along
    * the y-axis.  For example, when displaying stations by month
    * for a given year, this Vector would contain the list of
    * stations.  All elements are stored as a <i>String</i>.
    */
    public Vector yValues;

    /**
    * A Vector of Inventory data arrays.  Each element contains an
    * array of the inventory data as the number of observations for
    * a month or day, or as the number of days data exists for a
    * year.  For example, when displaying stations by month for a
    * given year, this Vector would contain an array of the number
    * of observations per month for each station.  All elements in
    * the arrays are stored as a <i>String</i>.
    */
    public Vector xValues;

    /**
    * The header for the first column of the report representing
    * the variables passed.  For example, it the year <i>1993</i>
    * is passed to retrieve information for a stations by month
    * matrix, <i>yHeader</i> would be <i>Yr=1993</i>.
    */
    public String yHeader;

    /**
    * The heading for the first row describing each row of the report.
    * If the inventory is being displayed for each month, this array
    * would contain month names for each column.
    */
    public String[] xHeader;

    /**
    * Date range of report.  These variables are set automatically
    * to provide a way to display the date range for any of the
    * reports run.  These <i>DateTime</i> objects can be retrieved
    * with their accessor methods in order to display a report.
    */
    public DateTime fromDT, toDT;

    /**
    * Either set to <i>true</i> or <i>false</i> automatically
    * by the constructor.
    */
    public boolean MINUTE;

    int NtmpDelete;

    int NtmpInsert;

    int NinvDelete;

    int NinvInsert;

    boolean accumStats;

    Statement stmt;

    DBConnect con;

    int getFormat;

    static final int byYear = 0;

    static final int byMonth = 1;

    static final int byDay = 2;

    /**
   * Saves the DB connection info and initializes the xValues and
   * yValues Vectors.  It is required to specify which <i>type</i>
   * of inventory you wish to use.  A valid arguments is one of
   * two Strings: "minute" or "hourly".  The default is set to
   * hourly.
   * @param type The type of inventory, "minute" or "hourly".
   * @param c JDBC Connection object for searching
   */
    public GeomInventory(String type, DBConnect c) {
        con = c;
        stmt = con.getStatement();
        if (type.equals("minute")) MINUTE = true; else MINUTE = false;
        yValues = new Vector();
        xValues = new Vector();
        accumStats = false;
    }

    /**
   * This method can be used to change the type of inventory in use.
   * A valid arguments is one of two Strings: "minute" or "hourly".
   * @param type The type of inventory, "minute" or "hourly".
   */
    public void setInventory(String type) {
        if (type.equals("minute")) MINUTE = true; else MINUTE = false;
    }

    /**
     * Get the <i>yValues</i> Vector.
     */
    public Vector getYValues() {
        return yValues;
    }

    /**
     * Get the <i>xValues</i> Vector.
     */
    public Vector getXValues() {
        return xValues;
    }

    /**
     * Get the from date.
     */
    public DateTime getFromDT() {
        return fromDT;
    }

    /**
     * Get the to date.
     */
    public DateTime getToDT() {
        return toDT;
    }

    /**
     * Get the Y-axis header.
     */
    public String getYHeader() {
        return yHeader;
    }

    /**
     * Get the X-axis header array.
     */
    public String[] getXHeader() {
        return xHeader;
    }

    /**
     * Get the data type.
     */
    public String getSetting() {
        if (MINUTE) return "minute"; else return "hourly";
    }

    /**
   * Create the inventory for all years and stations in the database.
   * Makes ready a <pre>station x year</pre> matrix.
   */
    public void get() {
        xValues.removeAllElements();
        yValues.removeAllElements();
        GeomDB gdb = new GeomDB(con);
        int fyear, tyear;
        if (MINUTE) {
            Vector minuteTables = gdb.getMinuteTables();
            String begTab = (String) minuteTables.firstElement();
            String endTab = (String) minuteTables.lastElement();
            begTab = begTab.substring(begTab.length() - 4, begTab.length());
            endTab = endTab.substring(endTab.length() - 4, endTab.length());
            fyear = Integer.parseInt(begTab);
            tyear = Integer.parseInt(endTab);
        } else {
            Vector years = gdb.getHourlyYears();
            fyear = Integer.parseInt((String) years.firstElement());
            tyear = Integer.parseInt((String) years.lastElement());
        }
        DateTime fdt = new DateTime(fyear, 1, 1);
        DateTime tdt = new DateTime(tyear, 12, 31);
        get(fdt, tdt);
    }

    /**
   * Create the inventory for a given date range.
   * Makes ready a <pre>station x year</pre> matrix.  The search is
   * inclusive of it's end points.
   * @param fDT The beginning date range to search for.
   * @param tDT The ending date range to search for.
   */
    public void get(DateTime fDT, DateTime tDT) {
        xValues.removeAllElements();
        yValues.removeAllElements();
        String[] maxObs;
        String maxOb;
        short mo;
        String stn;
        fromDT = fDT;
        toDT = tDT;
        int baseYear = Integer.parseInt(fromDT.toCustomString("yyyy"));
        boolean foundStn;
        String sql;
        getFormat = byYear;
        int Nelem = fromDT.timeBetween(Calendar.YEAR, toDT);
        xHeader = new String[Nelem];
        for (int i = 0; i < Nelem; i++) xHeader[i] = String.valueOf(baseYear + i);
        String fyear = (fromDT.toCustomString("yyyy"));
        String tyear = (toDT.toCustomString("yyyy"));
        yHeader = fyear.substring(fyear.length() - 2, fyear.length()) + "->" + tyear.substring(tyear.length() - 2, tyear.length());
        maxObs = new String[Nelem];
        sql = "SELECT stn,yr,SUM(maxObs) AS max";
        if (MINUTE) sql += " FROM inventoryMin"; else sql += " FROM inventoryHrly";
        sql += " WHERE yr>=" + fyear + " AND yr<=" + tyear;
        sql += " GROUP BY stn,yr";
        sql += " ORDER BY stn,yr";
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                stn = rs.getString("stn");
                int y = rs.getInt("yr");
                maxOb = rs.getString("max");
                foundStn = false;
                for (int i = 0; i < yValues.size(); i++) {
                    if (stn.equals((String) yValues.elementAt(i))) {
                        maxObs = (String[]) xValues.elementAt(i);
                        maxObs[y - baseYear] = maxOb;
                        xValues.setElementAt(maxObs, i);
                        foundStn = true;
                        break;
                    }
                }
                if (!foundStn) {
                    yValues.addElement(stn);
                    maxObs = new String[Nelem];
                    for (int n = 0; n < Nelem; n++) maxObs[n] = "0";
                    maxObs[y - baseYear] = maxOb;
                    xValues.addElement(maxObs);
                }
            }
            rs.close();
        } catch (SQLException e) {
            blowup(e);
        }
    }

    /**
   * Create the inventory for a given year.
   * Makes ready a <pre>station x month</pre> matrix.
   * @param yr the year to search for
   */
    public void get(int yr) {
        xValues.removeAllElements();
        yValues.removeAllElements();
        String[] maxObs;
        String maxOb;
        short mo;
        String stn;
        getFormat = byMonth;
        boolean foundStn;
        String sql;
        int Nelem = 12;
        String[] moNames = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
        xHeader = new String[Nelem];
        for (int i = 0; i < Nelem; i++) xHeader[i] = moNames[i];
        fromDT = new DateTime(yr, 1, 1);
        toDT = new DateTime(yr, 12, 31);
        yHeader = "Yr=" + yr;
        maxObs = new String[Nelem];
        sql = "SELECT stn,mo,maxObs";
        if (MINUTE) sql += " FROM inventoryMin"; else sql += " FROM inventoryHrly";
        sql += " WHERE yr=" + yr + " GROUP BY stn,mo";
        sql += " ORDER BY stn,mo";
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                stn = rs.getString("stn");
                int m = rs.getInt("mo") - 1;
                maxOb = rs.getString("maxObs");
                foundStn = false;
                for (int i = 0; i < yValues.size(); i++) {
                    if (stn.equals((String) yValues.elementAt(i))) {
                        maxObs = (String[]) xValues.elementAt(i);
                        maxObs[m] = maxOb;
                        xValues.setElementAt(maxObs, i);
                        foundStn = true;
                        break;
                    }
                }
                if (!foundStn) {
                    yValues.addElement(stn);
                    maxObs = new String[Nelem];
                    for (int n = 0; n < Nelem; n++) maxObs[n] = "0";
                    maxObs[m] = maxOb;
                    xValues.addElement(maxObs);
                }
            }
            rs.close();
        } catch (SQLException e) {
            blowup(e);
        }
    }

    /**
   * Create the inventory for a given year and month.
   * Makes ready a <pre>month x day</pre> matrix.
   * @param yr the year to search for
   * @param mo the month to search for
   */
    public void get(int yr, int mo) {
        xValues.removeAllElements();
        yValues.removeAllElements();
        String[] Nobs;
        String ob;
        int dy;
        String stn;
        boolean foundStn;
        String sql;
        getFormat = byDay;
        dbaccess.util.Format f2 = new dbaccess.util.Format("%2.2d");
        fromDT = new DateTime(yr, mo, 1);
        toDT = new DateTime(yr, mo, fromDT.daysInMonth());
        yHeader = yr + "." + f2.form(mo);
        int Nelem = fromDT.daysInMonth();
        xHeader = new String[Nelem];
        for (int m = 0; m < Nelem; m++) xHeader[m] = f2.form(m + 1);
        Nobs = new String[Nelem];
        sql = "SELECT stn,DAYOFMONTH(obsdate) AS dy,Nobs";
        if (MINUTE) sql += " FROM min" + yr; else sql += " FROM hourly";
        sql += " WHERE YEAR(obsdate)=" + yr + " AND MONTH(obsdate)=" + mo;
        sql += " GROUP BY stn,dy ORDER BY stn,dy";
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                stn = rs.getString("stn");
                int d = rs.getInt("dy") - 1;
                ob = rs.getString("Nobs");
                foundStn = false;
                for (int i = 0; i < yValues.size(); i++) {
                    if (stn.equals((String) yValues.elementAt(i))) {
                        Nobs = (String[]) xValues.elementAt(i);
                        Nobs[d] = ob;
                        xValues.setElementAt(Nobs, i);
                        foundStn = true;
                        break;
                    }
                }
                if (!foundStn) {
                    yValues.addElement(stn);
                    Nobs = new String[Nelem];
                    for (int n = 0; n < Nelem; n++) Nobs[n] = "0";
                    Nobs[d] = ob;
                    xValues.addElement(Nobs);
                }
            }
            rs.close();
        } catch (SQLException e) {
            blowup(e);
        }
    }

    /**
   * Create the inventory for a given station.
   * Makes ready a <pre>year x month</pre> matrix.
   * @param stn the station to search for
   */
    public void get(String stn) {
        xValues.removeAllElements();
        yValues.removeAllElements();
        String[] maxObs;
        String maxOb;
        String yr;
        short mo;
        getFormat = byMonth;
        boolean foundYear;
        String sql;
        int Nelem = 12;
        int maxYr = 0, minYr = Integer.MAX_VALUE;
        String[] moNames = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
        xHeader = new String[Nelem];
        for (int i = 0; i < Nelem; i++) xHeader[i] = moNames[i];
        yHeader = "stn=" + stn;
        maxObs = new String[Nelem];
        sql = "SELECT yr,mo,maxObs";
        if (MINUTE) sql += " FROM inventoryMin"; else sql += " FROM inventoryHrly";
        sql += " WHERE stn='" + stn + "' GROUP BY yr,mo";
        sql += " ORDER BY yr,mo";
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                yr = rs.getString("yr");
                int m = rs.getInt("mo") - 1;
                maxOb = rs.getString("maxObs");
                int tmp = Integer.parseInt(yr);
                if (tmp > maxYr) maxYr = tmp;
                if (tmp < minYr) minYr = tmp;
                foundYear = false;
                for (int i = 0; i < yValues.size(); i++) {
                    if (yr.equals((String) yValues.elementAt(i))) {
                        maxObs = (String[]) xValues.elementAt(i);
                        maxObs[m] = maxOb;
                        xValues.setElementAt(maxObs, i);
                        foundYear = true;
                        break;
                    }
                }
                if (!foundYear) {
                    yValues.addElement(yr);
                    maxObs = new String[Nelem];
                    for (int n = 0; n < Nelem; n++) maxObs[n] = "0";
                    maxObs[m] = maxOb;
                    xValues.addElement(maxObs);
                }
            }
            rs.close();
            fromDT = new DateTime(minYr, 1, 1);
            toDT = new DateTime(maxYr, 12, 31);
        } catch (SQLException e) {
            blowup(e);
        }
    }

    /**
   * Create the inventory for a given station and year.
   * Makes ready a <pre>month x day</pre> matrix.
   * @param stn the station to search for
   * @param yr the year to search for
   */
    public void get(String stn, int yr) {
        xValues.removeAllElements();
        yValues.removeAllElements();
        String[] Nobs;
        String ob;
        int mo;
        getFormat = byDay;
        boolean foundStn;
        String sql;
        dbaccess.util.Format f2 = new dbaccess.util.Format("%2.2d");
        String[] moNames = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
        fromDT = new DateTime(yr, 1, 1);
        toDT = new DateTime(yr, 12, 31);
        yHeader = stn + yr;
        int Nelem = toDT.get(Calendar.DAY_OF_MONTH);
        xHeader = new String[Nelem];
        for (int m = 0; m < Nelem; m++) xHeader[m] = f2.form(m + 1);
        Nobs = new String[Nelem];
        sql = "SELECT MONTH(obsdate) AS mo, DAYOFMONTH(obsdate) AS dy, SUM(Nobs) AS Nobs";
        if (MINUTE) sql += " FROM min" + yr; else sql += " FROM hourly";
        sql += " WHERE YEAR(obsdate)=" + yr + " AND stn='" + stn + "'";
        sql += " GROUP BY mo,dy ORDER BY mo,dy";
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                mo = rs.getInt("mo");
                int d = rs.getInt("dy") - 1;
                ob = rs.getString("Nobs");
                foundStn = false;
                for (int i = 0; i < yValues.size(); i++) {
                    if (moNames[mo - 1].equals((String) yValues.elementAt(i))) {
                        Nobs = (String[]) xValues.elementAt(i);
                        Nobs[d] = ob;
                        xValues.setElementAt(Nobs, i);
                        foundStn = true;
                        break;
                    }
                }
                if (!foundStn) {
                    yValues.addElement(moNames[mo - 1]);
                    Nobs = new String[Nelem];
                    for (int n = 0; n < Nelem; n++) Nobs[n] = "0";
                    Nobs[d] = ob;
                    xValues.addElement(Nobs);
                }
            }
            rs.close();
        } catch (SQLException e) {
            blowup(e);
        }
    }

    /**
   * Print the inventory depending on which format was requested.
   * When printing by year, data spanning more than a decade will
   * be printed out one decade at a time for each station.
   * To display the matrix without using the print method, the
   * following may be done:
   * <ol>
   * <li>Print out the date range using their accessor methods.
   * <li>Print out the yHeader using <i>getYHeader()</i>.
   * <li>Print out the x-axis headings in a <i>for</i> loop
   *     using the <i>getXHeader()</i> method.
   * <li>Begin a nested <i>for</i> loop displaying each <i>yValues</i>
   *     element.  The Vector can be retreived using the
   *     <i>getYValues()</i> method.
   * <li>In a second <i>for</i> loop display the inventory data in the
   *     <i>xValues</i> Vector for each column.  The number of columns
   *     can be calculated by taking the length of <i>xHeader</i>.
   *     The Vector can be retrieved using the <i>getXValues()</i>
   *     method.
   * </ol>
   * @see #getXHeader
   * @see #getYHeader
   * @see #getYValues
   * @see #getXValues
   */
    public void print() {
        switch(getFormat) {
            case 0:
                int Nelem = fromDT.timeBetween(Calendar.YEAR, toDT);
                if (Nelem <= 10) {
                    printByYear();
                    break;
                }
                int baseyear = fromDT.get(Calendar.YEAR);
                int decades = (Nelem + 9) / 10;
                for (int i = 0; i < decades; i++) {
                    DateTime fdt = new DateTime(baseyear + (i * 10), 1, 1);
                    DateTime tdt = new DateTime(baseyear + ((i + 1) * 10) - 1, 12, 31);
                    if (tdt.get(Calendar.YEAR) > toDT.get(Calendar.YEAR)) tdt = toDT;
                    GeomInventory inv;
                    if (MINUTE) inv = new GeomInventory("minute", con); else inv = new GeomInventory("hourly", con);
                    inv.get(fdt, tdt);
                    inv.print();
                }
                break;
            case 1:
                printByMonth();
                break;
            case 2:
                printByDay();
                break;
        }
    }

    private void printByYear() {
        int slen = yValues.size();
        int Nelem = xHeader.length;
        int y, x;
        String maxObs[];
        dbaccess.util.Format f6 = new dbaccess.util.Format(" %6s");
        dbaccess.util.Format f4 = new dbaccess.util.Format("%-4s");
        dbaccess.util.Format f7 = new dbaccess.util.Format("%-7s");
        System.out.println("\n          Number of Observations ");
        System.out.print("         " + fromDT.toCustomString("MM-dd-yyyy") + " -> ");
        System.out.println(toDT.toCustomString("MM-dd-yyyy") + "\n");
        System.out.print(f7.form(yHeader));
        for (int j = 0; j < Nelem; j++) System.out.print("   " + xHeader[j]);
        System.out.println();
        for (y = 0; y < slen; y++) {
            System.out.print(f4.form((String) yValues.elementAt(y)) + "   ");
            maxObs = (String[]) xValues.elementAt(y);
            for (x = 0; x < Nelem; x++) {
                System.out.print(f6.form(maxObs[x]));
            }
            System.out.println();
        }
    }

    private void printByMonth() {
        int slen = yValues.size();
        int Nelem = xHeader.length;
        String maxObs[];
        dbaccess.util.Format f5 = new dbaccess.util.Format(" %5s");
        dbaccess.util.Format f4 = new dbaccess.util.Format("%-4s");
        dbaccess.util.Format f7 = new dbaccess.util.Format("%-7s");
        System.out.println("\n          Number of Observations ");
        System.out.print("         " + fromDT.toCustomString("MM-dd-yyyy") + " -> ");
        System.out.println(toDT.toCustomString("MM-dd-yyyy") + "\n");
        System.out.print(f7.form(yHeader));
        for (int x = 0; x < Nelem; x++) System.out.print("   " + xHeader[x]);
        System.out.println();
        for (int y = 0; y < slen; y++) {
            System.out.print(f4.form((String) yValues.elementAt(y)) + "   ");
            maxObs = (String[]) xValues.elementAt(y);
            for (int x = 0; x < Nelem; x++) {
                System.out.print(f5.form(maxObs[x]));
            }
            System.out.println();
        }
    }

    private void printByDay() {
        int slen = yValues.size();
        int Nelem = xHeader.length;
        String[] Nobs;
        dbaccess.util.Format f7 = new dbaccess.util.Format("%-7s");
        dbaccess.util.Format f4 = new dbaccess.util.Format(" %4s");
        dbaccess.util.Format f2 = new dbaccess.util.Format("%2.2d");
        System.out.println("\n          Number of Observations ");
        System.out.print("         " + fromDT.toCustomString("MM-dd-yyyy") + " -> ");
        System.out.println(toDT.toCustomString("MM-dd-yyyy") + "\n");
        System.out.print(f7.form(yHeader) + "    ");
        for (int x = 0; x < 10; x++) System.out.print("   " + f2.form(x + 1));
        System.out.println();
        for (int y = 0; y < slen; y++) {
            System.out.print((String) yValues.elementAt(y));
            System.out.print("\n    [1-10] ");
            Nobs = (String[]) xValues.elementAt(y);
            for (int x = 0; x < Nelem; x++) {
                if (x == 10) System.out.print("\n   [11-20] ");
                if (x == 20) System.out.print("\n   [21-" + Nelem + "] ");
                System.out.print(f4.form(Nobs[x]));
            }
            System.out.println();
        }
    }

    /**
   * Create the inventory for the entire database.
   * @param yr Update the minute inventory for this entire year
   * @return The number of inventory rows inserted
   */
    public int set() {
        System.out.print("In set");
        int rows = 0;
        NtmpDelete = 0;
        NtmpInsert = 0;
        NinvDelete = 0;
        NinvInsert = 0;
        accumStats = true;
        if (MINUTE) {
            GeomDB gdb = new GeomDB(con);
            Vector minTabs = gdb.getMinuteTables();
            for (int i = 0; i < minTabs.size(); i++) {
                String tab = (String) minTabs.elementAt(i);
                int year = Integer.parseInt(tab.substring(tab.length() - 4, tab.length()));
                rows += set(year, 0);
            }
        } else rows = set(0, 0);
        accumStats = false;
        return rows;
    }

    /**
   * Create the inventory for a given year.
   * @param yr Update the minute inventory for this entire year
   * @return The number of inventory rows inserted
   */
    public int set(int yr) {
        return set(yr, 0);
    }

    /**
   * Create the inventory for a given year and month.
   * @param yr Update the inventory for this year
   * @param mo Update the inventory for this month only
   * @return The number of inventory rows inserted
   */
    public int set(int yr, int mo) {
        String sql;
        if (!accumStats) {
            NtmpDelete = 0;
            NtmpInsert = 0;
            NinvDelete = 0;
            NinvInsert = 0;
        }
        try {
            sql = "DELETE FROM invtmp";
            NtmpDelete += stmt.executeUpdate(sql);
            if (MINUTE) sql = "DELETE FROM inventoryMin"; else sql = "DELETE FROM inventoryHrly";
            if (yr + mo > 0) sql += " WHERE";
            if (yr > 0) sql += " yr=" + yr;
            if (yr > 0 && mo > 0) sql += " AND";
            if (mo > 0) sql += " mo=" + mo;
            NinvDelete += stmt.executeUpdate(sql);
            System.out.println("Calculate new inventory for the yr/mo: " + yr + "/" + mo);
            sql = "INSERT INTO invtmp(stn,yr,mo,element,sumObs)";
            sql += " SELECT DISTINCT stn, YEAR(obsdate) AS yr,";
            sql += " MONTH(obsdate) AS mo, element, SUM(Nobs)";
            if (MINUTE) sql += " FROM min" + yr; else sql += " FROM hourly";
            if (yr + mo > 0) sql += " WHERE flag=1 AND";
            if (yr > 0) sql += " YEAR(obsdate)=" + yr;
            if (yr > 0 && mo > 0) sql += " AND";
            if (mo > 0) sql += " MONTH(obsdate)=" + mo;
            sql += " GROUP BY stn, yr, mo, element";
            sql += " ORDER BY stn, yr, mo, element";
            System.out.println("SQL = " + sql);
            NtmpInsert += stmt.executeUpdate(sql);
            if (MINUTE) sql = "INSERT INTO inventoryMin(stn,yr,mo,maxObs)"; else sql = "INSERT INTO inventoryHrly(stn,yr,mo,maxObs)";
            sql += " SELECT stn,yr,mo,MAX(sumObs)";
            sql += " FROM invtmp GROUP BY stn,yr,mo";
            NinvInsert += stmt.executeUpdate(sql);
        } catch (SQLException e) {
            blowup(e);
        }
        return NinvInsert;
    }

    /**
   * Display update statistics
   */
    public void updateStats() {
        System.out.println("\nNumber invtmp:");
        System.out.println("   Deleted...." + NtmpDelete);
        System.out.println("   Inserted..." + NtmpInsert);
        System.out.print("Number inventory ");
        if (MINUTE) System.out.println("minute:"); else System.out.println("hourly");
        System.out.println("   Deleted...." + NinvDelete);
        System.out.println("   Inserted..." + NinvInsert);
    }

    /**
   * Generic error handler
   */
    private void blowup(SQLException e) {
        System.out.println("\nSQLException: " + e.getMessage());
        System.out.println(" *** " + e.getSQLState());
        e.printStackTrace();
    }
}
