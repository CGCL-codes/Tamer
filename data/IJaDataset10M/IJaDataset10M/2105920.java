package jhomenet.db;

import java.text.*;
import java.sql.*;
import java.util.*;
import org.apache.log4j.*;
import jhomenet.hw.*;
import jhomenet.hw.sensor.*;
import jhomenet.hw.device.*;

/**
 * @author $Author: dhirwinjr $
 * @version $Revision: 513 $
 * ID: $Id: JHomeNetDB.java 513 2005-09-08 03:42:57Z dhirwinjr $
 * Filename: $Source$
 * Description: The <class>JHomeNetDB</class> class is used as a the database
 * connector. The class uses the Singleton design pattern in order to ensure
 * that there is only a single database instance.
 */
public class JHomeNetDB extends Database {

    /**
     * Define a logging mechanism.
     */
    private static Logger logger = Logger.getLogger(JHomeNetDB.class.getName());

    /**
     * Keep a list of database listeners.
     */
    private ArrayList<DatabaseListener> listeners = new ArrayList<DatabaseListener>();

    /**
     * 
     */
    private static Hashtable<String, String> indexCache = new Hashtable<String, String>();

    private static final int maxCacheSize = 10;

    /**
     * Used for formatting purposes.
     */
    private DecimalFormat df = new DecimalFormat();

    /**
     * A collection of reference tables.
     */
    public static enum ReferenceTables {

        POLLINGTYPES("pollingTypes", "pollingType_id", "pollingType"), DRIVERS("hardwaredrivers", "hardwaredriver_id", "hardwaredriver"), HWIDS("hwids", "hwID_id", "hwID"), HNHWTYPES("hnTypes", "hnType_id", "hnType"), DRIVERHWTYPES("driverhwtypes", "driverhwtype_id", "driverhwtype");

        /**
         * Table name
         */
        private String tableName;

        /**
         * Index column name
         */
        private String indexColumnName;

        /**
         * Value column name.
         */
        private String valueColumnName;

        /**
         * Default constructor.
         * 
         * @param tableName
         * @param indexColumnName
         * @param valueColumnName
         */
        private ReferenceTables(String tableName, String indexColumnName, String valueColumnName) {
            this.tableName = tableName;
            this.indexColumnName = indexColumnName;
            this.valueColumnName = valueColumnName;
        }

        /**
         * Get the table name of the reference table.
         * 
         * @return
         */
        public String getTableName() {
            return tableName;
        }

        /**
         * Get the index column name.
         * 
         * @return
         */
        public String getIndexColumnName() {
            return indexColumnName;
        }

        /**
         * Get the value column name.
         * 
         * @return
         */
        public String getValueColumnName() {
            return valueColumnName;
        }

        /**
         * @see java.lang.Object#toString()
         */
        public String toString() {
            return tableName;
        }
    }

    private enum ValidInputs {

        LOW(-100), HIGH(150);

        private Integer value;

        private ValidInputs(Integer input) {
            this.value = input;
        }

        public Integer getValue() {
            return value;
        }
    }

    private static final String SENSORS_tableName = "sensors";

    private static final String DEVICES_tableName = "devices";

    private static final String DATA_tableName = "data";

    private static final String DATA_dateColumn = "timestamp";

    private static final String DATA_valueColumn = "value";

    private static final String DATA_unitColumn = "unit";

    public static final String descColumn = "description";

    public static final String configColumn = "config";

    private static final int maxRecursions = 0;

    private static JHomeNetDB db = null;

    /**
     * Default constructor. Because of the 'protected' access modifier, the
     * class cannot be instantiated from outside the class.
     */
    protected JHomeNetDB() {
        df.applyPattern("###,###.000");
    }

    /**
     * Get the singleton instance of the database.
     * 
     * @return Instance of the database.
     */
    static JHomeNetDB instance() {
        if (db == null) {
            db = new JHomeNetDB();
        }
        return db;
    }

    /**
     * @see jhomenet.db.Database#storeData(java.lang.String, double)
     */
    public void storeData(String hardwareID, double value) throws DatabaseException {
        storeData(hardwareID, value, "");
    }

