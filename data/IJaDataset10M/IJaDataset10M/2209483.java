package crawler.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBObjectDB {

    private static String propfile = "db.properties";

    public static Connection get_connection() throws SQLException {
        System.out.println("DBObject.get_connection");
        Connection cn = null;
        try {
            java.io.InputStream propFile = DBObjectDB.class.getResourceAsStream(propfile);
            Properties props = new Properties(System.getProperties());
            props.load(propFile);
            Class.forName(props.getProperty("driver"));
            String usr = props.getProperty("user");
            String pwd = props.getProperty("password");
            String url = props.getProperty("url");
            cn = DriverManager.getConnection(url, usr, pwd);
        } catch (Exception e) {
            System.err.println("DBObject::Probleme beim Einlesen der DB-Verbindungsdaten:\n" + e.toString());
        }
        return cn;
    }
}
