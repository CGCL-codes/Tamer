package edu.uga.galileo.voci.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import javax.servlet.ServletContext;
import edu.uga.galileo.voci.db.ConnectionPool;
import edu.uga.galileo.voci.db.ConnectionPoolFactory;
import edu.uga.galileo.voci.exception.NoAvailablePoolException;
import edu.uga.galileo.voci.logging.Logger;

/**
 * Holds all application context, connection pool, and other information, and
 * makes it available via static methods to all classes in the system. This
 * class is instantiated by the {@link edu.uga.galileo.voci.servlet.Controller}
 * servlet at startup time.
 * 
 * @author <a href="mailto:mdurant@uga.edu">Mark Durant</a>
 * @version 1.0
 */
public class Configuration {

    /**
	 * Holds the configuration information.
	 */
    private static HashMap<String, String> config = new HashMap<String, String>();

    private static ServletContext context;

    private static ConnectionPool pool;

    public static Set<String> getKeys() {
        return config.keySet();
    }

    /**
	 * Add configuration data. This method only takes data in
	 * <code>String</code> form, but the <code>get...</code> methods will
	 * return that data in <code>int</code>, <code>String</code>, or
	 * <code>java.util.Date</code> form.
	 * 
	 * @param name
	 *            The name of the key to store.
	 * @param value
	 *            The value to store with the <code>name</code> key.
	 */
    public static void addConfigValue(String name, String value) {
        config.put(name, value);
    }

    /**
	 * Get a configuration value by key in <code>int</code> form.
	 * 
	 * @param name
	 *            The key to retrieve data for.
	 * @return The <code>int</code> value associated with the requested value,
	 *         or <code>-1</code> if it's either not found, or a
	 *         <code>NumberFormatException</code> is encountered.
	 */
    public static int getInt(String name) {
        if (config.containsKey(name)) {
            try {
                return Integer.parseInt(config.get(name));
            } catch (NumberFormatException nme) {
                Logger.warn("Number format exception caught retrieving config for '" + name + "'");
            }
        }
        return -1;
    }

    /**
	 * Get a configuration value by key in <code>String</code> form.
	 * 
	 * @param name
	 *            The key to retrieve data for.
	 * @return The <code>String</code> value associated with the requested
	 *         value, or <code>null</code> if it's not found.
	 */
    public static String getString(String name) {
        if (config.containsKey(name)) {
            return config.get(name);
        }
        return null;
    }

    /**
	 * Get a configuration value by key in <code>Date</code> form. Dates
	 * should be entered in <code>SimpleDateFormat's</code> "EEE MMM dd
	 * HH:mm:ss z yyyy" format (e.g., "Fri Feb 17 13:05:24 EDT 2006").
	 * 
	 * @param name
	 *            The key to retrieve data for.
	 * @return The <code>Date</code> value associated with the requested
	 *         value, or <code>null</code> if it's either not found, or a an
	 *         exception occurs parsing the format.
	 */
    public static Date getDate(String name) {
        return getDate(name, "EEE MMM dd HH:mm:ss z yyyy");
    }

    /**
	 * Get a configuration value by key in <code>Date</code> form, specifying
	 * a <code>SimpleDateFormat</code>-style <code>String</code> value to
	 * use in parsing the value.
	 * 
	 * @param name
	 *            The key to retrieve data for.
	 * @param format
	 *            The format to use in creating the
	 *            <code>SimpleDateFormat</code> that will parse the value.
	 * @return The <code>Date</code> value associated with the requested
	 *         value, or <code>null</code> if it's either not found, or a an
	 *         exception occurs parsing the format.
	 */
    public static Date getDate(String name, String format) {
        if (config.containsKey(name)) {
            try {
                return (new SimpleDateFormat(format)).parse(config.get(name));
            } catch (ParseException pe) {
                Logger.warn("SimpleDateFormat exception caught retrieving config for '" + name + "'");
            }
        }
        return null;
    }

    /**
	 * Get the <code>ServletContext</code>.
	 * 
	 * @return The <code>ServletContext</code>
	 */
    public static ServletContext getServletContext() {
        return context;
    }

    /**
	 * Set the <code>ServletContext</code>.
	 * 
	 * @param context
	 *            The <code>ServletContext</code>
	 */
    public static void setServletContext(ServletContext context) {
        Configuration.context = context;
    }

    /**
	 * Get the connection pool.
	 * 
	 * @return The connection pool.
	 */
    public static ConnectionPool getConnectionPool() {
        if (pool == null) {
            try {
                pool = ConnectionPoolFactory.getConnectionPool();
            } catch (NoAvailablePoolException e) {
                Logger.fatal("Couldn't get a connection pool", e);
                System.exit(-1);
            }
        }
        return pool;
    }

    /**
	 * Set the connection pool.
	 * 
	 * @param pool
	 *            The connection pool.
	 */
    public static void setConnectionPool(ConnectionPool pool) {
        Configuration.pool = pool;
    }
}
