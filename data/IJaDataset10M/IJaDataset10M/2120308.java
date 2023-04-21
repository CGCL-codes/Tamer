package net.sourceforge.pbeans;

import net.sourceforge.pbeans.data.*;
import java.io.*;
import java.util.*;
import java.util.logging.*;

public class StoreFactory {

    private static final Logger logger = Logger.getLogger(StoreFactory.class.getName());

    private static final StoreFactory instance = new StoreFactory();

    private static final String RES_PROPERTIES = "/pbeans.properties";

    private StoreFactory() {
    }

    public static StoreFactory getInstance() {
        return instance;
    }

    private Store cachedStore;

    /**
	 * Obtains a {@link Store} initialized with properties
	 * from resource '/pbeans.properties' or system properties. 
	 * The following properties must be provided:
	 * <ul>
	 * <li>driver.class.name
	 * <li>driver.url
	 * </ul>
	 */
    public Store getStore() throws StoreException {
        synchronized (this) {
            try {
                Store store = this.cachedStore;
                if (store == null) {
                    Properties props = new Properties();
                    InputStream stream = StoreFactory.class.getResourceAsStream(RES_PROPERTIES);
                    if (stream == null) {
                        if (logger.isLoggable(Level.INFO)) {
                            logger.info("getStore(): Resource " + RES_PROPERTIES + " not found; using system properties.");
                        }
                        props = System.getProperties();
                    } else {
                        if (logger.isLoggable(Level.INFO)) {
                            logger.info("getStore(): Loading properties from " + RES_PROPERTIES);
                        }
                        try {
                            props.load(stream);
                        } finally {
                            stream.close();
                        }
                    }
                    String driverClassName = props.getProperty("driver.class.name");
                    if (driverClassName == null) {
                        throw new java.lang.IllegalStateException("Property driver.class.name not found in resource " + RES_PROPERTIES + " or system properties.");
                    }
                    String connectionURL = props.getProperty("driver.url");
                    if (connectionURL == null) {
                        throw new java.lang.IllegalStateException("Property driver.url not found in resource " + RES_PROPERTIES + " or system properties.");
                    }
                    if (logger.isLoggable(Level.INFO)) {
                        logger.info("getStore(): driverClassName=" + driverClassName + ",connectionURL=" + connectionURL);
                    }
                    GenericDataSource gds = new GenericDataSource();
                    gds.setDriverClassName(driverClassName);
                    gds.setUrl(connectionURL);
                    store = new Store(gds);
                    this.cachedStore = store;
                }
                return store;
            } catch (IOException ioe) {
                throw new StoreException(ioe);
            }
        }
    }

    /**
	 * Clears cached store. Calling {@link #getStore()} will
	 * produce a new instance. 
	 */
    public void invalidate() {
        this.cachedStore = null;
    }
}
