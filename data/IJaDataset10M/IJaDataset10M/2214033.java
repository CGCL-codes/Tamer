package be.roam.drest.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Configuration class for drest.
 */
public class PropertiesConfiguration {

    /**
     * The default properties file.
     */
    private static final String DEFAULT_PROPERTIES_FILE = "drest.properties";

    /**
     * Log.
     */
    private static Log log = LogFactory.getLog(PropertiesConfiguration.class);

    /**
     * Singleton instance.
     */
    private static PropertiesConfiguration instance = new PropertiesConfiguration();

    /**
     * Configuration properties.
     */
    private Properties properties;

    /**
     * Retrieves the singleton instance.
     *
     * @return the singleton instance of this class
     */
    public static PropertiesConfiguration getInstance() {
        return instance;
    }

    /**
     * Loads a properties file from the given input stream.
     *
     * @param inputStream input stream containing the properties
     *
     * @throws IOException
     */
    public void load(InputStream inputStream) throws IOException {
        properties = new Properties();
        properties.load(inputStream);
    }

    /**
     * Sets the properties.
     *
     * @param properties properties to set
     */
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    /**
     * Retrieves the value associated with the given key.
     *
     * @param key key for the value
     *
     * @return the value associated with the given key
     */
    public String getValue(String key) {
        if (properties == null) {
            loadDefaults();
        }
        return properties.getProperty(key);
    }

    /**
     * Sets the property with the given key to the given value.
     * <p>
     * Can be used to override default settings.
     * </p>
     * @param key key of the property
     * @param value value of the property
     */
    public void setValue(String key, String value) {
        if (properties == null) {
            properties = new Properties();
        }
        properties.setProperty(key, value);
    }

    private void loadDefaults() {
        try {
            load(getClass().getClassLoader().getResourceAsStream(DEFAULT_PROPERTIES_FILE));
        } catch (Exception e) {
            log.fatal("Could not load default properties file for drest: " + DEFAULT_PROPERTIES_FILE, e);
            throw new IllegalArgumentException("Could not load default properties file for drest: " + DEFAULT_PROPERTIES_FILE);
        }
    }

    /**
     * Private constructor.
     */
    private PropertiesConfiguration() {
        super();
    }
}