    /**
     * Store data in the database.
     * 
     * @see jhomenet.db.Database#storeData(java.lang.String, double,
     *      java.lang.String)
     */
    public void storeData(String hardwareID, double value, String unit) throws DatabaseException {
        if (value < ValidInputs.LOW.getValue() || value > ValidInputs.HIGH.getValue()) {
            logger.error("Possible error in data value: out of range -> " + value);
        } else {
            Statement stmt = null;
            ResultSet rs = null;
            if (conn != null) {
                if (isRegistered(hardwareID)) {
                    try {
                        String address_id = getKey(ReferenceTables.HWIDS.getTableName(), ReferenceTables.HWIDS.getIndexColumnName(), ReferenceTables.HWIDS.getValueColumnName(), hardwareID, false);
                        if (address_id == null) {
                            throw new DatabaseException("Key value set to null");
                        }
                        logger.debug("Storing data for hardware " + hardwareID + ": " + value);
                        String query = "INSERT INTO " + DATA_tableName + " (" + ReferenceTables.HWIDS.getIndexColumnName() + ", " + DATA_valueColumn + ", " + DATA_unitColumn + ", " + DATA_dateColumn + ") VALUES ('" + address_id + "', '" + df.format(value) + "', '" + unit + "', '" + System.currentTimeMillis() + "')";
                        logger.debug("SQL query: " + query);
                        stmt = conn.createStatement();
                        int result = stmt.executeUpdate(query);
                        logger.debug(" Results -> " + result + " row created");
                        rs = stmt.executeQuery("SELECT LAST_INSERT_ID()");
                        if (rs.next()) {
                            logger.debug(" Last insert ID: " + Integer.toString(rs.getInt(1)));
                        }
                        fireDatabaseUpdate(new DataUpdateEvent(this, hardwareID));
                    } catch (SQLException sqle) {
                        logger.error("Exception while storing data: " + sqle.getMessage(), sqle);
                    } finally {
                        closeStatement(stmt);
                        closeResultSet(rs);
                    }
                }
            }
        }
    }

    /**
     * Store event text in the database.
     * 
     * TODO: Implement the method!
     * 
     * @param hardwareID
     * @param event
     */
    public void storeEvent(String hardwareID, String event) {
    }

    /**
     * Edit the hardware.
     *
     * @see jhomenet.db.Database#editHardware(java.lang.String, java.lang.String, java.lang.String)
     */
    public void editHardware(String hardwareID, String columnName, String updatedValue) throws DatabaseException {
        Statement stmt = null;
        if (conn != null) {
            if (isRegistered(hardwareID)) {
                try {
                    stmt = conn.createStatement();
                    String address_id = getHardwareIDIndex(hardwareID);
                    if (address_id == null) {
                        throw new DatabaseException("Key value set to null");
                    }
                    logger.debug("Updating registered hardware information");
                    String query = "UPDATE " + SENSORS_tableName + " SET " + columnName + "='" + updatedValue + "' WHERE " + SENSORS_tableName + "." + ReferenceTables.HWIDS.getIndexColumnName() + "=" + address_id;
                    logger.debug("SQL query = " + query);
                    int result = stmt.executeUpdate(query);
                    logger.debug(" Results -> " + result + " row updated");
                } catch (SQLException sqle) {
                    logger.error("SQL excpetion while updating registered hardware: " + sqle.getMessage(), sqle);
                    throw new DatabaseException(sqle.getMessage());
                } finally {
                    closeStatement(stmt);
                }
            }
        }
    }

    /**
     * Save registered hardware to the database.
     *
     * @see jhomenet.db.Database#saveRegisteredHardware(jhomenet.hw.HomenetHardware)
     */
    public void saveRegisteredHardware(HomenetHardware hw) {
        try {
            if (hw instanceof Sensor) {
                saveRegisteredSensorHardware((Sensor) hw);
            } else if (hw instanceof Device) {
                saveRegisteredDeviceHardware((Device) hw);
            }
        } catch (DatabaseException dbe) {
        }
    }

