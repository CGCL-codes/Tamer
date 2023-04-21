package adbus.libraries;

import adbus.AdBusApp;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *klasa odpowiedzialan za lacznosc z baza danych
 * @author rafal
 */
public class Database {

    private static Connection con = null;

    /**metoda odpowiedzialna za połaczenie sie z baza*/
    public static void connectDB() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            AdBusApp.getApplication().exit();
        }
        Properties p = new Properties();
        try {
            String path = Database.class.getProtectionDomain().getCodeSource().getLocation().toString();
            File jarFile = new File(Database.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
            File jarDir = jarFile.getParentFile();
            if (jarDir != null && jarDir.isDirectory()) {
                p.load(new FileInputStream(jarDir.toString() + "/db.props"));
            }
            String host = p.getProperty("host");
            String port = p.getProperty("port", "3306");
            String dbname = p.getProperty("dbname");
            String user = p.getProperty("user");
            String pass = p.getProperty("pass");
            con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + dbname + "", user, pass);
        } catch (URISyntaxException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**metoda odpowiedzialna za rozlaczenie sie z baza*/
    public static void disconnectDB() throws SQLException {
        if (con != null) {
            con.close();
        }
    }

    public static ResultSet queryUpdate(String query, String id) throws SQLException {
        if (con == null) {
            connectDB();
        }
        String arr[] = { id };
        Statement stmt = con.createStatement();
        stmt.executeUpdate(query, arr);
        ResultSet set = stmt.getGeneratedKeys();
        return set;
    }

    public static ResultSet queryUpdate(String query, String id, LinkedHashMap<String, Object> map) throws SQLException {
        if (con == null) {
            connectDB();
        }
        System.out.println(query);
        PreparedStatement stmt = con.prepareStatement(query);
        int i = 1;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            stmt.setObject(i, entry.getValue());
            System.out.println(entry.getValue());
            i++;
        }
        ResultSet result = null;
        try {
            stmt.execute();
            result = stmt.getGeneratedKeys();
        } catch (Exception e) {
            connectDB();
            stmt = con.prepareStatement(query);
            i = 1;
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                stmt.setObject(i, entry.getValue());
                System.out.println(entry.getValue());
                i++;
            }
            stmt.execute();
            result = stmt.getGeneratedKeys();
        }
        return result;
    }

    public static ResultSet querySelect(String query, LinkedHashMap<String, Object> map) throws SQLException {
        if (con == null) {
            connectDB();
        }
        System.out.println(query);
        PreparedStatement stmt = con.prepareStatement(query);
        int i = 1;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            stmt.setObject(i, entry.getValue());
            System.out.println(entry.getValue());
            i++;
        }
        ResultSet result;
        try {
            result = stmt.executeQuery();
        } catch (Exception e) {
            connectDB();
            stmt = con.prepareStatement(query);
            result = stmt.executeQuery();
        }
        return result;
    }

    public static ResultSet querySelect(String query) throws SQLException {
        if (con == null) {
            connectDB();
        }
        PreparedStatement stmt = con.prepareStatement(query);
        ResultSet result;
        try {
            result = stmt.executeQuery();
        } catch (Exception e) {
            connectDB();
            stmt = con.prepareStatement(query);
            result = stmt.executeQuery();
        }
        return result;
    }
}
