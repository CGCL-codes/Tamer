package de.psychomatic.mp3db.utils;

import java.io.File;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

/**
 * Configuration
 * @author Kykal
 */
public class Config {

    /**
     * Creates the Configuration-singleton
     * @return Instance of Config
     */
    private static Config getInstance() {
        if (_instance == null) {
            synchronized (Config.class) {
                if (_instance == null) {
                    _instance = new Config();
                }
            }
        }
        return _instance;
    }

    /**
     * Returns the Configuration for Torque
     * @return The configuration
     */
    public static Configuration getConfiguration() {
        return getInstance()._props;
    }

    /**
     * Get the Path to CD-Drive
     * @return Fileobject set CD-Drive
     */
    public static File getCdRoot() {
        return getInstance().getCdPath();
    }

    /**
     * Changes a config-value
     * @param key Key to changes
     * @param value New value
     */
    public static void setConfig(String key, String value) {
        getInstance().setProperty(key, value);
    }

    /**
     * Read a value of the configuration
     * @param key Key to read
     * @return Existing value or null
     */
    public static String getConfig(String key) {
        return getInstance().getProperty(key);
    }

    /**
     * Changes the path to CD-Drive
     * @param root New path to CD-Drive
     */
    public static void setCdRoot(File root) {
        getInstance().setCdPath(root);
    }

    /**
     * Instance of Config
     */
    private static Config _instance;

    /**
     * Creates an instance of Config
     */
    private Config() {
        _props = new PropertiesConfiguration();
        _propertyFile = new File("mp3db.properties");
        try {
            _props.load(_propertyFile);
        } catch (Exception e) {
            _props.setProperty("torque.database.default", "mp3db");
            _props.setProperty("torque.dsfactory.mp3db.factory", "org.apache.torque.dsfactory.SharedPoolDataSourceFactory");
            _props.setProperty("torque.dsfactory.mp3db.pool.testOnBorrow", "true");
            _props.setProperty("torque.dsfactory.mp3db.pool.validationQuery", "SELECT 1");
            _log.debug("Propertiesdatei nicht gefunden, lade Default-Werte");
        }
    }

    /**
     * Returns the path to CD-Drive
     * @return Path to CD-Drive
     */
    private File getCdPath() {
        String cdpath = _props.getString("cdpath");
        if (cdpath == null) return null;
        return new File(FilenameUtils.separatorsToSystem(cdpath));
    }

    /**
     * Changes to path to CD-Drive
     * @param path New path to CD-Drive
     */
    private void setCdPath(File path) {
        setProperty("cdpath", FilenameUtils.separatorsToUnix(path.getAbsolutePath()));
    }

    /**
     * Changes a key in configuration
     * @param key Key to change
     * @param value New value
     */
    private void setProperty(String key, String value) {
        _props.setProperty(key, value);
        try {
            _props.save(_propertyFile);
        } catch (ConfigurationException e) {
            _log.error("setProperty(String, String)", e);
        }
    }

    /**
     * Read a property from the configuration
     * @param key Key to read
     * @return Value or null
     */
    private String getProperty(String key) {
        return _props.getString(key);
    }

    /**
     * Configuration
     */
    private PropertiesConfiguration _props;

    /**
     * Configurationfile
     */
    private File _propertyFile;

    /**
     * Logger for this class
     */
    private Logger _log = Logger.getLogger(Config.class);
}
