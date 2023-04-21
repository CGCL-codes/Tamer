package de.psychomatic.mp3db.utils;

/**
 * Wrapper for databasedriver in GUI
 * @author Kykal
 */
public class DriverObject {

    /**
     * Creates a wrapper
     * @param driver String of driverclass
     * @param name Name of database
     * @param key Key in config
     */
    public DriverObject(String driver, String name, String key) {
        _driver = driver;
        _name = name;
        _key = key;
    }

    /**
     * Gets the string of the driverclass
     * @return Sting
     */
    public String get_driver() {
        return _driver;
    }

    /**
     * Gets the string of the key in torque
     * @return String
     */
    public String get_key() {
        return _key;
    }

    /**
     * Gets the name of the database
     * @return
     */
    public String get_name() {
        return _name;
    }

    /**
     * Preformated String for GUI
     */
    public String toString() {
        return _name + " (" + _driver + ")";
    }

    /**
     * Driverstring
     */
    private String _driver;

    /**
     * Databasename
     */
    private String _name;

    /**
     * Torquekey
     */
    private String _key;
}
