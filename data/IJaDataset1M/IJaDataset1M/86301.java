package data.db;

import java.util.List;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Maintains a pool of DataConnections. This class is a bit dumb really - I'm going to design it properly.
 * @author Ben Hill
 * @version 0.1
 */
public class DataConnectionPool {

    private static List connections;

    private static String driver;

    private static String url;

    private static int poolSize = 10;

    private static int index = 0;

    private static int connectionLifetime = 120;

    private static int maximumPoolSize = 20;

    public static DataConnection getDataConnection() {
        if ((connections == null) || (connections.size() == 0)) {
            createPool();
        }
        return nextConnection();
    }

    /** Set the properties from the database.properties resource bundle. */
    private static void setProperties() {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("properties.database");
            driver = bundle.getString("driver");
            url = bundle.getString("url");
            try {
                poolSize = Integer.parseInt(bundle.getString("initial-pool-size"));
            } catch (NumberFormatException nfe) {
            }
            ;
            try {
                connectionLifetime = Integer.parseInt(bundle.getString("connection-lifetime"));
            } catch (NumberFormatException nfe) {
            }
            ;
            try {
                maximumPoolSize = Integer.parseInt(bundle.getString("maximum-pool-size"));
            } catch (NumberFormatException nfe) {
            }
            ;
        } catch (Exception e) {
            System.out.println("DataConnectionPool.setProperties() caught " + e.getMessage());
        }
    }

    /** Lazy-load connections if pool contains many. */
    private static void createPool() {
        connections = new ArrayList();
        setProperties();
        for (int i = 0; i < poolSize; i++) {
            try {
                DataConnection connection = new DataConnection(url, driver);
                connections.add(connection);
            } catch (Exception e) {
                System.out.println("DataConnectionPool.createPool() caught " + e.getMessage());
            }
        }
    }

    /** Obtain the next index or if at max, loops back to zero. */
    private static int nextIndex() {
        if (index == (connections.size() - 1)) {
            index = 0;
        } else {
            index++;
        }
        return index;
    }

    /** Return the next available connection. */
    private static DataConnection nextConnection() {
        DataConnection dc = (DataConnection) connections.get(nextIndex());
        if (dc.isClosed()) {
            System.out.println("DataConnection has been closed, re-establishing");
            try {
                dc = new DataConnection(url, driver);
            } catch (Exception e) {
                System.out.println("DataConnectionPool.nextConnection() caught " + e.getMessage());
            }
        }
        return dc;
    }

    /** Return a data connection after it's been used. */
    public static void returnDataConnection(DataConnection conn) {
        conn.setInUse(false);
    }
}
