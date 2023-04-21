package projectawesome;

import java.io.*;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/** <b>Project AWESOME</b> <br/>
 * Handles the configuration file and all interactions with it. At initialization
 * the file is read and all the data is put in a list consisting of {@link ConfigInfo}
 * objects.
 * @author Karl Linderhed and Anton Sundblad
 */
public class Config {

    private static File fConfig;

    private static String strFileName = "config.cfg";

    private static int nDebugId = Debug.getId(Config.class.getName(), Thread.currentThread().toString());

    private static boolean bStarted = false;

    public static LinkedList<KeyValuesData> lConfig;

    /** 
     * Initializes the configuration file and list. Reads the configuration file
     * and inputs the data into the list as {@link ConfigInfo} objects. If there is no file the list is populated
     * with default values and a new file is created.
     */
    public static void init() {
        lConfig = new LinkedList<KeyValuesData>();
        fConfig = new File(strFileName);
        try {
            BufferedReader in = new BufferedReader(new FileReader(fConfig));
            String strRead;
            while ((strRead = in.readLine()) != null) {
                String strConfigArr[] = new String[2];
                strConfigArr = strRead.split("=");
                lConfig.add(new KeyValuesData(strConfigArr[0], strConfigArr[1]));
            }
            Debug.log("Successfully retrieved config info", nDebugId, Debug.FLAG_NOTICE);
        } catch (FileNotFoundException ex) {
            Debug.log("Config file not found at " + fConfig.getAbsolutePath() + ", creating new one", nDebugId, Debug.FLAG_NOTICE);
            createDefaultConfig();
            writeConfig();
        } catch (IOException ex) {
            Debug.log("IOException while reading from " + strFileName, nDebugId, Debug.FLAG_FATAL);
        }
        bStarted = true;
    }

    /**
     * Edits a value in the configuration.
     * @param strKey The key which should have it's value edited
     * @param strNewValue The new value to put at strKey
     */
    public static void editValue(String strKey, String strNewValue) {
        for (int i = 0; i < lConfig.size(); i++) {
            if (lConfig.get(i).strKey.equals(strKey)) {
                lConfig.get(i).strValue = strNewValue;
                break;
            }
        }
        writeConfig();
    }

    /**
     * Writes the configuration list to the config file.
     *
     */
    private static void writeConfig() {
        if (!fConfig.exists()) {
            try {
                fConfig.createNewFile();
            } catch (IOException ex) {
                Debug.log("Couldn't create new config file at " + fConfig.getAbsolutePath(), nDebugId, Debug.FLAG_FATAL);
            }
        }
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(fConfig));
            for (int i = 0; i < lConfig.size(); i++) {
                out.write(lConfig.get(i).strKey + "=" + lConfig.get(i).strValue + "\r\n");
            }
            out.flush();
            Debug.log("Successfully wrote config info to file", nDebugId, Debug.FLAG_NOTICE);
        } catch (IOException ex) {
            Debug.log("IOException while writing to " + strFileName, nDebugId, Debug.FLAG_ERROR);
        }
    }

    /**
     * Fetches a string value from the config.
     * @param strKey the name of the key to fetch a value from.
     * @return a string containing the value of the corresponding strKey.
     */
    public static String strGetValue(String strKey) {
        if (!bStarted) return "";
        for (int i = 0; i < lConfig.size(); i++) {
            if (lConfig.get(i).strKey.equals(strKey)) {
                if (lConfig.get(i).strValue.equals(null)) {
                    Debug.log("No value for field '" + strKey + "' in Config file", nDebugId, Debug.FLAG_ERROR);
                    return null;
                }
                Debug.log("Successfully retrieved configuration from list", nDebugId, Debug.FLAG_NOTICE);
                return lConfig.get(i).strValue;
            }
        }
        Debug.log("No field with the name '" + strKey + "' in Config file", nDebugId, Debug.FLAG_ERROR);
        return null;
    }

    /**
     * Fetches an integer value from the config.
     * @param strKey the name of the key to fetch a value from.
     * @return the value corresponding to strKey.
     */
    public static int nGetValue(String strKey) {
        if (!bStarted) return -1;
        int nValue = -1;
        for (int i = 0; i < lConfig.size(); i++) {
            if (lConfig.get(i).strKey.equals(strKey)) {
                try {
                    nValue = Integer.parseInt(lConfig.get(i).strValue);
                } catch (NumberFormatException e) {
                    Debug.log("in: nGetValue(), ConfigInfo[" + i + "].strValue not an integer, couldn't convert to string" + lConfig.get(i).strValue + " to int.", nDebugId, Debug.FLAG_ERROR);
                    nValue = -1;
                }
                Debug.log("Successfully retrieved configuration from list", nDebugId, Debug.FLAG_NOTICE);
                return nValue;
            }
        }
        return -1;
    }

    /**
     * Finds all config values that have a set prefix or belongs to a certain "set",
     * ie: database_host and database_pass both belong to the set/prefix database.
     * Returns the number of config file values that have the prefix and don't have a
     * value of null.
     * @param strPrefix Name of the set/prefix
     * @return Number of config values with the set/prefix
     */
    public static int nHasPrefix(String strPrefix) {
        if (!bStarted) return -1;
        int nCountKeys = 0;
        for (int i = 0; i < lConfig.size(); i++) {
            String strKey = lConfig.get(i).strKey;
            String strValue = lConfig.get(i).strValue;
            if (strPrefix.length() >= strKey.length()) continue; else if (strKey.substring(0, strPrefix.length()).equals(strPrefix) && strValue != null) {
                Debug.log("Found a key with prefix: " + strPrefix + ", it's value: " + strValue, nDebugId, Debug.FLAG_DEBUG);
                nCountKeys++;
            }
        }
        Debug.log("Found " + nCountKeys + " keys with prefix: " + strPrefix, nDebugId, Debug.FLAG_DEBUG);
        return nCountKeys;
    }

    /**
     * Populates the config list with default values.
     */
    private static void createDefaultConfig() {
        lConfig.add(new KeyValuesData("server_ip", "localhost"));
        lConfig.add(new KeyValuesData("server_port", "13338"));
        lConfig.add(new KeyValuesData("database_user", "root"));
        lConfig.add(new KeyValuesData("database_pass", "haschkaka"));
        lConfig.add(new KeyValuesData("database_host", "localhost"));
        lConfig.add(new KeyValuesData("database_port", "3306"));
        lConfig.add(new KeyValuesData("database_db", "pawesome"));
        lConfig.add(new KeyValuesData("crawler_num", "3"));
        lConfig.add(new KeyValuesData("crawler_port", "13338"));
        lConfig.add(new KeyValuesData("siterecv_ip", "localhost"));
        lConfig.add(new KeyValuesData("siterecv_port", "13339"));
    }
}