    /**
     * Save only registered device hardware to the database.
     * 
     * @param device
     * @throws DatabaseException
     */
    private void saveRegisteredDeviceHardware(Device device) throws DatabaseException {
        String query = null;
        String hardwareID = device.getHardwareId();
        String driverHWType = device.getHardwareType().toString();
        String hnType = device.getHardwareType().toString();
        String config = device.getConfiguration();
        String hardwareDriverName = device.getDriverName();
        String desc = device.getDescription();
        if (!isRegistered(hardwareID)) {
            String address_id = getKey(ReferenceTables.HWIDS.getTableName(), ReferenceTables.HWIDS.getIndexColumnName(), ReferenceTables.HWIDS.getValueColumnName(), hardwareID, true);
            String driverHWType_id = getKey(ReferenceTables.DRIVERHWTYPES.getTableName(), ReferenceTables.DRIVERHWTYPES.getIndexColumnName(), ReferenceTables.DRIVERHWTYPES.getValueColumnName(), driverHWType, true);
            String hnType_id = getKey(ReferenceTables.HNHWTYPES.getTableName(), ReferenceTables.HNHWTYPES.getIndexColumnName(), ReferenceTables.HNHWTYPES.getValueColumnName(), hnType, true);
            String driver_id = getKey(ReferenceTables.DRIVERS.getTableName(), ReferenceTables.DRIVERS.getIndexColumnName(), ReferenceTables.DRIVERS.getValueColumnName(), hardwareDriverName, false);
            logger.debug("Storing device hardware in db:");
            logger.debug("  Hardware ID: " + hardwareID);
            logger.debug("  Driver hardware type: " + driverHWType);
            logger.debug("  Hardware driver name: " + hardwareDriverName);
            logger.debug("  Description: " + desc);
            logger.debug("  Configuration: " + config);
            query = "INSERT INTO " + DEVICES_tableName + " (" + ReferenceTables.HWIDS.getIndexColumnName() + ", " + ReferenceTables.DRIVERHWTYPES.getIndexColumnName() + ", " + ReferenceTables.HNHWTYPES.getIndexColumnName() + ", " + ReferenceTables.DRIVERS.getIndexColumnName() + ", " + descColumn + ", " + configColumn + ") VALUES ('" + address_id + "', '" + driverHWType_id + "', '" + hnType_id + "', '" + driver_id + "', '" + desc + "', '" + config + "')";
            int result = executeUpdate(query);
            fireDatabaseUpdate(new NewRegisteredHWEvent(this, hardwareID));
        }
    }

    /**
     * Save only registered sensor hardware to the database.
     * 
     * @param sensor
     * @throws DatabaseException
     */
    private void saveRegisteredSensorHardware(Sensor sensor) throws DatabaseException {
        String query = null;
        String hardwareID = sensor.getHardwareId();
        String driverHWType = sensor.getDriverHardwareDescription();
        String hnType = sensor.getHardwareType().toString();
        String pollingType = sensor.getPollingType().toString();
        String config = sensor.getConfiguration();
        String hardwareDriverName = sensor.getDriverName();
        String desc = sensor.getDescription();
        if (!isRegistered(hardwareID)) {
            String address_id = getKey(ReferenceTables.HWIDS.getTableName(), ReferenceTables.HWIDS.getIndexColumnName(), ReferenceTables.HWIDS.getValueColumnName(), hardwareID, true);
            String driverHWType_id = getKey(ReferenceTables.DRIVERHWTYPES.getTableName(), ReferenceTables.DRIVERHWTYPES.getIndexColumnName(), ReferenceTables.DRIVERHWTYPES.getValueColumnName(), driverHWType, true);
            String hnType_id = getKey(ReferenceTables.HNHWTYPES.getTableName(), ReferenceTables.HNHWTYPES.getIndexColumnName(), ReferenceTables.HNHWTYPES.getValueColumnName(), hnType, true);
            String pollingFreq_id = getKey(ReferenceTables.POLLINGTYPES.getTableName(), ReferenceTables.POLLINGTYPES.getIndexColumnName(), ReferenceTables.POLLINGTYPES.getValueColumnName(), pollingType, false);
            String driver_id = getKey(ReferenceTables.DRIVERS.getTableName(), ReferenceTables.DRIVERS.getIndexColumnName(), ReferenceTables.DRIVERS.getValueColumnName(), hardwareDriverName, false);
            logger.debug("Storing sensor hardware in db:");
            logger.debug("  Hardware ID: " + hardwareID);
            logger.debug("  Driver hardware type: " + driverHWType);
            logger.debug("  Polling type: " + pollingType);
            logger.debug("  Hardware driver name: " + hardwareDriverName);
            logger.debug("  Description: " + desc);
            logger.debug("  Configuration: " + config);
            query = "INSERT INTO " + SENSORS_tableName + " (" + ReferenceTables.HWIDS.getIndexColumnName() + ", " + ReferenceTables.DRIVERHWTYPES.getIndexColumnName() + ", " + ReferenceTables.HNHWTYPES.getIndexColumnName() + ", " + ReferenceTables.POLLINGTYPES.getIndexColumnName() + ", " + ReferenceTables.DRIVERS.getIndexColumnName() + ", " + descColumn + ", " + configColumn + ") VALUES ('" + address_id + "', '" + driverHWType_id + "', '" + hnType_id + "', '" + pollingFreq_id + "', '" + driver_id + "', '" + desc + "', '" + config + "')";
            int result = executeUpdate(query);
            fireDatabaseUpdate(new NewRegisteredHWEvent(this, hardwareID));
        }
    }

