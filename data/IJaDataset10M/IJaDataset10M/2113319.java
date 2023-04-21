package org.lacson.utils;

import java.util.*;
import java.io.*;

/**
 * PropertyLoader class used to load .properties file
 * into system
 *
 * @author Patrick Lacson
 */
public class PropertyLoader {

    public PropertyLoader() {
        isPropLoaded = false;
    }

    /**
   * set the File location
   *
   * @param file relative or absolute file path to .properties location
   */
    public void loadFile(String file) {
        file_location = file;
        try {
            init();
            isPropLoaded = true;
        } catch (FileNotFoundException fe) {
            System.err.println("File " + file + " not found!");
        }
        useFile(file);
    }

    /**
   * Check if properties file location has been loaded
   *
   * @return boolean - true if property file loaded, false if not
   */
    public boolean isLoaded() {
        return this.isPropLoaded;
    }

    /**
   * Get Properties instance of loaded values
   *
   * @return Properties instance of the loaded class properties
   */
    public Properties getProperties() {
        return this.dbProps;
    }

    /**
   * Get Enumeration instance of loaded values
   *
   * @return Enumeration instance of the loaded class properties
   */
    public Enumeration getPropertyNames() {
        return dbProps.propertyNames();
    }

    /**
   * Get key value of the passed key_name parameter
   *
   * @param key_name - String type of the key name to get
   * @return String type of the value pair
   */
    public String getKey(String key_name) {
        return (String) dbProps.getProperty(key_name);
    }

    /**
   * Get key value of the passed key_name parameter
   *
   * @param key_name - String type of the key name to get
   * @param k_default - String default value, if key is undefined
   * @return String type of the value pair
   */
    public String getKey(String key_name, String k_default) {
        return dbProps.getProperty(key_name, k_default);
    }

    /**
   * Get key value as int of the passed key_name parameter
   *
   * @param key_name - String type of the key name to get
   * @param k_default - String default value, if key is undefined
   * @return int type of the value pair
   * @exception NumberFormatException if value of key is not a numeric value
   */
    public int getInt(String key_name, int k_default) throws NumberFormatException {
        try {
            int temp;
            String key = dbProps.getProperty(key_name);
            temp = java.lang.Integer.parseInt(key);
            return temp;
        } catch (NumberFormatException nfe) {
            System.out.println("NumerFormatException caught: " + nfe);
        }
        return k_default;
    }

    /**
   * Debugging tool to show all name/value pairs in properties
   * and prints to Standard Output
   */
    public void showCollection() {
        dbProps.list(System.out);
    }

    public String getInfo() {
        return "This bean is a properties bean used to load files into the property space.";
    }

    /**
   * Store a temporary value to the local temp data member
   *
   * @param val String type of the temporary value assigned to
   * the temp data member
   */
    public void setTemp(String val) {
        temp = val;
    }

    /**
   * Get the temporary value assigned to the local temp data member
   *
   * @return String type value of the temp data member
   */
    public String getTemp() {
        return temp;
    }

    /**
   * Get the file location of the used .properties file
   *
   * @return String value of the absolute file path of the .properties
   * file used
   */
    public String getFileLocation() {
        return (this.file_location != null) ? this.file_location : null;
    }

    private void useFile(String f) {
        file_location = f;
    }

    private void init() throws FileNotFoundException {
        is = new FileInputStream(file_location);
        dbProps = new Properties();
        try {
            dbProps.load(is);
        } catch (Exception e) {
            System.err.println("File not found!");
        }
    }

    private String file_location;

    private String temp;

    private InputStream is;

    private Properties dbProps;

    private boolean isPropLoaded;
}
