package dbaccess.iono;

import java.sql.*;
import java.util.*;
import java.io.*;
import dbaccess.util.*;
import org.apache.log4j.Logger;

/** * This class retrieves station information from the ionospheric database. * <p> * The <i>constructor</i> simply creates the object and stores the connection * information.  Use the <i>set</i> methods to set search criteria and then * use the <i>get()</i> method to search for station information in the * database.  Use the <i>getClosestStation()</i> method to find the station * closest to a lat/lon point with data on the date specified.  The <i>get</i> * accessor methods provide access to the station information.  Use the * <i>isFound()</i> method to determine if the information was found or not. * <p> * Use the <i>print()</i> and <i>display()</i> methods to print station * information to stdout or to display it to an AWT text area, respectively. * <p> * The <i>printHdr()</i> and <i>displayHdr()</i> print or display a * message telling whether or not the station info was found. */
public class Station {

    DBConnect con;

    Statement stmt;

    boolean found;

    /** Station code (ursi) */
    protected String stn = "";

    /** Station id (iuwds) */
    protected String iuwdsID = "";

    /** Station id (UML) */
    protected String umlID = "";

    /** Station id (WMO) */
    protected String wmoID = "";

    /** Station name */
    protected String name = "";

    /** Lat/lon location of station */
    protected LatLon ll;

    /** Meridian time */
    protected byte meridianTime;

    /** Gyrofrequency */
    protected float gyroFrequency;

    /** Dip angle */
    protected float dipAngle;

    /** Declination */
    protected float declination;

    /** Ionosonde ID */
    protected byte ionosondeID;

    /** Ionosonde */
    protected String ionosonde = "";

    /** Nantenna */
    protected byte Nantenna;

    /** Antenna Position */
    protected float[][] antPosition;

    /** Antenna Layout */
    protected String antLayout = "";

    /** Antenna Rotation */
    protected String antRotation = "";

    private Logger _log = Logger.getLogger(dbaccess.iono.Station.class);

    /**   * Get information for a station   * @param c JDBC DBConnect object for access to the database   */
    public Station(DBConnect c) {
        con = c;
        stmt = c.getStatement();
        found = false;
    }

    /**   * Set the class variables from the database result set   * @param s JDBC Statement object for access to the database   * @param rs JDBC ResultSet object   */
    public Station(Statement s, ResultSet rs) {
        stmt = s;
        getStationData(rs);
        found = true;
    }

    /**   * Get station id (ursi)   * @return Station id code   */
    public String getStn() {
        return stn;
    }

    /**   * Get station id (iuwds)   * @return UML station id code   */
    public String getIuwdsID() {
        return iuwdsID;
    }

    /**   * Get station id (UML)   * @return UML station id code   */
    public String getUmlID() {
        return umlID;
    }

    /**   * Get station id (WMO)   * @return WMO station id code   */
    public String getWmoID() {
        return wmoID;
    }

    /**   * Get station name   * @return Station name   */
    public String getName() {
        return name;
    }

    /**   * Get station latitude   * @return Station latitude   */
    public float getLat() {
        return ll.getLat();
    }

    /**   * Get station longitude   * @return Station longitude   */
    public float getLon() {
        return ll.getLon();
    }

    /**   * Get Meridian Time   * @return MeridianTime   */
    public byte getMeridianTime() {
        return meridianTime;
    }

    /**   * Get GyroFrequency   * @return GyroFrequency   */
    public float getGyroFrequency() {
        return gyroFrequency;
    }

    /**   * Get dip angle   * @return Dip Angle   */
    public float getDipAngle() {
        return dipAngle;
    }

    /**   * Get Declination   * @return Declination   */
    public float getDeclination() {
        return declination;
    }

    /**   * Get ionosonde ID   * @return Ionosonde ID   */
    public byte getIonosondeID() {
        return ionosondeID;
    }

    /**   * Get ionosonde description   * @return Ionosonde description   */
    public String getIonosonde() {
        return ionosonde;
    }

    /**   * Get Nantenna   * @return Number of antenna   */
    public byte getNantenna() {
        return Nantenna;
    }

    /**   * Get Antenna Position   * @return Antenna Position   */
    public float[][] getAntPosition() {
        return antPosition;
    }

    /**   * Get Antenna Layout   * @return Antenna Layout   */
    public String getAntLayout() {
        return antLayout;
    }

    /**   * Get Antenna Rotation   * @return Antenna Rotation   */
    public String getAntRotation() {
        return antRotation;
    }

    /**   * Set station (URSI) for searching   * @param station Station to search for.  If <i>null</i> then search   * for all stations.   */
    public void setStn(String station) {
        stn = station;
    }

