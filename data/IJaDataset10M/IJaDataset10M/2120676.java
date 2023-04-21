package org.personalsmartspace.sre.api.pss3p;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

/**
 * A Utility class for reading and writing to the file referenced by
 * CONFIG_FILE_LOCATION.
 * 
 */
public class PSSConfigUtil {

    /**
     * Private constructor
     */
    private PSSConfigUtil() {
    }

    /**
     * Reads the configuration information from the default location "./pssconfig.properties"
     * @return Name/Value Properties collection
     */
    public static Properties getConfigurationProps() {
        return getConfigurationProps(PssConstants.CONFIG_FILE_LOCATION);
    }

    /**
     * Reads the configuration information from the default location "./pssconfig.properties"
     * @param fileLocation overrides the default location with new path to the config file
     * @return Name/Value Properties collection
     */
    public static Properties getConfigurationProps(String fileLocation) {
        Properties config = new Properties();
        try {
            config.load(new FileInputStream(fileLocation));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return config;
    }

    /**
     * Saves the properties back to the default configuration file
     * @param props The name/value Properties collection
     */
    public static void setConfigurationProps(Properties props) {
        setConfigurationProps(props, PssConstants.CONFIG_FILE_LOCATION);
    }

    /**
     * Saves the properties back to the default configuration file
     * @param props The name/value Properties collection
     * @param fileLocation overrides the default location with a new path to the config file
     */
    public static void setConfigurationProps(Properties props, String fileLocation) {
        try {
            props.store(new FileOutputStream(fileLocation), "");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
