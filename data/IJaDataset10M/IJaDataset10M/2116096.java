package com.meatfreezer.nms.cacti;

import com.meatfreezer.nms.IDevice;
import com.meatfreezer.nms.INMS;
import com.meatfreezer.nms.IPollingItem;
import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import org.apache.log4j.Level;

/**
 *
 * @author Alex Shepard
 */
public class Cacti implements INMS {

    private java.sql.Connection db;

    private static Properties cactiProperties;

    private Queue<IPollingItem> pollResults = new ConcurrentLinkedQueue<IPollingItem>();

    private List<IPollingItem> pollingItems;

    private CactiInserter cactiInserter;

    private static Logger logger;

    /**
     * shut down the cacti system.
     */
    public void close() {
        cactiInserter.recordPollerStats();
        cactiInserter.setFinished();
    }

    /**
     * Initialize the cacti system.
     */
    public void connect() throws SQLException, IOException {
        getOptionsFromConfigFile();
        db = initDb();
        getOptionsFromDb();
        logger = Logger.getLogger("com.meatfreezer.jactid");
        cactiInserter = new CactiInserter(db, pollResults);
        Thread t = new Thread(cactiInserter);
        t.start();
    }

    /**
     * Get a property from the cacti Properties object.
     * 
     * @param   property    The property to get, ie "max_oid_len".
     * @return  The value of the property requested.
     */
    public String getProperty(String property) {
        return cactiProperties.getProperty(property);
    }

    /**
     * Update the device record (ping response time, availability, etc).
     * This is a TOTAL hack, this insertSql stuff.
     * 
     * @param   device  The device whose record needs updating.
     */
    public void updateDeviceRecord(IDevice device) {
        CactiDevice cactiDevice = (CactiDevice) device;
        cactiInserter.insertSql(cactiDevice.toSql());
    }

    /**
     * Add poll results to Cacti's poll results queue.  This queue is monitored
     * by a CactiInserter consumer process which then will handle inserting
     * the poll results into the Cacti database/system/etc.
     * 
     * @param   pollingItems The poll results to add.
     */
    public void insertResults(List<IPollingItem> results) {
        if (logger.isEnabledFor(Level.INFO)) {
            logger.info("asked to insert " + results.size() + " items.");
        }
        this.pollResults.addAll(results);
    }

    /**
     * Creates a new instance of Cacti.
     */
    public Cacti() {
        cactiProperties = new Properties(getDefaultProperties());
    }

    /**
     * Creates the default properties.  Should probably read these
     * from a file or something.
     *
     * @return  A Properties object containing the Cacti defaults.
     */
    public Properties getDefaultProperties() {
        Properties defaults = new Properties();
        defaults.setProperty("DB_Type", "mysql");
        defaults.setProperty("DB_Host", "localhost");
        defaults.setProperty("DB_Database", "cacti");
        defaults.setProperty("DB_User", "cactiuser");
        defaults.setProperty("DB_Pass", "cactiuser");
        defaults.setProperty("availability_method", "1");
        defaults.setProperty("ping_retries", "1");
        defaults.setProperty("ping_timeout", "400");
        defaults.setProperty("max_get_size", "10");
        return defaults;
    }

    /**
     * Read the configuration file.  Currently we assume that one is 
     * installed in /etc/jactid.conf or /usr/local/etc/jactid.conf.  The 
     * config file gives us information on how to contact the Cacti
     * database.
     */
    private void getOptionsFromConfigFile() {
        File configFile;
        File etcConfigFile = new File("/etc/jactid.conf");
        File usrLocalEtcConfigFile = new File("/usr/local/etc/jactid.conf");
        if (etcConfigFile.exists()) {
            configFile = etcConfigFile;
        } else if (usrLocalEtcConfigFile.exists()) {
            configFile = usrLocalEtcConfigFile;
        } else {
            System.err.println("Can't find a jactid.conf file.  Please " + "place one in /etc or /usr/local/etc.");
            return;
        }
        Pattern commentLine = Pattern.compile("#");
        Pattern whiteSpace = Pattern.compile("\\s+");
        try {
            BufferedReader configReader = new BufferedReader(new FileReader(configFile));
            String line;
            while ((line = configReader.readLine()) != null) {
                if (commentLine.matcher(line).lookingAt()) {
                    continue;
                } else if (whiteSpace.matcher(line).lookingAt()) {
                    continue;
                } else {
                    String[] tuple = whiteSpace.split(line);
                    String[] keyVal = parseConfigOption(tuple);
                    cactiProperties.setProperty(keyVal[0], keyVal[1]);
                }
            }
        } catch (IOException e) {
            System.err.println("Can't read config file: " + e.getMessage());
        }
    }

    /**
     * Parse a config file option.  If there are less than two elements
     * in the inbound array, it's not a valid configuration entry.  If there
     * are two elements, the first is the option, the second is the value.
     * If there are more than two elements, the first is the option, the rest
     * make up the value.
     *
     * @param   tuple   a String array containing option and value.
     * @return  A string array.  Element 0 is the key, element 1 is the value.
     */
    public String[] parseConfigOption(String[] tuple) {
        String option;
        String value;
        if (tuple.length < 2) {
            option = "";
            value = "";
        } else if (tuple.length == 2) {
            option = tuple[0];
            value = tuple[1];
        } else {
            option = tuple[0];
            StringBuilder valueBuffer = new StringBuilder(tuple[1]);
            for (int i = 2; i < tuple.length; i++) {
                valueBuffer.append(tuple[i]);
            }
            value = valueBuffer.toString();
        }
        String[] retVal = { option, value };
        return retVal;
    }

