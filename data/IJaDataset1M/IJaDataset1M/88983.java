package picasatagstopictures.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Provide UserPreferences but do not write into the
 * Windows registry. Intended use for Windows. Some user
 * may prefer to avoid writing something into the windows
 * registry. For example they are using a computer from
 * a customer and do not want to leave unnesseccary traces they can't
 * remove because of limited rigths.
 *
 * @author tom
 */
public class ScannerProperties {

    private static ScannerProperties instance;

    public static final String KEY_FILE_NAME = "scanner.properties";

    private Properties props;

    private Logger logger;

    /**
     * Sole constructor. Sets the directory where to write the preferences.
     */
    private ScannerProperties() {
        this.props = new Properties();
        this.logger = Logger.getLogger(this.getClass().getName());
    }

    /**
     * Sole entry point.
     * @return sole instance (singleton) of this class
     */
    public static ScannerProperties getInstance() {
        if (instance == null) {
            instance = new ScannerProperties();
        }
        instance.loadUserPrefs();
        return instance;
    }

    public void put(String key, String value) {
        if (this.props != null) {
            this.props.setProperty(key, value);
            this.storePrefs();
        } else {
            this.logger.warning("Failed to read property because underlying properties are not initialised.");
        }
    }

    /**
     * 
     * @param key
     * @param value default value to return if no value is found
     */
    public String get(String key, String value) {
        if (this.props != null) {
            String v = this.props.getProperty(key);
            if (v == null) {
                return value;
            } else {
                return v;
            }
        } else {
            this.logger.warning("Failed to read property because underlying properties are not initialised.");
            return value;
        }
    }

    public void putInt(String key, int i) {
        if (this.props != null) {
            this.props.setProperty(key, Integer.toString(i));
            this.storePrefs();
        } else {
            this.logger.warning("Failed to read property because underlying properties are not initialised.");
        }
    }

    /**
     * 
     * @param key
     * @param value default value to return if no value is found
     */
    public int getInt(String key, int i) {
        if (this.props != null) {
            String v = this.props.getProperty(key);
            if (v == null) {
                return i;
            } else {
                try {
                    int parsedInt = Integer.parseInt(v);
                    return parsedInt;
                } catch (Exception e) {
                    this.logger.warning("Failed to parse '" + v + "' from the preferences.");
                    return i;
                }
            }
        } else {
            this.logger.warning("Failed to read property because underlying properties are not initialised.");
            return i;
        }
    }

    public void putBoolean(String key, boolean b) {
        if (this.props != null) {
            this.props.setProperty(key, Boolean.toString(b));
            this.storePrefs();
        } else {
            this.logger.warning("Failed to read property because underlying properties are not initialised.");
        }
    }

    /**
     * 
     * @param key
     * @param value default value to return if no value is found
     */
    public boolean getBoolean(String key, boolean b) {
        if (this.props != null) {
            String v = this.props.getProperty(key);
            if (v == null) {
                return b;
            } else {
                try {
                    boolean parsedBoolean = Boolean.parseBoolean(v);
                    return parsedBoolean;
                } catch (Exception e) {
                    this.logger.warning("Failed to parse '" + v + "' from the preferences.");
                    return b;
                }
            }
        } else {
            this.logger.warning("Failed to read property because underlying properties are not initialised.");
            return b;
        }
    }

    /**
     * File to read and write the user preferences
     * @return the absolute path of the properties file used to store the
     * scanner preferences: {user.dir}/scanner.properties
     */
    public String getPropertiesFile() {
        String dir = System.getProperty("user.dir");
        String f = dir + File.separator + KEY_FILE_NAME;
        return f;
    }

    private void loadUserPrefs() {
        String f = this.getPropertiesFile();
        File file = new File(f);
        if (!file.exists()) {
            this.logger.finer("The file '" + f + "' for user preferences does not exit yet. Nothing to load.");
            return;
        }
        if (f != null) {
            this.logger.finer("Loading user preferences from file '" + f + "'...");
            BufferedReader in = null;
            try {
                in = new BufferedReader(new FileReader(f));
                this.props.load(in);
                in.close();
                in = null;
            } catch (IOException ex) {
                this.logger.log(Level.SEVERE, null, ex);
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException ex) {
                    }
                }
            }
        }
    }

    private void storePrefs() {
        String f = this.getPropertiesFile();
        if (f != null) {
            this.logger.finer("Storing preferences to file'" + f + "'...");
            BufferedWriter out = null;
            try {
                out = new BufferedWriter(new FileWriter(f));
                this.props.store(out, null);
                out.close();
                out = null;
            } catch (IOException ex) {
                this.logger.log(Level.SEVERE, null, ex);
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException ex) {
                    }
                }
            }
        } else {
            this.logger.finer("Failed to store preferences because file is not known");
        }
    }
}
