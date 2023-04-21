package jhomenet.db;

import java.util.*;
import java.sql.*;
import jhomenet.hw.*;
import org.apache.log4j.*;

/**
 * @author $Author: dhirwinjr $
 * @version $Revision: 389 $
 * Filename: $Source$
 * DESCRIPTION: Defines a database.
 */
public abstract class Database {

    /**
     * Define a logging mechanism.
     */
    private static Logger logger = Logger.getLogger(Database.class.getName());

    /**
     * The database connection
     */
    protected Connection conn = null;

    /**
     * Method used to connect to the database.
     * 
     * @param hostname
     * @param database
     * @param username
     * @param password
     */
    public void connect(String hostname, String database, String username, String password) throws DatabaseException {
        logger.debug("Attempting to connect to database:");
        logger.debug("  DB name: " + database);
        logger.debug("  Hostname: " + hostname);
        logger.debug("  Username: " + username);
        logger.debug("  Password: ******");
        if (conn == null) {
            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                logger.debug("Database driver successfully loaded");
            } catch (Exception e) {
                logger.fatal("Exception while loading database driver: " + e.getMessage(), e);
                throw new DatabaseException(e.getMessage());
            }
            try {
                this.conn = getConnection(hostname, database, username, password);
            } catch (Exception e) {
                logger.fatal("Exception while getting database connection: " + e.getMessage(), e);
                throw new DatabaseException(e.getMessage());
            }
        }
    }

    /**
     * Returns a connection to the MySQL database
     */
    protected Connection getConnection(String host, String database, String username, String password) throws Exception {
        String url = "";
        try {
            url = "jdbc:mysql://" + host + "/" + database + "?user=" + username + "&password=" + password;
            Connection con = DriverManager.getConnection(url);
            logger.debug("Database connection established to " + url);
            return con;
        } catch (java.sql.SQLException e) {
            logger.fatal("SQL excpetion while connected to database: " + e.getMessage(), e);
            throw new DatabaseException(e);
        }
    }

    /**
     * Close the connection to the database.
     * 
     * @return true if the operation is successful, false if not
     */
    public boolean close() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException sqle) {
                return false;
            } finally {
                conn = null;
            }
        }
        return true;
    }

    /**
     * Check whether a piece of hardware is registered.
     * 
     * @param hardwareID The hardware ID to check
     * @return Whether the hardware is registered or not
     */
    public abstract boolean isRegistered(String hardwareID) throws DatabaseException;

    public abstract boolean isSensor(String hardwareID) throws DatabaseException;

    public abstract boolean isDevice(String hardwareID) throws DatabaseException;

    /**
     * Edit a hardware's information stored in the database.
     * 
     * @param hardwareID
     * @param columnName
     * @param updatedValue
     * @throws DatabaseException
     */
    public abstract void editHardware(String hardwareID, String columnName, String updatedValue) throws DatabaseException;

    /**
     * Store the hardware's data (no unit passed).
     * 
     * @param hardwareID
     * @param value
     * @throws DatabaseException
     */
    public abstract void storeData(String hardwareID, double value) throws DatabaseException;

    /**
     * Store the hardware's data (with unit passed).
     * 
     * @param hardwareID The hardware ID 
     * @param value The value to store
     * @param unit The value's unit
     * @throws DatabaseException
     */
    public abstract void storeData(String hardwareID, double value, String unit) throws DatabaseException;

    /**
     * Save a newly registered hardware to the database.
     * 
     * @param hw A reference to the hardware object
     * @throws DatabaseException
     */
    public abstract void saveRegisteredHardware(HomenetHardware hw) throws DatabaseException;

    /**
     * Get the hardware parameters of a previously registered hardware
     * object from the database.
     * 
     * @param hardwareID The hardware ID parameters to retrieve
     * @return Hardware parameters of a registered hardware, null if the hardware
     *  ID is not of a registered piece of hardware.
     * @throws DatabaseException
     */
    public abstract HashMap<String, String> getHardwareMap(String hardwareID) throws DatabaseException;

    /**
     * Retrieve all the stored data for a given hardware object.
     * 
     * @param hardwareID
     * @return
     * @throws DatabaseException
     */
    public abstract Hashtable<Long, Vector> getData(String hardwareID) throws DatabaseException;

    /**
     * Register a database listener to the database.
     * 
     * @param listener The listener to add
     */
    public abstract void addListener(DatabaseListener listener);

    /**
     * Remove a database listener from the database.
     * 
     * @param listener The listener to remove
     */
    public abstract void removeListener(DatabaseListener listener);
}
