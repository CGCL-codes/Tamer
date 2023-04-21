package org.tigr.antware.shared.idgen;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.tigr.antware.shared.exceptions.IDGenerationException;
import org.tigr.antware.shared.util.ExceptionHandler;
import org.tigr.antware.shared.util.Logger;

/**
 * The class <code>DBIDGenerator</code> is a unique identifier generator class
 * that relies on a database server stored procedure to return the next
 * available batch of unique IDs. The class calls the stored procedure
 * <i>get_next_id</i> using the database name specified by the property
 * <i>id.dbserver.database</i> using the URL specified by the property
 * <i>id.dbserver.url</i> and the driver specified with the property
 * <i>id.dbserver.driver</i> The class establishes a connection with the
 * database server and retrieves a batch of IDs specified by the property
 * <i>id.batch.size</i>.
 * 
 * If there is a prolonged level of inactivity then the connection to the
 * database is closed and reopened when a subsequent request is made. The time
 * of inactivity is specified by the property
 * <i>id.dbserver.inactive.timeout</i> in seconds. The default is 15 seconds.
 */
public abstract class DBIDGenerator extends BaseIDGenerator {

    private static Logger logger = new Logger(DBIDGenerator.class);

    /**
     * This variable <code>connections</code> holds the persistent connection to the
     * database.
     */
    private Connection connection;

    private static String url;

    private static String driver;

    private static String username;

    private static String password;

    private static String name;

    private static int inactivity = 15000;

    private Thread closeConnThread;

    private Map<String, String> additionalProps;

    private static boolean monitorInactivity = false;

    /**
     * The variable <code>lockObject</code> holds an object used to synchronize operation in this class.
     */
    private Object lockObject = new Object();

    static {
        url = conf.getProperty("id.dbserver.url");
        driver = conf.getProperty("id.dbserver.driver");
        name = conf.getProperty("id.dbserver.name");
        username = conf.getProperty("id.dbserver.username");
        password = conf.getProperty("id.dbserver.password");
        if (conf.getProperty("id.dbserver.inactive.timeout") != null) {
            inactivity = Integer.parseInt((String) conf.getProperty("id.dbserver.inactive.timeout"));
            inactivity *= 1000;
            monitorInactivity = true;
            logger.debug("Inactivity monitor timeout specified. Value is: " + inactivity + " milliseconds. Will launch monitoring thread.");
        }
    }

    /**
     * A no-arguments constructor.
     * 
     * @throws IDGenerationException 
     * 
     */
    public DBIDGenerator() throws IDGenerationException {
        super();
        logger.debug("In the Constructor");
        additionalProps = new HashMap<String, String>();
        String addPropsStr = conf.getProperty("id.dbserver.props");
        String[] addPropsArr = addPropsStr.split(";");
        for (int i = 0; i < addPropsArr.length; i++) {
            String prop = addPropsArr[i];
            String[] keyValuePair = prop.split("=");
            additionalProps.put(keyValuePair[0], keyValuePair[1]);
        }
    }

    /**
     * Method <code>getConnection</code> for getting a database connection
     * 
     * @return a {@link Connection} 
     * @throws IDGenerationException
     */
    protected synchronized Connection getConnection() throws IDGenerationException {
        if (logger.isFinerEnabled()) {
            logger.finer("Attempting to establish a connection to the database server.");
        }
        synchronized (lockObject) {
            if (connection == null) {
                if (logger.isInfoEnabled()) {
                    logger.info("Connecting to server " + name + " using URL: " + url + " user name: " + username + " driver name: " + driver);
                }
                try {
                    Class.forName(driver);
                } catch (ClassNotFoundException e) {
                    throw new IDGenerationException("Could not establish database connection.", e);
                }
                Properties props = new Properties();
                props.put("user", username);
                props.put("password", password);
                for (String key : additionalProps.keySet()) {
                    props.put(key, additionalProps.get(key));
                }
                try {
                    if (logger.isInfoEnabled()) {
                        logger.info("Getting Connecting from DriverManager.");
                    }
                    connection = DriverManager.getConnection(url, props);
                    if (logger.isInfoEnabled()) {
                        logger.info("Got Connection from DriverManager.");
                    }
                    closeConnThread = new CloseConnectionThread();
                } catch (SQLException e) {
                    logger.warn("Exception while getting the connecting from DriverManager." + e.getMessage());
                    throw new IDGenerationException("Could not establish database connection.", e);
                }
            }
        }
        if (monitorInactivity) {
            if (closeConnThread.isAlive()) {
                if (logger.isFinerEnabled()) {
                    logger.finer("Interrupting monitoring thread.");
                }
                closeConnThread.interrupt();
                closeConnThread = new CloseConnectionThread();
                closeConnThread.start();
            } else {
                closeConnThread.start();
            }
        }
        if (logger.isFinerEnabled()) {
            logger.finer("Established a connection to the database server.");
        }
        return connection;
    }

    protected void releaseConnection() throws SQLException {
        if (!monitorInactivity) {
            connection.close();
            connection = null;
        }
    }

    class CloseConnectionThread extends Thread {

        /**
         * The constructor <code>CloseConnectionThread</code> creates a new instance
         * 
         */
        public CloseConnectionThread() {
            super();
            if (DBIDGenerator.logger.isFinerEnabled()) {
                DBIDGenerator.logger.finer("Creating database inactivity monitoring thread.");
            }
        }

        @Override
        public void run() {
            if (DBIDGenerator.logger.isFinerEnabled()) {
                DBIDGenerator.logger.finer("Starting thread to monitor database inactivity.");
            }
            try {
                sleep(inactivity);
                if (DBIDGenerator.logger.isFinerEnabled()) {
                    DBIDGenerator.logger.finer("Finished waiting for the specified time. " + "Will close connection.");
                }
                synchronized (lockObject) {
                    try {
                        connection.close();
                        connection = null;
                    } catch (SQLException e1) {
                        ExceptionHandler.handleException(e1);
                    }
                }
                if (DBIDGenerator.logger.isFinerEnabled()) {
                    DBIDGenerator.logger.finer("Connection closed.");
                }
            } catch (InterruptedException e) {
            }
        }
    }
}
