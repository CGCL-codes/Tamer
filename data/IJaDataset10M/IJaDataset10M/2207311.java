package javazoom.jlgui.player.amp.util.ini;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import javazoom.jlgui.player.amp.util.Config;

/**
 * A Configuration is used to save a set of configuration
 * properties.  The properties can be written out to disk
 * in "name=value" form, and read back in.
 *
 * @author Jeremy Cloud
 * @version 1.2.0
 */
public class Configuration {

    private File config_file = null;

    private URL config_url = null;

    private Hashtable props = new Hashtable(64);

    /**
     * Constructs a new Configuration object that stores
     * it's properties in the file with the given name.
     */
    public Configuration(String file_name) {
        if (Config.startWithProtocol(file_name)) {
            try {
                this.config_url = new URL(file_name);
            } catch (Exception e) {
                e.printStackTrace();
            }
            load();
        } else {
            this.config_file = new File(file_name);
            load();
        }
    }

    /**
     * Constructs a new Configuration object that stores
     * it's properties in the given file.
     */
    public Configuration(File config_file) {
        this.config_file = config_file;
        load();
    }

    /**
     * Constructs a new Configuration object that stores
     * it's properties in the given file.
     */
    public Configuration(URL config_file) {
        this.config_url = config_file;
        load();
    }

    /**
     * Constructs a new Configuration object that doesn't
     * have a file associated with it.
     */
    public Configuration() {
        this.config_file = null;
    }

    /**
     * @return The config file.
     */
    public File getConfigFile() {
        return config_file;
    }

    /**
     * Adds a the property with the given name and value.
     *
     * @param name  The name of the property.
     * @param value The value of the property.
     */
    public void add(String name, String value) {
        props.put(name, value);
    }

    /**
     * Adds the boolean property.
     *
     * @param name  The name of the property.
     * @param value The value of the property.
     */
    public void add(String name, boolean value) {
        props.put(name, value ? "true" : "false");
    }

    /**
     * Adds the integer property.
     *
     * @param name  The name of the property.
     * @param value The value of the property.
     */
    public void add(String name, int value) {
        props.put(name, Integer.toString(value));
    }

    /**
     * Adds the double property.
     *
     * @param name  The name of the property.
     * @param value The value of the property.
     */
    public void add(String name, double value) {
        props.put(name, Double.toString(value));
    }

    /**
     * Returns the value of the property with the
     * given name.  Null is returned if the named
     * property is not found.
     *
     * @param The name of the desired property.
     * @return The value of the property.
     */
    public String get(String name) {
        return (String) props.get(name);
    }

    /**
     * Returns the value of the property with the
     * given name.  'default_value' is returned if the
     * named property is not found.
     *
     * @param The           name of the desired property.
     * @param default_value The default value of the property which is returned
     *                      if the property does not have a specified value.
     * @return The value of the property.
     */
    public String get(String name, String default_value) {
        Object value = props.get(name);
        return value != null ? (String) value : default_value;
    }

    /**
     * Returns the value of the property with the given name.
     * 'false' is returned if the property does not have a
     * specified value.
     *
     * @param name   The name of the desired property.
     * @param return The value of the property.
     */
    public boolean getBoolean(String name) {
        Object value = props.get(name);
        return value != null ? value.equals("true") : false;
    }

    /**
     * Returns the value of the property with the given name.
     *
     * @param name          The name of the desired property.
     * @param default_value The default value of the property which is returned
     *                      if the property does not have a specified value.
     * @param return        The value of the property.
     */
    public boolean getBoolean(String name, boolean default_value) {
        Object value = props.get(name);
        return value != null ? value.equals("true") : default_value;
    }

    /**
     * Returns the value of the property with the given name.
     * '0' is returned if the property does not have a
     * specified value.
     *
     * @param name   The name of the desired property.
     * @param return The value of the property.
     */
    public int getInt(String name) {
        try {
            return Integer.parseInt((String) props.get(name));
        } catch (Exception e) {
        }
        return -1;
    }

    /**
     * Returns the value of the property with the given name.
     *
     * @param name          The name of the desired property.
     * @param default_value The default value of the property which is returned
     *                      if the property does not have a specified value.
     * @param return        The value of the property.
     */
    public int getInt(String name, int default_value) {
        try {
            return Integer.parseInt((String) props.get(name));
        } catch (Exception e) {
        }
        return default_value;
    }

    /**
     * Returns the value of the property with the given name.
     * '0' is returned if the property does not have a
     * specified value.
     *
     * @param name   The name of the desired property.
     * @param return The value of the property.
     */
    public double getDouble(String name) {
        try {
            return new Double((String) props.get(name)).doubleValue();
        } catch (Exception e) {
        }
        return -1d;
    }

    /**
     * Returns the value of the property with the given name.
     *
     * @param name          The name of the desired property.
     * @param default_value The default value of the property which is returned
     *                      if the property does not have a specified value.
     * @param return        The value of the property.
     */
    public double getDouble(String name, double default_value) {
        try {
            return new Double((String) props.get(name)).doubleValue();
        } catch (Exception e) {
        }
        return default_value;
    }

    /**
     * Removes the property with the given name.
     *
     * @param name The name of the property to remove.
     */
    public void remove(String name) {
        props.remove(name);
    }

    /**
     * Removes all the properties.
     */
    public void removeAll() {
        props.clear();
    }

    /**
     * Loads the property list from the configuration file.
     *
     * @return True if the file was loaded successfully, false if
     *         the file does not exists or an error occurred reading
     *         the file.
     */
    public boolean load() {
        if ((config_file == null) && (config_url == null)) return false;
        if (config_url != null) {
            try {
                return load(new BufferedReader(new InputStreamReader(config_url.openStream())));
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            if (!config_file.exists()) return false;
            try {
                return load(new BufferedReader(new FileReader(config_file)));
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    public boolean load(BufferedReader buffy) throws IOException {
        Hashtable props = this.props;
        String line = null;
        while ((line = buffy.readLine()) != null) {
            int eq_idx = line.indexOf('=');
            if (eq_idx > 0) {
                String name = line.substring(0, eq_idx).trim();
                String value = line.substring(eq_idx + 1).trim();
                props.put(name, value);
            }
        }
        buffy.close();
        return true;
    }

    /**
     * Saves the property list to the config file.
     *
     * @return True if the save was successful, false othewise.
     */
    public boolean save() {
        if (config_url != null) return false;
        try {
            PrintWriter out = new PrintWriter(new FileWriter(config_file));
            return save(out);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean save(PrintWriter out) throws IOException {
        Hashtable props = this.props;
        Enumeration names = props.keys();
        SortedStrings sorter = new SortedStrings();
        while (names.hasMoreElements()) {
            sorter.add((String) names.nextElement());
        }
        for (int i = 0; i < sorter.stringCount(); i++) {
            String name = sorter.stringAt(i);
            String value = (String) props.get(name);
            out.print(name);
            out.print("=");
            out.println(value);
        }
        out.close();
        return true;
    }

    public void storeCRC() {
        add("crc", generateCRC());
    }

    public boolean isValidCRC() {
        String crc = generateCRC();
        String stored_crc = (String) props.get("crc");
        if (stored_crc == null) return false;
        return stored_crc.equals(crc);
    }

    private String generateCRC() {
        Hashtable props = this.props;
        CRC32OutputStream crc = new CRC32OutputStream();
        PrintWriter pr = new PrintWriter(crc);
        Enumeration names = props.keys();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            if (!name.equals("crc")) {
                pr.println((String) props.get(name));
            }
        }
        pr.flush();
        return "" + crc.getValue();
    }
}