    /**   * Set station (IUWDS ID) for searching   * @param iuwdsID Station ID   */
    public void setIuwdsID(String iuwdsID) {
        this.iuwdsID = iuwdsID;
    }

    /**   * Set station (UML ID) for searching   * @param uml Station ID   */
    public void setUmlID(String uml) {
        umlID = uml;
    }

    /**   * Set station (WMO ID) for searching   * @param wmo WMO Station ID.   */
    public void setWmoID(String wmo) {
        wmoID = wmo;
    }

    /**   * Set station name   * @param stnName Station name.   */
    public void setName(String stnName) {
        name = stnName;
    }

    /**   * Set lat/lon for searching   * @param latlon Latitude/Longitude   */
    public void setLatLon(LatLon latlon) {
        ll = latlon;
    }

    /**   * Set meridian time   * @param mt Meridian time   */
    public void setMeridianTime(byte mt) {
        meridianTime = mt;
    }

    /**   * Set gyrofrequency   * @param gf Gyrofrequency   */
    public void setGyroFrequency(float gf) {
        gyroFrequency = gf;
    }

    /**   * Set dip angle   * @param dip Dip angle   */
    public void setDipAngle(float dip) {
        dipAngle = dip;
    }

    /**   * Set declination   * @param decl declination   */
    public void setDeclination(float decl) {
        declination = decl;
    }

    /**   * Set ionosonde   * @param ionosondeID ionosonde   */
    public void setIonosondeID(byte iosID) {
        ionosondeID = iosID;
    }

    /**   * Set ionosonde description   * @param ionosonde Ionosonde   */
    public void setIonosonde(String ios) {
        ionosonde = ios;
    }

    /**   * Set number of antenna   * @param Nant Number of antenna   */
    public void setNantenna(byte Nant) {
        Nantenna = Nant;
    }

    /**   * Set antenna position   * @param antpos Antenna position   */
    public void setAntPosition(float[][] antpos) {
        antPosition = antpos;
    }

    /**   * Set antenna layout   * @param antlay Antenna layout   */
    public void setAntLayout(String antlay) {
        antLayout = antlay;
    }

    /**   * Set antenna rotation   * @param antlay Antenna rotation   */
    public void setAntRotation(String antrot) {
        antRotation = antrot;
    }

