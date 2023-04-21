package sol.admin.systemmanagement.base.host;

import java.sql.*;
import java.util.*;
import sol.admin.systemmanagement.db.*;
import sol.admin.systemmanagement.util.*;

/**
 * This class is responsible for managing all database requests
 * concerning the Host class.
 * You can only use the methods implemented here by using
 * the HostDatabaseObjectManager.
 * @author Markus Hammori
 */
public abstract class HostDatabaseManager extends DatabaseManager {

    protected HostDatabaseManager() {
        super();
    }

    public int getFreeIPCount() throws SQLException {
        Statement stmt = _connectionManager.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery("select count(ip) as count from ips_free");
        while (rs.next()) {
            return rs.getInt("count");
        }
        return 0;
    }

    public int getAllIPCount() throws SQLException {
        Statement stmt = _connectionManager.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery("select count(ip) as count from ip");
        while (rs.next()) {
            return rs.getInt("count");
        }
        return 0;
    }

    public int getTakenIPCount() throws SQLException {
        Statement stmt = _connectionManager.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery("select count(distinct ip) as count from hosts");
        while (rs.next()) {
            return rs.getInt("count");
        }
        return 0;
    }

    public ArrayList getFreeIPArrayList() throws SQLException {
        ArrayList neuListe = new ArrayList();
        String freiIP = "";
        Statement stmt = _connectionManager.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery("select ip from ips_free order by host(ip)");
        while (rs.next()) {
            neuListe.add(rs.getString("ip"));
        }
        return neuListe;
    }

    public String checkIP(String ip) throws SQLException {
        if (getFreeIPCount() > 0) {
            Statement stmt = _connectionManager.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("select ip from ip where ip='" + ip + "'");
            if (!rs.next()) {
                return "IP not in the system";
            }
            stmt = _connectionManager.getConnection().createStatement();
            rs = stmt.executeQuery("select ip from ips_free where ip='" + ip + "'");
            if (rs.next()) {
                return null;
            }
            return "IP already taken";
        }
        return "no more free IPs";
    }

    public int countUsersForIP(String ip) throws SQLException {
        int count = 0;
        Statement stmt = _connectionManager.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery("select count(uid) as count from hosts where ip='" + ip + "'");
        while (rs.next()) {
            count = rs.getInt("count");
        }
        return count;
    }

    public int countUsersForHostName(String hostname) throws SQLException {
        int count = 0;
        Statement stmt = _connectionManager.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery("select count(uid) as count from hosts where lower(hostname)='" + hostname.toLowerCase() + "'");
        while (rs.next()) {
            count = rs.getInt("count");
        }
        return count;
    }

    public int countOccuranceOfHostnameInDatabase(String hostname) throws SQLException {
        int count = 0;
        String qString = "select count(hostname) as count from hosts where hostname = '" + hostname + "'";
        Statement stmt = _connectionManager.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(qString);
        while (rs.next()) {
            count = rs.getInt("count");
        }
        return count;
    }

    public int countUsersForMAC(String mac) throws SQLException {
        int count = 0;
        String qString = "select count(b.uid) as count from hosts as h JOIN benutzer " + "as b ON h.uid=b.uid where deleted=false and mac='" + mac + "'";
        Logger.getLogger().log("UserDatabaseManager", "countUsersForMAC Query:" + qString, Logger.DEBUG);
        Statement stmt = getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(qString);
        while (rs.next()) {
            count = rs.getInt("count");
        }
        return count;
    }

    public ArrayList getHostsForUID(String uid) throws SQLException {
        String qString = "select ip, hostname, mac, os from hosts where uid = '" + uid + "'";
        Statement stmt = getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(qString);
        ArrayList userHosts = new ArrayList();
        while (rs.next()) {
            Host temp = new Host(uid, rs.getString("ip"), new MAC(rs.getString("mac")), rs.getString("hostname"), new OperatingSystem(rs.getInt("os")));
            userHosts.add(temp);
        }
        stmt.close();
        return userHosts;
    }
}
