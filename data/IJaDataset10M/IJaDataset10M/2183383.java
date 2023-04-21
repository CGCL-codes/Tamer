package com.lovejoy.jabapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;
import com.lovejoy.jabapper.config.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class is the starting point for all bapi executions.
 * This singleton declares two <code>execute()</code> methods
 * and a getter/setter pair for the <code>Configuration</code>
 * type. 
 * 
 * TODO: create a Listener for every critical action
 * performed by this manager and all executions 
 * TODO: create a Exception Handler for exceptions raised 
 * during bapi executions 
 *
 * @author  (latest modification by $Author: roman_garcia $).
 * @version $Revision: 1.3 $ $Date: 2005/04/27 21:09:19 $
 */
public abstract class Jabapper {

    /** the manager logger. */
    protected static final Log LOG = LogFactory.getLog(Jabapper.class);

    /** the default properties file path. */
    protected static final String DEFAULT_PROPERTIES = "/jabapper.properties";

    /** the XML file property key. */
    protected static final String MANAGER_CONFIG_KEY = "jabapper.manager.xml.file";

    /** the default XML file path. */
    protected static final String DEFAULT_CONFIG = "/jabapper.xml";

    /** the default class name for manager implementation. */
    protected static final String DEFAULT_MANAGER = "com.lovejoy.jabapper.impl.JabapperImpl";

    /** the manager implementing class property key. */
    protected static final String MANAGER_CLASS_KEY = "jabapper.manager.class";

    /** the executor implementing class property key. */
    protected static final String EXECUTOR_CLASS_KEY = "jabapper.manager.executor.class";

    /** the singleton instance. */
    private static Jabapper instance;

    /** the manager properties. */
    protected static Properties properties;

    /** the manager config (as read from XML file). */
    protected Configuration configuration;

    /**
	 * Default Constructor. Protected.
	 */
    protected Jabapper() {
        super();
    }

    /**
	 * When called, this singleton-implemented method will, if necessary, create a new Jabapper
	 * instance for a className defined in: <br>
	 * 1. a System Property called {@link #MANAGER_CLASS_KEY}<br>
	 * 2. a line on the {@link #DEFAULT_PROPERTIES}file, keyed {@link #MANAGER_CLASS_KEY}<br>
	 * 3. a default implementation, {@link #DEFAULT_MANAGER}<br>
	 *
	 * @return the requested Jabapper instance (singleton)
	 *
	 * @throws JabapperException if a PersistenceManager couldn't be created
	 */
    public static synchronized Jabapper getInstance() throws JabapperException {
        if (instance != null) {
            return instance;
        }
        String className = null;
        Properties p = loadProperties();
        className = System.getProperty(MANAGER_CLASS_KEY);
        if (className != null) {
            return getInstance(className, p);
        }
        if ((properties != null) && !properties.isEmpty()) {
            className = getProperty(MANAGER_CLASS_KEY);
            if (className != null) {
                return getInstance(className, p);
            }
        }
        return getInstance(DEFAULT_MANAGER, p);
    }

    /**
	 * will return a Jabapper implemented in <code>className</code>.
	 *
	 * @param className the full class path pointing to a valid <code>Jabapper</code>.
	 * @param properties DOCUMENT ME!
	 *
	 * @return the requested Jabapper.
	 *
	 * @throws JabapperException if the requested Jabapper couldn't be created.
	 */
    protected static synchronized Jabapper getInstance(String className, Properties properties) throws JabapperException {
        if (instance == null) {
            instance = createManager(className);
            instance.configure(properties);
            if (instance == null) {
                LOG.fatal("Cannot create a Jabapper.");
                throw new JabapperException("Fatal Error creating " + className);
            }
        }
        return instance;
    }

    /**
	 * Creates a new Jabapper for the defined class Name. This method will be called only once.
	 *
	 * @param className the class name to instance
	 *
	 * @return the Jabapper for <code>className</code>
	 *
	 * @throws JabapperException when the Jabapper couldn't be instanced.
	 */
    private static Jabapper createManager(String className) throws JabapperException {
        Object instance;
        try {
            instance = Class.forName(className).newInstance();
            if (!(instance instanceof Jabapper)) {
                instance = null;
                throw new IllegalArgumentException("Not a Jabapper: " + className);
            }
        } catch (InstantiationException e) {
            throw new JabapperException("Couldn't instance the Jabapper " + className);
        } catch (IllegalAccessException e) {
            throw new JabapperException("Illegal Access attempting to instance " + className);
        } catch (ClassNotFoundException e) {
            throw new JabapperException("Couldn't find the class " + className);
        }
        return (Jabapper) instance;
    }

    /**
	 * Load and parse the XML file.
	 *
	 * @param properties DOCUMENT ME!
	 *
	 * @throws JabapperException If an error happen
	 */
    protected abstract void configure(Properties properties) throws JabapperException;

    /**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 *
	 * @throws JabapperException DOCUMENT ME!
	 */
    protected static Properties loadProperties() throws JabapperException {
        InputStream in = null;
        properties = new Properties();
        try {
            in = Jabapper.class.getResourceAsStream(DEFAULT_PROPERTIES);
            if (in != null) {
                properties.load(in);
            }
        } catch (IOException e) {
            LOG.warn("Unable to load properties from " + DEFAULT_PROPERTIES, e);
            throw new JabapperException(e);
        }
        return properties;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @return Returns the configuration.
	 */
    protected Configuration getConfiguration() {
        return configuration;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param configuration The configuration to set.
	 */
    protected void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param key DOCUMENT ME!
	 *
	 * @return String DOCUMENT ME!
	 */
    protected static String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param key DOCUMENT ME!
	 * @param def DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
    protected static String getProperty(String key, String def) {
        return properties.getProperty(key, def);
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param bapiName DOCUMENT ME!
	 * @param context DOCUMENT ME!
	 *
	 * @throws JabapperException If an error happened calling this method.
	 */
    public abstract void execute(String bapiName, Context context) throws JabapperException;

    /**
	 * DOCUMENT ME!
	 *
	 * @param bapiName DOCUMENT ME!
	 * @param context DOCUMENT ME!
	 * @param locale DOCUMENT ME!
	 *
	 * @throws JabapperException If an error happened calling this method.
	 */
    public abstract void execute(String bapiName, Context context, Locale locale) throws JabapperException;

    /**
	 * @see Object#toString()
	 */
    public String toString() {
        return getConfiguration().toString();
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param msg DOCUMENT ME!
	 * @param t DOCUMENT ME!
	 *
	 * @throws JabapperException If an error happened calling this method.
	 */
    protected void handleException(String msg, Throwable t) throws JabapperException {
        LOG.error(msg, t);
        throw new JabapperException(msg, t);
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param msg DOCUMENT ME!
	 *
	 * @throws JabapperException If an error happened calling this method.
	 */
    protected void handleException(String msg) throws JabapperException {
        LOG.error(msg, null);
        throw new JabapperException(msg);
    }
}
