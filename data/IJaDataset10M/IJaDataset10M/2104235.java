package net.sf.jncu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * Application preferences.
 * 
 * @author Moshe
 */
public class Preferences {

    /** Properties file folder. */
    private static final String FOLDER = "jNCU";

    /** Properties file name. */
    private static final String NAME = "jncu.xml";

    private static Preferences instance;

    private final Properties props = new Properties();

    private File file;

    /**
	 * Constructs a new preferences.
	 */
    protected Preferences() {
        super();
    }

    /**
	 * Get the preferences instance.
	 * 
	 * @return the preferences.
	 */
    public static Preferences getInstance() {
        if (instance == null) {
            instance = new Preferences();
            instance.load();
        }
        return instance;
    }

    /**
	 * Load the properties from storage.
	 */
    protected void load() {
        InputStream in = null;
        try {
            in = new FileInputStream(getFile());
            props.loadFromXML(in);
        } catch (FileNotFoundException fnfe) {
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                }
            }
        }
    }

    /**
	 * Persist the properties to storage.
	 */
    public void save() {
        OutputStream out = null;
        try {
            out = new FileOutputStream(getFile());
            props.storeToXML(out, null);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                }
            }
        }
    }

    /**
	 * Get the property.
	 * 
	 * @param key
	 *            the property key.
	 * @return the property value - {@code null} otherwise.
	 */
    public String get(String key) {
        return props.getProperty(key);
    }

    /**
	 * Set the property.
	 * 
	 * @param key
	 *            the property key.
	 * @param value
	 *            the property value.
	 */
    public void set(String key, String value) {
        props.setProperty(key, value);
    }

    /**
	 * Get the properties file.
	 * 
	 * @return the file.
	 */
    protected File getFile() {
        if (file == null) {
            File userFolder = new File(System.getProperty("user.home"));
            File jncuFolder = new File(userFolder, FOLDER);
            jncuFolder.mkdirs();
            file = new File(jncuFolder, NAME);
        }
        return file;
    }
}
