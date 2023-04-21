package org.esigate;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

/**
 * Factory class used to configure and retrieve {@linkplain Driver} INSTANCIES.
 * 
 * @author Stanislav Bernatskyi
 */
public class DriverFactory {

    private static final String DEFAULT_INSTANCE = "default";

    private static final Map<String, Driver> INSTANCIES = new HashMap<String, Driver>();

    static {
        configure();
    }

    private DriverFactory() {
    }

    /** Loads all instancies according to default configuration file */
    public static final void configure() {
        InputStream inputStream = Driver.class.getResourceAsStream("driver.properties");
        InputStream extInputStream = DriverFactory.class.getClassLoader().getResourceAsStream("driver-ext.properties");
        try {
            Properties merged = new Properties();
            if (inputStream != null) {
                Properties props = new Properties();
                props.load(inputStream);
                merged.putAll(props);
            }
            if (extInputStream != null) {
                Properties extProps = new Properties();
                extProps.load(extInputStream);
                merged.putAll(extProps);
            }
            configure(merged);
        } catch (IOException e) {
            throw new ConfigurationException("Error loading configuration", e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (extInputStream != null) {
                    extInputStream.close();
                }
            } catch (IOException e) {
                throw new ConfigurationException("failed to close stream", e);
            }
        }
    }

    /**
	 * Loads all instancies according to the properties parameter
	 * 
	 * @param props
	 *            properties to use for configuration
	 */
    public static final void configure(Properties props) {
        HashMap<String, Properties> driversProps = new HashMap<String, Properties>();
        for (Enumeration<?> enumeration = props.propertyNames(); enumeration.hasMoreElements(); ) {
            String propertyName = (String) enumeration.nextElement();
            String prefix;
            String name;
            int idx = propertyName.lastIndexOf('.');
            if (idx < 0) {
                prefix = DEFAULT_INSTANCE;
                name = propertyName;
            } else {
                prefix = propertyName.substring(0, idx);
                name = propertyName.substring(idx + 1);
            }
            Properties driverProperties = driversProps.get(prefix);
            if (driverProperties == null) {
                driverProperties = new Properties();
                driversProps.put(prefix, driverProperties);
            }
            driverProperties.put(name, props.getProperty(propertyName));
        }
        synchronized (INSTANCIES) {
            INSTANCIES.clear();
            for (Entry<String, Properties> entry : driversProps.entrySet()) {
                configure(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
	 * Registers new {@linkplain Driver} under provided name with specified
	 * properties.
	 */
    public static void configure(String name, Properties props) {
        String effectiveName = name;
        if (effectiveName == null) {
            effectiveName = DEFAULT_INSTANCE;
        }
        synchronized (INSTANCIES) {
            INSTANCIES.put(effectiveName, new Driver(name, props));
        }
    }

    /**
	 * Retrieves the default instance of this class that is configured according
	 * to the properties file (driver.properties)
	 * 
	 * @return the default instance
	 */
    public static final Driver getInstance() {
        return getInstance(DEFAULT_INSTANCE);
    }

    /**
	 * Retrieves the default instance of this class that is configured according
	 * to the properties file (driver.properties)
	 * 
	 * @param instanceName
	 *            The name of the instance (corresponding to the prefix in the
	 *            driver.properties file)
	 * 
	 * @return the named instance
	 */
    public static final Driver getInstance(String instanceName) {
        String effectiveInstanceName = instanceName;
        if (effectiveInstanceName == null) {
            effectiveInstanceName = DEFAULT_INSTANCE;
        }
        synchronized (INSTANCIES) {
            if (INSTANCIES.isEmpty()) {
                throw new ConfigurationException("Driver has not been configured and driver.properties file was not found");
            }
            Driver instance = INSTANCIES.get(effectiveInstanceName);
            if (instance == null) {
                throw new ConfigurationException("No configuration properties found for factory : " + effectiveInstanceName);
            }
            return instance;
        }
    }

    /**
	 * Method used to inject providers. Usefull mainly for unit testing purpose
	 * 
	 * @param instanceName
	 *            The name of the provider
	 * @param instance
	 *            The instance
	 */
    public static final void put(String instanceName, Driver instance) {
        synchronized (INSTANCIES) {
            INSTANCIES.put(instanceName, instance);
        }
    }
}
