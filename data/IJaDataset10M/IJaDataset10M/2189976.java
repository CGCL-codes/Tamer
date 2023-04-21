package dataAccessLayer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

/**
 * Klasse voor het onderhouden, openen en sluiten van database connecties
 * @author Chris, Bart, Wannes
 */
public class ConnectionPool {

    private Properties dbProps = new Properties();

    private HashMap connections;

    /**
		 * Constructor voor het aanmaken van een nieuwe ConnectionPool aan de hand van een
		 * property Bestand.
		 * Het bestand moet ingelezen worden met een stream. Het bestand wordt dan
		 * doorgegeven als parameten aan de constructor
		 * @param props 
		 * @param initialConnections 
		 * @throws SQLException 
		 * @throws ClassNotFoundException 
		 */
    public ConnectionPool(Properties props, int initialConnections) throws SQLException, ClassNotFoundException {
        dbProps = props;
        initializePool(initialConnections);
    }

    /**
		 * Constructor voor het aanmaken van een nieuwe ConnectionPool zonder property
		 * bestand maar met een bekende driver, url, user en paswoord.
		 * @param driver
		 * @param url 
		 * @param user 
		 * @param password 
		 * @param initialConnections 
		 * @throws SQLException 
		 * @throws ClassNotFoundException 
		 */
    public ConnectionPool(String driver, String url, String user, String password, int initialConnections) throws SQLException, ClassNotFoundException {
        dbProps.put("connection.driver", driver);
        dbProps.put("connection.url", url);
        dbProps.put("user", user);
        dbProps.put("password", password);
        initializePool(initialConnections);
    }

    /** 
		 * Maakt de connectionpool aan met een gedefini�erd aantal initi�le connecties.
		 * De driver wordt geladen
		 * De capaciteit van de hashmap wordt op 2 maal de initiele connecties gebracht.
		 * een initieel aantal connecties wordt aangemaakt en in de hashmap geplaatst, er wordt een 
		 * "false" waarde aan meegegeven om aan te geven dat de connectie vrij is en klaar
		 * om gebruikt te worden
		 * @param initialConnections
		 * @throws SQLException
		 * @throws ClassNotFoundException
		 */
    @SuppressWarnings("unchecked")
    private void initializePool(int initialConnections) throws SQLException, ClassNotFoundException {
        connections = new HashMap(2 * initialConnections);
        Connection currentCon;
        Class.forName(dbProps.getProperty("connection.driver"));
        for (int i = 1; i <= initialConnections; i++) {
            currentCon = DriverManager.getConnection(dbProps.getProperty("connection.url"), dbProps);
            connections.put(currentCon, Boolean.FALSE);
        }
    }

    /**
		 * Deze methode neemt een vrije connectie en houd deze in gebruik tot ze niet meer nodig is
		 * @return con
		 * @throws SQLException
		 */
    @SuppressWarnings("unchecked")
    public synchronized Connection getConnection() throws SQLException {
        Connection con;
        con = findAvailableConnection();
        connections.put(con, Boolean.TRUE);
        return con;
    }

    /**
		 * Deze methode zoekt naar beschikbare connecties.Als er geen connectie vrij is wordt er
		 * 1 bijgemaakt.
		 * De "getConnection" methode past de "notFree" waarde in de hashmap aan naar "true"
		 * Als er een nieuwe connectie werd aangemaakt voegt hij deze toe aan de hashmap.
		 * @return con
		 * @throws SQLException
		 */
    private Connection findAvailableConnection() throws SQLException {
        Set currConnections = connections.keySet();
        Iterator conIter = currConnections.iterator();
        Connection con;
        while (conIter.hasNext()) {
            con = ((Connection) conIter.next());
            Boolean notFree = ((Boolean) connections.get(con));
            if (!(notFree.booleanValue())) {
                return con;
            }
        }
        return DriverManager.getConnection(dbProps.getProperty("connection.url"), dbProps);
    }

    /**
		 * Als de connectie er 1 is uit de pool dan wordt deze vrijgegeven
		 * 
		 * @param con
		 */
    @SuppressWarnings("unchecked")
    public void releaseConnection(Connection con) {
        if (connections.containsKey(con)) {
            connections.put(con, Boolean.FALSE);
        }
    }
}
