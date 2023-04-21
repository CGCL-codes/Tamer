package com.cyberoblivion.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ben Erridge
 */
public class PropertyUtils {

    /**
     * Function loads a properties file first tries current directory then tries 
     * inside the jar file
     * @param filename The name of the file example "defaults.properties"
     * @return the loaded properties
     */
    public static Properties loadProperties(String filename) {
        Properties properties = new Properties();
        return loadProperties(properties, filename);
    }

    /**
     * 
     * @param properties the default properties to load into
     * @param filename the filename of the properties to load
     * @return loaded properties 
     */
    public static Properties loadProperties(Properties properties, String filename) {
        InputStream is = null;
        try {
            properties.load(new FileInputStream(filename));
        } catch (IOException e) {
            try {
                is = ClassLoader.getSystemClassLoader().getResourceAsStream(filename);
                properties.load(is);
            } catch (IOException ex) {
                System.out.println("couldn't read resource bundle");
                return properties;
            }
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                    Logger.getLogger(PropertyUtils.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return properties;
    }
}