    /**
     * Execute a given SQL query.
     * 
     * @param query
     *            The SQL query to execute
     * @return
     */
    private int executeUpdate(String query) throws DatabaseException {
        Statement stmt = null;
        int result = -1;
        if (conn != null) {
            try {
                stmt = conn.createStatement();
                logger.debug("SQL query: " + query);
                result = stmt.executeUpdate(query);
                logger.debug(" Results -> " + result + " row created");
            } catch (SQLException sqle) {
                logger.error("SQL excpetion while saving registered hardware: " + sqle.getMessage(), sqle);
                throw new DatabaseException(sqle.getMessage());
            } finally {
                closeStatement(stmt);
            }
        }
        return result;
    }

    /**
     * Execute the SQL query against the database.
     * 
     * @param query The SQL query to execute
     * @return
     * @throws DatabaseException
     */
    private ResultSet executeQuery(String query) throws DatabaseException {
        Statement stmt = null;
        ResultSet rs = null;
        if (conn != null) {
            try {
                stmt = conn.createStatement();
                logger.debug("SQL query: " + query);
                rs = stmt.executeQuery(query);
            } catch (SQLException sqle) {
                logger.error("SQL excpetion while executing query: " + sqle.getMessage(), sqle);
                throw new DatabaseException(sqle.getMessage());
            } finally {
                closeStatement(stmt);
            }
        }
        return rs;
    }

