package neembuuuploader.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import neembuuuploader.NeembuuUploader;

/** This class is used to store the user specific properties like his username
 * and password on a .nuproperties file in the user's home folder.
 * 
 * There will be different .nuproperties file for different users whereas there
 * will be one settings.dat common for all users.
 *
 * @author vigneshwaran
 */
public class NeembuuUploaderProperties {

    public static final File propertyfile = new File(System.getProperty("user.home") + File.separator + ".nuproperties");

    private static Properties properties = new Properties();

    public static String comment = "Neembuu Uploader Properties";

    /** Static Initializer - Create new file with default parameters 
     * if nothing exists */
    static {
        if (!propertyfile.exists()) {
            try {
                NULogger.getLogger().info("Creating new properties file");
                propertyfile.createNewFile();
                setDefaultProperties();
            } catch (IOException ex) {
                NULogger.getLogger().log(Level.INFO, "Exception while creating property file: {0}", ex);
            }
        }
    }

    /**
     * Set up the properties object. It loads the properties from the file.
     * If the file doesn't exist, it will be created by the static initializer before call to this method executes.
     * If the file exists but in different version, then clear the properties and set the default properties again.
     * (no need to delete the file and create.. just clear the properties and set default)
     */
    public static void setUp() {
        NULogger.getLogger().info("Setting up properties..");
        loadProperties();
        if (!getProperty("version").equals(Float.toString(NeembuuUploader.version))) {
            properties.clear();
            setDefaultProperties();
        }
    }

    /**
     * Sets default properties and overwrites the properties to the file.
     */
    private static void setDefaultProperties() {
        setProperty("version", Float.toString(NeembuuUploader.version));
        setProperty("firstlaunch", "true");
        storeProperties();
    }

    /**
     * Write the properties to property file
     */
    public static void storeProperties() {
        NULogger.getLogger().fine("Storing Properties");
        try {
            properties.store(new FileOutputStream(propertyfile), comment);
        } catch (FileNotFoundException ex) {
            NULogger.getLogger().log(Level.INFO, "Properties file not found: {0}", ex);
        } catch (IOException ex) {
            NULogger.getLogger().log(Level.INFO, "IOException while writing property file {0}", ex);
        }
        NULogger.getLogger().fine("Properties stored successfully");
    }

    /**
     * Load the properties from property file
     */
    public static void loadProperties() {
        NULogger.getLogger().fine("Loading Properties");
        try {
            properties.load(new FileInputStream(propertyfile));
        } catch (FileNotFoundException ex) {
            NULogger.getLogger().log(Level.INFO, "Properties file not found: {0}", ex);
        } catch (IOException ex) {
            NULogger.getLogger().log(Level.INFO, "IOException while reading property file {0}", ex);
        }
        NULogger.getLogger().fine("Properties loaded successfully");
    }

    /**Sets the specified key and value
     * For passwords, use setEncryptedProperty
     * @param key
     * @param value 
     */
    public static void setProperty(String key, String value) {
        loadProperties();
        properties.setProperty(key, value);
        storeProperties();
    }

    /**
     * Sets the specified key and the value.
     * Value will be encrypted before getting stored.
     * 
     * If you are not storing confidential data, use setProperty instead
     * 
     * @param key
     * @param value 
     */
    public static void setEncryptedProperty(String key, String value) {
        loadProperties();
        if (value.isEmpty()) {
            properties.setProperty(key, value);
        }
        properties.setProperty(key, Encrypter.encrypt(value));
        storeProperties();
    }

    private NeembuuUploaderProperties() {
    }

    /**Get the value of a specified property
     * For passwords, call getEncryptedProperty
     * @param key The property key
     * @return The value of the given property if exists. If none exist, return ""
     */
    public static String getProperty(String key) {
        return properties.getProperty(key, "");
    }

    /**Get the value of a specified property. The stored value must have been encrypted.
     * So it'll be decrypted and returned.
     * 
     * For unencrypted values, use getProperty() instead.
     * 
     * @param key The property key
     * @return The value of the given property if exists, If none exist, return ""
     */
    public static String getEncryptedProperty(String key) {
        String value = properties.getProperty(key, "");
        if (value.isEmpty()) return "";
        return Encrypter.decrypt(value);
    }

    /**Get the value of a specified property
     * For passwords, call getEncryptedProperty
     * @param key The property key
     * @param defaultValue A default value to return if none exists
     * @return The value of the given property if exists. If none exists, specified defaultValue will be returned.
     */
    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /**If a value stored is a boolean, then use this method 
     * instead of using Boolean.valueOf(getProperty(key))
     * 
     * @param key The Key of the property
     * @return A boolean value that indicates whether the property has a true
     * value or not
     */
    public static boolean isPropertyTrue(String key) {
        return Boolean.valueOf(properties.getProperty(key, "false"));
    }

    /**
     * No need to use this unless there is some reason..
     * @return The property file
     */
    public static File getPropertyfile() {
        return propertyfile;
    }
}