    /**
     * Initialize the connection to the Cacti database.  Currently this is
     * hardcoded to mysql.  this should throw exceptions rather than catch them
     *
     * @return  a java.sql.Connection object for the Cacti database.
     */
    public java.sql.Connection initDb() throws SQLException {
        System.setProperty("jdbc.drivers", "com.mysql.jdbc.Driver:org.postgresql.Driver");
        java.sql.Connection dbConnection = null;
        String dbType = cactiProperties.getProperty("DB_Type");
        if (dbType.equals("mysql")) {
            cactiProperties.setProperty("DB_Driver", "com.mysql.jdbc.Driver");
        } else if (dbType.equals("postgres")) {
            cactiProperties.setProperty("DB_Driver", "org.postgresql.Driver");
        }
        StringBuilder urlBuffer = new StringBuilder("jdbc:");
        urlBuffer.append(cactiProperties.getProperty("DB_Type"));
        urlBuffer.append("://");
        urlBuffer.append(cactiProperties.getProperty("DB_Host"));
        urlBuffer.append("/");
        urlBuffer.append(cactiProperties.getProperty("DB_Database"));
        urlBuffer.append("?user=");
        urlBuffer.append(cactiProperties.getProperty("DB_User"));
        urlBuffer.append("&password=");
        urlBuffer.append(cactiProperties.getProperty("DB_Pass"));
        if (dbType.equals("mysql")) {
            urlBuffer.append("&zeroDateTimeBehavior=convertToNull");
        }
        String url = urlBuffer.toString();
        cactiProperties.setProperty("DB_Url", url);
        dbConnection = DriverManager.getConnection(url);
        return dbConnection;
    }

    /**
     * Read the Cacti settings from the Cacti database.
     */
    public void getOptionsFromDb() {
        if (db == null) {
            System.err.println("DB Connection is null, can't read the " + "settings from the SQL database.");
            return;
        }
        getPollingItems();
        String query = "SELECT name, value FROM settings";
        ResultSet resultSet;
        try {
            resultSet = db.createStatement().executeQuery(query);
        } catch (SQLException e) {
            System.err.println("Can't query database, can't read the " + "settings from the SQL database.");
            return;
        }
        try {
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String value = resultSet.getString("value");
                cactiProperties.setProperty(name, value);
            }
        } catch (SQLException e) {
            System.err.println("Unexpected response from database query, " + "can't read at least part of the ResultSet from the " + "SQL Database.");
        }
    }

    /**
     * Get the devices out of the Cacti database/system.
     *
     * @param   first   The first device to get.  The number refers to
     * the Cacti device ID.
     * @param   last    The last device to get.  The number refers to
     * the Cacti device ID.
     * @return  An ArrayList of devices.
     */
    public List<IDevice> getDevices(int first, int last) throws SQLException, IOException {
        if (db == null) {
            return new ArrayList<IDevice>();
        }
        List<IDevice> devices = new ArrayList<IDevice>();
        String limitQuery;
        if ((first == 0) && (last == 0)) {
            limitQuery = "";
        } else {
            limitQuery = " and id >= " + first + " and id <= " + last;
        }
        this.pollingItems = getPollingItems();
        String query = "SELECT * from host where disabled = '' " + limitQuery + " ORDER by id";
        ResultSet resultSet;
        resultSet = db.createStatement().executeQuery(query);
        DeviceFactory factory = DeviceFactory.getDefault(cactiProperties);
        while (resultSet.next()) {
            IDevice d = factory.getDevice(resultSet);
            d.addPollingItems(getPollingItems(d.getId()));
            devices.add(d);
        }
        return devices;
    }

    /**
     * Get all polling items for a particular device.
     *
     * @param   deviceId    The device to check for.
     * @return  A list of all polling items for this device.
     */
    public List<IPollingItem> getPollingItems(int deviceId) {
        List<IPollingItem> items = new ArrayList<IPollingItem>();
        for (IPollingItem item : pollingItems) {
            if (item == null) {
                continue;
            }
            if (deviceId == item.getHostId()) {
                items.add(item);
            }
        }
        return items;
    }

    /**
     * Get all polling items from the cacti instance.
     *
     * @return  A list of all polling items for this cacti instance.
     */
    public List<IPollingItem> getPollingItems() {
        String query = "SELECT * from poller_item";
        List<IPollingItem> items = new ArrayList<IPollingItem>();
        PollingItemFactory factory = PollingItemFactory.getDefault();
        try {
            ResultSet resultSet = db.createStatement().executeQuery(query);
            while (resultSet.next()) {
                IPollingItem item = factory.getPollingItem(resultSet);
                if (item != null) {
                    items.add(item);
                }
            }
        } catch (SQLException e) {
            if (logger.isEnabledFor(Level.WARN)) {
                logger.warn("Error getting polling items.  SQL Exception was " + e.getMessage());
            }
            return new ArrayList<IPollingItem>();
        }
        return items;
    }
}