    /**
     * Gets a set of registered hardware parameters given the hardware
     * address. Returns null if the address doesn't exist.
     *
     * @see jhomenet.db.Database#getHardwareMap(java.lang.String)
     */
    public HashMap<String, String> getHardwareMap(String hardwareID) throws DatabaseException {
        logger.debug("[getHWMap] Getting hardware map for hardware " + hardwareID);
        HashMap<String, String> hardwareMap = null;
        boolean isSensor = false;
        Statement stmt = null;
        ResultSet rs = null;
        String query = null;
        if (conn != null) {
            try {
                stmt = conn.createStatement();
                String hwID_id = getKey(ReferenceTables.HWIDS.getTableName(), ReferenceTables.HWIDS.getIndexColumnName(), ReferenceTables.HWIDS.getValueColumnName(), hardwareID, false);
                if (hwID_id == null) {
                    throw new DatabaseException("1-Wire address ID set to null");
                }
                if (isSensor(hardwareID)) {
                    logger.debug("[getHWMap] Hardware is a sensor");
                    isSensor = true;
                    query = "SELECT * FROM " + SENSORS_tableName + " WHERE " + SENSORS_tableName + "." + ReferenceTables.HWIDS.getIndexColumnName() + " = '" + hwID_id + "'";
                    logger.debug("[getHWMap] Sensor SQL query: " + query);
                    rs = stmt.executeQuery(query);
                } else if (isDevice(hardwareID)) {
                    logger.debug("[getHWMap] Hardware is a device");
                    isSensor = false;
                    query = "SELECT * FROM " + DEVICES_tableName + " WHERE " + DEVICES_tableName + "." + ReferenceTables.HWIDS.getIndexColumnName() + " = '" + hwID_id + "'";
                    logger.debug("[getHWMap] Device SQL query: " + query);
                    rs = stmt.executeQuery(query);
                } else {
                    logger.debug("[getHWMap] Hardware " + hardwareID + " neither a sensor or device");
                    return null;
                }
                logger.debug("[getHWMap] Building hardware map");
                if (rs.next()) {
                    String homenetType_id = rs.getObject(ReferenceTables.HNHWTYPES.getIndexColumnName()).toString();
                    String driverHWType_id = rs.getObject(ReferenceTables.DRIVERHWTYPES.getIndexColumnName()).toString();
                    String driver_id = rs.getObject(ReferenceTables.DRIVERS.getIndexColumnName()).toString();
                    String pollingType_id = null;
                    if (isSensor) {
                        pollingType_id = rs.getObject(ReferenceTables.POLLINGTYPES.getIndexColumnName()).toString();
                    }
                    hardwareMap = new HashMap<String, String>();
                    hardwareMap.put(ReferenceTables.HWIDS.toString(), hardwareID);
                    hardwareMap.put(descColumn, rs.getObject(descColumn).toString());
                    hardwareMap.put(configColumn, rs.getObject(configColumn).toString());
                    query = "SELECT " + ReferenceTables.DRIVERHWTYPES.getValueColumnName() + " FROM " + ReferenceTables.DRIVERHWTYPES.getTableName() + " WHERE " + ReferenceTables.DRIVERHWTYPES.getTableName() + "." + ReferenceTables.DRIVERHWTYPES.getValueColumnName() + " = " + driverHWType_id;
                    logger.debug("[getHWMap] SQL query: " + query);
                    rs = stmt.executeQuery(query);
                    if (rs.next()) {
                        hardwareMap.put(ReferenceTables.DRIVERHWTYPES.toString(), rs.getObject(ReferenceTables.DRIVERHWTYPES.getValueColumnName()).toString());
                    }
                    query = "SELECT " + ReferenceTables.HNHWTYPES.getValueColumnName() + " FROM " + ReferenceTables.HNHWTYPES.getTableName() + " WHERE " + ReferenceTables.HNHWTYPES.getTableName() + "." + ReferenceTables.HNHWTYPES.getIndexColumnName() + " = " + homenetType_id;
                    logger.debug("[getHWMap] SQL query: " + query);
                    rs = stmt.executeQuery(query);
                    if (rs.next()) {
                        hardwareMap.put(ReferenceTables.HNHWTYPES.toString(), rs.getObject(ReferenceTables.HNHWTYPES.getValueColumnName()).toString());
                    }
                    query = "SELECT " + ReferenceTables.DRIVERS.getValueColumnName() + " FROM " + ReferenceTables.DRIVERS.getTableName() + " WHERE " + ReferenceTables.DRIVERS.getTableName() + ".hardwaredriver_id = " + driver_id;
                    logger.debug("[getHWMap] SQL query: " + query);
                    rs = stmt.executeQuery(query);
                    if (rs.next()) {
                        hardwareMap.put(ReferenceTables.DRIVERS.toString(), rs.getObject(ReferenceTables.DRIVERS.getValueColumnName()).toString());
                    }
                    if (isSensor) {
                        query = "SELECT " + ReferenceTables.POLLINGTYPES.getValueColumnName() + " FROM " + ReferenceTables.POLLINGTYPES.getTableName() + " WHERE " + ReferenceTables.POLLINGTYPES.getTableName() + ".pollingType_id = " + pollingType_id;
                        logger.debug("[getHWMap] SQL query: " + query);
                        rs = stmt.executeQuery(query);
                        if (rs.next()) {
                            hardwareMap.put(ReferenceTables.POLLINGTYPES.toString(), rs.getObject(ReferenceTables.POLLINGTYPES.getValueColumnName()).toString());
                        }
                    }
                }
            } catch (SQLException sqle) {
                logger.error("Exception while retrieving registered hardware map: " + sqle.getMessage(), sqle);
                throw new DatabaseException(sqle);
            } finally {
                closeStatement(stmt);
                closeResultSet(rs);
            }
        }
        return hardwareMap;
    }

