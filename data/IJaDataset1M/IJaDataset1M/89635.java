package equilSharedFramework;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.ListIterator;
import java.util.LinkedList;

/**
 * A basic way of manipulating configuration data in a plain text file.
 * @author CITS3200 2006 Group E
 * @version 1.0.0
 */
public class ConfigFile {

    private static final String DELIM = " -> ";

    private static final String COMMENT = "#";

    private File configFile;

    private LinkedList<String> configuration;

    /**
     * Creates a new configuration object with the data from a specified file.
     * @param filePath the path of the file containing the configuration data
     */
    public ConfigFile(String filePath) {
        this.configuration = new LinkedList<String>();
        this.configFile = new File(filePath);
        try {
            if (!configFile.exists()) {
                configFile.createNewFile();
            }
        } catch (Exception e) {
            System.err.println("Error opening configuration file.");
        }
        load();
    }

    /**
     * Load a configuration from the file.
     */
    public void load() {
        try {
            configuration = new LinkedList<String>();
            FileReader fr = new FileReader(configFile);
            BufferedReader br = new BufferedReader(fr);
            String readLine;
            while ((readLine = br.readLine()) != null) {
                if (!readLine.startsWith(COMMENT)) {
                    configuration.add(readLine);
                }
            }
            br.close();
            fr.close();
        } catch (Exception e) {
            System.err.println("Error loading configuration file.");
        }
    }

    /**
     * Save the current configuration to the file.
     */
    public void save() {
        try {
            FileWriter fw = new FileWriter(configFile);
            PrintWriter pw = new PrintWriter(fw);
            ListIterator lit = configuration.listIterator();
            while (lit.hasNext()) {
                pw.println(lit.next());
            }
            pw.flush();
            pw.close();
            fw.close();
        } catch (Exception e) {
            System.err.println("Error saving configuration file.");
        }
    }

    private boolean isKey(String str, String key) {
        return strKey(str).equalsIgnoreCase(key);
    }

    /**
     * Takes a plain text string in the format KEY+DELIM+VALUE and returns the 
     * key.
     * @param str a string in the correct format.
     * @return   the key, or null if the string is in an incorrect format
     */
    private String strKey(String str) {
        try {
            return str.split(DELIM)[0];
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Takes a plain text string in the format KEY+DELIM+VALUE and returns the
     * value.
     * @param str a string in the correct format.
     * @return   the value, or null if the string is in an incorrect format
     */
    private String strValue(String str) {
        try {
            return str.split(DELIM)[1];
        } catch (Exception e) {
            return null;
        }
    }

    /** 
     * Sets the value of a key in the loaded configuration list. If the key 
     * does not already exist, then it is created.
     * @param key   the key to modify
     * @param value the new value to associate with the key
     */
    public void setKey(String key, String value) {
        String str;
        ListIterator<String> lit = configuration.listIterator();
        boolean keyAdded = false;
        while (lit.hasNext() && !keyAdded) {
            str = lit.next();
            if (isKey(str, key)) {
                lit.set(key + DELIM + value);
                keyAdded = true;
            }
        }
        if (!keyAdded) configuration.add(key + DELIM + value);
    }

    /**
     * Searches the loaded configuration list for an occurance of a specified
     * key, and then returns the value associated with that key (or null, if
     * no occurance of the key is found).
     * @param key  the key to search for
     * @return     the value associated with the key (or null if no key found)
     */
    public String getValue(String key) {
        String str;
        String returnValue = null;
        ListIterator lit = configuration.listIterator();
        while (lit.hasNext()) {
            str = (String) lit.next();
            if (isKey(str, key)) returnValue = strValue(str);
        }
        return returnValue;
    }

    /**
     * Iterates through the loaded configuration list and removes any occurances
     * of the specified key.
     * @param key the key to remove
     */
    public void removeKey(String key) {
        String str;
        ListIterator lit = configuration.listIterator();
        while (lit.hasNext()) {
            str = (String) lit.next();
            if (isKey(str, key)) lit.remove();
        }
    }
}
