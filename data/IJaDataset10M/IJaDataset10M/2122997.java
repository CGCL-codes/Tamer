package net.mantucon.vqwiki.utilities;

import java.util.Properties;

/**
 *
 * @author mnt 
 */
public class PropertyHelper {

    static Properties properties;

    public static String getProperty(String key) {
        if (properties == null) {
            try {
                properties = new Properties();
                properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("dot.properties"));
            } catch (java.io.IOException ioe) {
                throw new RuntimeException(ioe);
            }
        }
        return properties.getProperty(key);
    }
}