    /**
     * Get a list of the data column names.
     * 
     * TODO: Implement!
     * 
     * @return
     */
    public Vector<String> getDataColumnNames() {
        Vector<String> columnNames = new Vector<String>();
        return columnNames;
    }

    /**
     * Get the hardware ID index of a hardware ID.
     * 
     * @param hardwareID
     * @return
     */
    private String getHardwareIDIndex(String hardwareID) {
        return getKey(ReferenceTables.HWIDS.getTableName(), ReferenceTables.HWIDS.getIndexColumnName(), ReferenceTables.HWIDS.getValueColumnName(), hardwareID, false);
    }

    /**
     * Check whether the hardware associated with the passed 1-Wire address is
     * registered or not.
     *
     * @see jhomenet.db.Database#isRegistered(java.lang.String)
     */
    public boolean isRegistered(String hardwareID) throws DatabaseException {
        if (isSensor(hardwareID) || isDevice(hardwareID)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @see jhomenet.db.Database#isSensor(java.lang.String)
     */
    public boolean isSensor(String hardwareID) throws DatabaseException {
        Statement stmt = null;
        ResultSet rs = null;
        String query = null;
        if (conn != null) {
            try {
                stmt = conn.createStatement();
                String address_id = getKey(ReferenceTables.HWIDS.getTableName(), ReferenceTables.HWIDS.getIndexColumnName(), ReferenceTables.HWIDS.getValueColumnName(), hardwareID, false);
                if (address_id == null) {
                    logger.debug("Hardwre " + hardwareID + " not registered in database");
                    return false;
                }
                query = "SELECT * FROM " + SENSORS_tableName + " WHERE " + SENSORS_tableName + "." + ReferenceTables.HWIDS.getIndexColumnName() + " = '" + address_id + "'";
                logger.debug("[isSensor] SQL query: " + query);
                rs = stmt.executeQuery(query);
                if (rs.next()) {
                    logger.debug("[isSensor] Hardware " + hardwareID + " is sensor in the sensor table");
                    return true;
                } else {
                    logger.debug("[isSensor] Hardware " + hardwareID + " is not in the sensor table");
                    return false;
                }
            } catch (SQLException sqle) {
                logger.error("Exception while checking for registered hardware: " + sqle.getMessage(), sqle);
            } finally {
                closeStatement(stmt);
                closeResultSet(rs);
            }
        }
        return false;
    }

    /**
     * @see jhomenet.db.Database#isDevice(java.lang.String)
     */
    public boolean isDevice(String hardwareID) throws DatabaseException {
        Statement stmt = null;
        ResultSet rs = null;
        String query = null;
        if (conn != null) {
            try {
                stmt = conn.createStatement();
                String address_id = getKey(ReferenceTables.HWIDS.getTableName(), ReferenceTables.HWIDS.getIndexColumnName(), ReferenceTables.HWIDS.getValueColumnName(), hardwareID, false);
                if (address_id == null) {
                    logger.debug("Hardwre " + hardwareID + " not in database");
                    return false;
                }
                query = "SELECT * FROM " + DEVICES_tableName + " WHERE " + DEVICES_tableName + "." + ReferenceTables.HWIDS.getIndexColumnName() + " = '" + address_id + "'";
                logger.debug("[isDevice] SQL query: " + query);
                rs = stmt.executeQuery(query);
                if (rs.next()) {
                    logger.debug("[isDevice] Hardware " + hardwareID + " is a device in the device table");
                    return true;
                } else {
                    logger.debug("[isDevice] Hardware " + hardwareID + " is not a device in the device table");
                    return false;
                }
            } catch (SQLException sqle) {
                logger.error("Exception while checking for device hardware: " + sqle.getMessage(), sqle);
            } finally {
                closeStatement(stmt);
                closeResultSet(rs);
            }
        }
        return false;
    }

    /**
     * Get all the data for a given registered hardware object.
     *
     * @see jhomenet.db.Database#getData(java.lang.String)
     */
    public Hashtable<Long, Vector> getData(String hardwareID) throws DatabaseException {
        Statement stmt = null;
        ResultSet rs = null;
        Hashtable<Long, Vector> data = new Hashtable<Long, Vector>();
        try {
            stmt = conn.createStatement();
            String hardwareIDIndex = getHardwareIDIndex(hardwareID);
            if (hardwareIDIndex == null) {
                throw new DatabaseException("Key value set to null");
            }
            String query = "SELECT * FROM " + DATA_tableName + " WHERE " + DATA_tableName + "." + ReferenceTables.HWIDS.getIndexColumnName() + "=" + hardwareIDIndex;
            logger.debug("SQL query: " + query);
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                Vector tmp = new Vector(3);
                double value = rs.getDouble("value");
                if (value < 250 && value > -100) {
                    tmp.add(rs.getLong("timestamp"));
                    tmp.add(value);
                    tmp.add(rs.getString("unit"));
                    data.put(rs.getLong("timestamp"), tmp);
                } else {
                    logger.error("Error while retrieving data: out of range -> " + value);
                }
            }
        } catch (SQLException sqle) {
            logger.error("Exception while retrieving data: " + sqle.getMessage(), sqle);
        } finally {
            closeStatement(stmt);
            closeResultSet(rs);
        }
        return data;
    }

    public void storeInCache(String key, String value) {
        if (indexCache.size() >= maxCacheSize) {
        }
    }

    /**
     * Retrieve the necessary key.
     * 
     * @param tableName The table to search
     * @param returnColumnName The column name we'd like returned
     * @param searchColumnName The column name in which we're searching
     * @param searchEntry The entry we're searching for
     * @return The key
     */
    private String getKey(String tableName, String returnColumnName, String searchColumnName, String searchEntry, boolean insertOk) {
        logger.debug("[getKey] Retrieving key from: tablename=" + tableName + ", column=" + searchColumnName + ", value=" + searchEntry);
        Statement stmt = null;
        ResultSet rs = null;
        String key = null;
        if (conn != null) {
            try {
                stmt = conn.createStatement();
                String query = "SELECT " + returnColumnName + " FROM " + tableName + " WHERE " + tableName + "." + searchColumnName + " = '" + searchEntry + "'";
                logger.debug("[getKey] SQL query: " + query);
                rs = stmt.executeQuery(query);
                if (rs.next()) {
                    return rs.getObject(returnColumnName).toString();
                } else if (insertOk) {
                    query = "INSERT INTO " + tableName + " (" + searchColumnName + ") VALUES ('" + searchEntry + "')";
                    logger.debug("[getKey] SQL query: " + query);
                    int result = stmt.executeUpdate(query);
                    logger.debug("[getKey]  Results -> " + result + " row created");
                    rs = stmt.executeQuery("SELECT LAST_INSERT_ID()");
                    if (rs.next()) {
                        key = Integer.toString(rs.getInt(1));
                    }
                }
            } catch (SQLException sqle) {
                logger.error("Exception while retrieving key: " + sqle.getMessage(), sqle);
            } finally {
                closeStatement(stmt);
                closeResultSet(rs);
            }
        }
        return key;
    }

    /**
     * Close a statement object.
     * 
     * @param stmt
     */
    private void closeStatement(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException sqle) {
            }
        }
    }

    /**
     * Close a result set object.
     * 
     * @param rs
     */
    private void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException sqlEx) {
            }
        }
    }

    /**
     * Add a database listener. Listeners will be updated whenever there is an
     * update to the datbase.
     * 
     * @see jhomenet.db.Database#addListener(jhomenet.db.DatabaseListener)
     */
    public void addListener(DatabaseListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
            logger.debug("Database listener added");
        } else {
            logger.error("Error: cannot add new listener -> list already contains listener");
        }
    }

    /**
     * Remove a database listener.
     * 
     * @see jhomenet.db.Database#removeListener(jhomenet.db.DatabaseListener)
     */
    public void removeListener(DatabaseListener listener) {
        if (listeners.contains(listener)) {
            listeners.remove(listener);
            logger.debug("Database listener removed");
        } else {
            logger.error("Error: cannot remove listener -> list doesn't contain listener");
        }
    }

    private void fireDatabaseUpdate(DatabaseEvent event) {
        logger.debug("notifying database listeners of data update event");
        logger.debug("Number of listeners receiving event: " + listeners.size());
        if (listeners.size() == 0) {
            return;
        }
        for (DatabaseListener listener : listeners) {
        }
    }
}
