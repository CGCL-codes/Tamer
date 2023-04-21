package dbaccess.iono;

import java.sql.*;
import java.util.*;
import java.io.*;
import dbaccess.util.*;
import org.apache.log4j.Logger;

/**
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

    /**
    public Station(DBConnect c) {
        con = c;
        stmt = c.getStatement();
        found = false;
    }

    /**
    public Station(Statement s, ResultSet rs) {
        stmt = s;
        getStationData(rs);
        found = true;
    }

    /**
    public String getStn() {
        return stn;
    }

    /**
    public String getIuwdsID() {
        return iuwdsID;
    }

    /**
    public String getUmlID() {
        return umlID;
    }

    /**
    public String getWmoID() {
        return wmoID;
    }

    /**
    public String getName() {
        return name;
    }

    /**
    public float getLat() {
        return ll.getLat();
    }

    /**
    public float getLon() {
        return ll.getLon();
    }

    /**
    public byte getMeridianTime() {
        return meridianTime;
    }

    /**
    public float getGyroFrequency() {
        return gyroFrequency;
    }

    /**
    public float getDipAngle() {
        return dipAngle;
    }

    /**
    public float getDeclination() {
        return declination;
    }

    /**
    public byte getIonosondeID() {
        return ionosondeID;
    }

    /**
    public String getIonosonde() {
        return ionosonde;
    }

    /**
    public byte getNantenna() {
        return Nantenna;
    }

    /**
    public float[][] getAntPosition() {
        return antPosition;
    }

    /**
    public String getAntLayout() {
        return antLayout;
    }

    /**
    public String getAntRotation() {
        return antRotation;
    }

    /**
    public void setStn(String station) {
        stn = station;
    }

    /**
    public void setIuwdsID(String iuwdsID) {
        this.iuwdsID = iuwdsID;
    }

    /**
    public void setUmlID(String uml) {
        umlID = uml;
    }

    /**
    public void setWmoID(String wmo) {
        wmoID = wmo;
    }

    /**
    public void setName(String stnName) {
        name = stnName;
    }

    /**
    public void setLatLon(LatLon latlon) {
        ll = latlon;
    }

    /**
    public void setMeridianTime(byte mt) {
        meridianTime = mt;
    }

    /**
    public void setGyroFrequency(float gf) {
        gyroFrequency = gf;
    }

    /**
    public void setDipAngle(float dip) {
        dipAngle = dip;
    }

    /**
    public void setDeclination(float decl) {
        declination = decl;
    }

    /**
    public void setIonosondeID(byte iosID) {
        ionosondeID = iosID;
    }

    /**
    public void setIonosonde(String ios) {
        ionosonde = ios;
    }

    /**
    public void setNantenna(byte Nant) {
        Nantenna = Nant;
    }

    /**
    public void setAntPosition(float[][] antpos) {
        antPosition = antpos;
    }

    /**
    public void setAntLayout(String antlay) {
        antLayout = antlay;
    }

    /**
    public void setAntRotation(String antrot) {
        antRotation = antrot;
    }

    /**
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

    /**
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

    /**
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

    /**
    public boolean get(String stnid) {
        setStn(stnid);
        return get();
    }

    /**
    public boolean getUML(String stnid) {
        setUmlID(stnid);
        return getUML();
    }

    /**
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

    /**
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

    /**
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

    /**
    public String toString() {
        if (!found) {
            return "No station";
        }
        return stn + " - " + name;
    }

    /**
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

    /**
    public void printHdr() {
        if (found) {
            System.out.println("Station found");
        } else {
            System.out.println("*** Station not found");
        }
    }

    /**
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

    /**
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

    /**
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