    /**   * Get information for a station.   * This method expects that the station ID to search for has been   * previously set with the <i>setStn()</i> method.   * @return True if station was found; False otherwise   * @see Station#setStn   */
    public boolean get() {
        found = false;
        String query = "SELECT * FROM stations WHERE stn='" + stn + "'";
        try {
            ResultSet rs = stmt.executeQuery(query);
            int count = 0;
            while (rs.next()) {
                found = true;
                getStationData(rs);
                count++;
            }
            rs.close();
            if (count == 0) {
                _log.info("Given station code: " + stn + " did not return any results from the stations table");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return found;
    }

    /**   * Get information for a station searching using only the station suffix.   * @return True if station was found; False otherwise   * @see Station#setStn   */
    public boolean getSuffix(String suffix) {
        found = false;
        String query = "SELECT * FROM stations WHERE stn like '%" + suffix + "'";
        try {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                found = true;
                getStationData(rs);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return found;
    }

    /**   * Get information for a station by SAO UML code.   * This method expects that the UML station ID to search for has been   * previously been set with the <i>setStn()</i> method.   * @return True if station was found; False otherwise   * @see Station#setStn   */
    public boolean getUML() {
        found = false;
        String query = "SELECT * FROM stations WHERE umlID='" + umlID + "'";
        try {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                found = true;
                getStationData(rs);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return found;
    }

    /**   * Get information for a station.   * @param stnid Station id   * @return True if station was found; False otherwise   * @see Station#get   */
    public boolean get(String stnid) {
        setStn(stnid);
        return get();
    }

    /**   * Get information for a station by UML code.   * @param stnid UML Station id   * @return True if station was found; False otherwise   * @see Station#get   */
    public boolean getUML(String stnid) {
        setUmlID(stnid);
        return getUML();
    }

    /**   * Get a stn code for a iuwds ID.   * @param iuwds IUWDS Station id   * @return the stn code   */
    public String convertIUWDS(String iuwds) {
        String station = null;
        String query = "SELECT stn FROM stations WHERE iuwdsID='" + iuwds + "'";
        try {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String stn = rs.getString("stn");
                if (!stn.equals("") && stn != null) {
                    station = stn;
                } else {
                    station = null;
                }
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return station;
    }

    /**   * Get information for a station that's closest to a lat/lon   * @param dttm Date/time - Only consider stations with data on this date/time   * @param ursicode Ursi code - Only consider stations with data for this ursi code   * @param latlon Lat/Lon to use for the closest station   * @return stn Station ID if station was found, null otherwise   */
    public String getClosestStation(String ursicode, DateTime dttm, LatLon latlon) {
        found = false;
        String dt = dttm.toCustomString("yyyy-MM-dd");
        int y = dttm.get(Calendar.YEAR);
        IonoDB idb = new IonoDB(con);
        String tab = idb.getDataTableName(y);
        String query = "SELECT distinct stations.* FROM stations," + tab;
        query += " WHERE ursi='" + ursicode + "'";
        query += " and obsdate='" + dt + "'";
        query += " and stations.stn=" + tab + ".stn";
        try {
            ResultSet rs = stmt.executeQuery(query);
            LatLonCompare lld = new LatLonCompare(latlon);
            while (rs.next()) {
                found = true;
                LatLon ll = new LatLon(rs.getFloat("lat"), rs.getFloat("lon"));
                if (lld.isCloser(ll)) {
                    getStationData(rs);
                }
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        if (found) {
            return stn;
        } else {
            return null;
        }
    }

    protected void getStationData(ResultSet rs) {
        byte[] blob;
        ByteArrayInputStream bais;
        DataInputStream dis;
        try {
            stn = rs.getString("stn");
            iuwdsID = rs.getString("iuwdsID");
            umlID = rs.getString("umlID");
            wmoID = rs.getString("wmoID");
            name = rs.getString("stnName");
            if (name == null) {
                name = "None";
            }
            ll = new LatLon(rs.getFloat("lat"), rs.getFloat("lon"));
            meridianTime = (byte) rs.getInt("meridianTime");
            gyroFrequency = rs.getFloat("gyroFrequency");
            dipAngle = rs.getFloat("dipAngle");
            declination = rs.getFloat("declination");
            ionosondeID = (byte) rs.getInt("ionosonde");
            Nantenna = (byte) rs.getInt("Nantenna");
            blob = (byte[]) rs.getBytes("antPosition");
            antPosition = new float[Nantenna][3];
            if (blob != null) {
                bais = new ByteArrayInputStream(blob);
                dis = new DataInputStream(bais);
                for (int i = 0; i < Nantenna; i++) {
                    antPosition[i][0] = dis.readFloat();
                    antPosition[i][1] = dis.readFloat();
                    antPosition[i][2] = dis.readFloat();
                }
            }
            antLayout = rs.getString("antLayout");
            antRotation = rs.getString("antRotation");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean isFound() {
        return found;
    }

    /**   * See if data exists for a station   * @param station stn code   * @return true if data exists, false if no data exists   */
    public boolean doesDataExist(String station) {
        IonoDB ionoDB = new IonoDB(con);
        int dataFound = 0;
        String[] tables = ionoDB.getDataTables();
        for (int i = 0; i < tables.length; i++) {
            String query = "SELECT count(*) AS count FROM " + tables[i] + " WHERE stn='" + station + "'";
            try {
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    dataFound = rs.getInt("count");
                    if (dataFound > 0) {
                        return true;
                    }
                }
                rs.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
        return false;
    }

    /**   * Convert the object into a string   */
    public String toString() {
        if (!found) {
            return "No station";
        }
        return stn + " - " + name;
    }

    /**   * Print the station information   */
    public void print() {
        Format f5 = new Format("%5.2f");
        if (!found) {
            System.out.println("\n*** Station not found");
            return;
        }
        System.out.println("\n    *** Station Info ***");
        System.out.println("Station=" + stn + " " + name);
        System.out.println("  UML ID:" + umlID + "  WMO ID: " + wmoID + " IUWDS ID: " + iuwdsID);
        System.out.println("  Lat,Lon:" + ll + "  Meridian Time: " + meridianTime);
        System.out.println("   Ionosonde=" + ionosondeID + " - " + ionosonde);
        System.out.print("   GyroFrequency=" + gyroFrequency);
        System.out.print("   Dip Angle=" + dipAngle);
        System.out.println("   Declination=" + declination);
        System.out.println(" Antenna Info:");
        System.out.println("   Nantenna=" + Nantenna);
        System.out.println("   Layout=" + antLayout);
        System.out.println("   Rotation=" + antRotation);
        System.out.println("   Position:");
        System.out.println("            X     Y     Z");
        for (int i = 0; i < Nantenna; i++) {
            System.out.print("   Ant" + (i + 1) + "  " + f5.form(antPosition[i][0]));
            System.out.print(" " + f5.form(antPosition[i][1]));
            System.out.println(" " + f5.form(antPosition[i][2]));
        }
        System.out.println("\n");
        System.out.flush();
    }

    /**   * printHdr()   * Print the Station information   */
    public void printHdr() {
        if (found) {
            System.out.println("Station found");
        } else {
            System.out.println("*** Station not found");
        }
    }

    /**   * Insert a Station into the ionodb database.   * Use the <i>setXXX()</i> methods to set the table column information   * in this object before calling this method.   * @return True if insert successful, else false   */
    public boolean insert() {
        PreparedStatement stmtInsert;
        try {
            String sql = "INSERT INTO stations VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            stmtInsert = con.getConnection().prepareStatement(sql);
        } catch (SQLException e) {
            System.out.println("***ERROR: Unable to create a prepared statement.");
            return false;
        }
        int x = 1;
        try {
            stmtInsert.setString(x, stn);
            stmtInsert.setString(++x, iuwdsID);
            stmtInsert.setString(++x, umlID);
            stmtInsert.setString(++x, wmoID);
            stmtInsert.setString(++x, name);
            stmtInsert.setFloat(++x, ll.getLat());
            stmtInsert.setFloat(++x, ll.getLon());
            stmtInsert.setInt(++x, (int) meridianTime);
            stmtInsert.setFloat(++x, gyroFrequency);
            stmtInsert.setFloat(++x, dipAngle);
            stmtInsert.setFloat(++x, declination);
            stmtInsert.setByte(++x, ionosondeID);
            stmtInsert.setInt(++x, (int) Nantenna);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            for (int i = 0; i < Nantenna; i++) {
                try {
                    dos.writeFloat(antPosition[i][0]);
                    dos.writeFloat(antPosition[i][1]);
                    dos.writeFloat(antPosition[i][2]);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                    return false;
                }
            }
            byte[] blob = baos.toByteArray();
            stmtInsert.setBytes(++x, blob);
            stmtInsert.setString(++x, antLayout);
            stmtInsert.setString(++x, antRotation);
            stmtInsert.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("   *** " + e.getSQLState());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**   * Update a Station's data in the ionodb database.   * Call the <i>get()</i> method to retrieve existing data for the station   * and set column information to current values.  Then use the   * <i>setXXX()</i> methods to override the table column information   * with new values.   * @return True if insert successful, else false   */
    public boolean update() {
        PreparedStatement stmtUpdate;
        try {
            String sql = "UPDATE stations ";
            sql += "set iuwdsID=?,umlID=?,wmoID=?,stnName=?,";
            sql += "lat=?,lon=?,meridianTime=?,";
            sql += "gyroFrequency=?,dipAngle=?,";
            sql += "declination=?,ionosonde=?,";
            sql += "Nantenna=?,antPosition=?,";
            sql += "antLayout=?,antRotation=?";
            sql += " WHERE stn=?";
            stmtUpdate = con.getConnection().prepareStatement(sql);
        } catch (SQLException e) {
            System.out.println("***ERROR: Unable to create a prepared statement.");
            return false;
        }
        int x = 1;
        try {
            stmtUpdate.setString(x, iuwdsID);
            stmtUpdate.setString(++x, umlID);
            stmtUpdate.setString(++x, wmoID);
            stmtUpdate.setString(++x, name);
            stmtUpdate.setFloat(++x, ll.getLat());
            stmtUpdate.setFloat(++x, ll.getLon());
            stmtUpdate.setInt(++x, (int) meridianTime);
            stmtUpdate.setFloat(++x, gyroFrequency);
            stmtUpdate.setFloat(++x, dipAngle);
            stmtUpdate.setFloat(++x, declination);
            stmtUpdate.setInt(++x, (int) ionosondeID);
            stmtUpdate.setInt(++x, (int) Nantenna);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            for (int i = 0; i < Nantenna; i++) {
                try {
                    dos.writeFloat(antPosition[i][0]);
                    dos.writeFloat(antPosition[i][1]);
                    dos.writeFloat(antPosition[i][2]);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                    return false;
                }
            }
            byte[] blob = baos.toByteArray();
            stmtUpdate.setBytes(++x, blob);
            stmtUpdate.setString(++x, antLayout);
            stmtUpdate.setString(++x, antRotation);
            stmtUpdate.setString(++x, stn);
            stmtUpdate.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("   *** " + e.getSQLState());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**   * Delete a Station's data from the ionodb database.   * Use the <i>setStn()</i> method to set the station to delete.   * @return True if insert successful, else false   */
    public boolean delete() {
        PreparedStatement stmtDelete;
        try {
            String sql = "DELETE FROM stations ";
            sql += " WHERE stn=?";
            stmtDelete = con.getConnection().prepareStatement(sql);
        } catch (SQLException e) {
            System.out.println("***ERROR: Unable to create a prepared statement.");
            return false;
        }
        try {
            stmtDelete.setString(1, stn);
            stmtDelete.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("   *** " + e.getSQLState());
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
