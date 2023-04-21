package com.ncs.flex.server.helper;

import org.apache.log4j.Logger;
import java.io.InputStream;
import java.util.Properties;

/**
 * Provides a convenience class for applications to read from properties
 * file with key-value pair
 * 
 * @since 3.3
 */
public class ApplicationProperties {

    private String CONFIG_FILE_NAME = null;

    /**
	 * Resource bundle to handle the properties file
	 */
    private Properties oProperties = null;

    private static Logger log = Logger.getLogger(ApplicationProperties.class);

    /**
	 * Default constructor
	 */
    public ApplicationProperties(String sConfigFileName) {
        CONFIG_FILE_NAME = sConfigFileName;
    }

    /**
	 * Loads the properties fromt he config file.
	 */
    protected void loadProperties() {
        try {
            oProperties = new Properties();
            ClassLoader oClassLoader = Thread.currentThread().getContextClassLoader();
            InputStream is = oClassLoader.getResourceAsStream(CONFIG_FILE_NAME + ".properties");
            if (is != null) {
                oProperties.load(is);
                is.close();
            }
            is = null;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
	 * Retrieves the property value as a String for the specified
	 * property name
	 *
	 * @param sPropertyName property name
	 * @param sDefaultValue return this value if property not found
	 * @return property value as a string of property name
	 */
    public String getPropertyAsString(String sPropertyName, String sDefaultValue) {
        try {
            if (oProperties == null) {
                loadProperties();
            }
            return oProperties.getProperty(sPropertyName, sDefaultValue);
        } catch (Exception e) {
            return sDefaultValue;
        }
    }

    /**
	 * Retrieves the property value as an integer for the specified
	 * property name
	 *
	 * @param sPropertyName property name
	 * @param iDefaultValue return this value if property not found
	 * @return property value as an integer of property name
	 */
    public int getPropertyAsInt(String sPropertyName, int iDefaultValue) {
        try {
            if (oProperties == null) {
                loadProperties();
            }
            String sProperty = oProperties.getProperty(sPropertyName);
            return Integer.parseInt(sProperty);
        } catch (Exception e) {
            return iDefaultValue;
        }
    }
}
