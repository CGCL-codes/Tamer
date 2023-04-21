package org.klco.openkeyvalfs;

import java.util.Properties;
import org.apache.log4j.Logger;

/**
 * A class of utility methods for the OpenKeyVal code.
 *
 * @author daniel.klco
 * @version 20101105
 */
public class Utilities {

    /** the log4j logger */
    private static Logger log = Logger.getLogger(Utilities.class);

    /** the application properties */
    protected static Properties prop;

    /**
     * Shrinks the provided byte array to the provided size.
     *
     * @param oldArray the array to shrink
     * @param newSize the size to which we should shrink the array
     * @return the new array
     */
    public static byte[] shrink(byte[] oldArray, int newSize) {
        byte[] newArray = new byte[newSize];
        System.arraycopy(oldArray, 0, newArray, 0, newSize);
        return newArray;
    }

    public static String getApplicationProperty(String key) {
        if (prop == null) {
            prop = new Properties();
            try {
                prop.load(Utilities.class.getClassLoader().getResourceAsStream("org/klco/openkeyvalfs/OpenKeyValFileSystem.properties"));
            } catch (Exception e) {
                log.warn("Unable to load properties", e);
            }
        }
        return prop.getProperty(key);
    }
}
