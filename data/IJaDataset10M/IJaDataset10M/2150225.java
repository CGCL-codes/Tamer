package amint.amintd;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.logging.Logger;
import java.sql.*;
import javax.sql.rowset.serial.SerialBlob;
import amint.amintd.document.*;
import amint.util.*;

public class MintServices {

    private Map<String, URL> map;

    private Conn conn;

    private MintInfo info;

    MintServices(MintInfo info) throws Exception {
        this.conn = info.getConn();
        this.info = info;
        map = new HashMap<String, URL>();
        String query = "select service_type,service_url from mint_services where mint_id = ?";
        PreparedStatement pstmt = conn.conn.prepareStatement(query);
        pstmt.setInt(1, info.getMintId());
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            String st = rs.getString(1);
            String su = rs.getString(2);
            URL url = new URL(su);
            map.put(st, url);
        }
    }

    public Set<Map.Entry<String, URL>> getAllServices() {
        return map.entrySet();
    }

    public URL getURL(String serviceType) {
        return map.get(serviceType);
    }

    public static boolean isSupported(String serviceType) {
        if (serviceType.equals("WEB_EXCHANGE")) return true;
        if (serviceType.equals("WEB_INFO")) return true;
        return false;
    }

    private boolean remove(String serviceType) {
        map.remove(serviceType);
        try {
            String query = "Delete from mint_services where mint_id = ? and service_type = ?";
            PreparedStatement pstmt = conn.conn.prepareStatement(query);
            pstmt.setInt(1, info.getMintId());
            pstmt.setString(2, serviceType);
            pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            conn.addError(e.toString());
            return false;
        }
    }

    boolean reset() {
        map = new HashMap<String, URL>();
        try {
            String query = "Delete from mint_services where mint_id = ?";
            PreparedStatement pstmt = conn.conn.prepareStatement(query);
            pstmt.setInt(1, info.getMintId());
            pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            conn.addError(e.toString());
            return false;
        }
    }

    public boolean setUrl(String serviceType, URL url) {
        this.remove(serviceType);
        map.put(serviceType, url);
        try {
            String query = "insert into mint_services (mint_id,service_type,service_url) values (?,?,?)";
            PreparedStatement pstmt = conn.conn.prepareStatement(query);
            pstmt.setInt(1, info.getMintId());
            pstmt.setString(2, serviceType);
            pstmt.setString(3, url.toString());
            pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            conn.addError(e.toString());
            return false;
        }
    }

    public static StringTable getStringTable(Conn conn) {
        String[] headers = { "nick", "service_type", "service_url" };
        StringTable st = null;
        try {
            st = new StringTable(headers);
            String query = "select nick, service_type,service_url from mints, mint_services " + "where mints.mint_id = mint_services.mint_id order by nick, service_type";
            PreparedStatement pstmt = conn.conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String nick = new String(rs.getString(1));
                String serviceType = new String(rs.getString(2));
                String url = new String(rs.getString(3));
                st.addRow(nick + "\t" + serviceType + "\t" + url);
            }
            return st;
        } catch (Exception e) {
            e.printStackTrace();
            conn.addError(e.toString());
            return st;
        }
    }
}
