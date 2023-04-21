package com.pentagaia.tb.start.impl.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A small class to load properties from files.
 * 
 * Taken from pds kernel class.
 * 
 * @author mepeisen
 * @version 0.1.0
 * @since 0.1.0
 */
public final class PropertyReader {

    /** The logger */
    private static final Logger LOGGER = Logger.getLogger(PropertyReader.class.getName());

    /**
     * Hidden constructor
     */
    private PropertyReader() {
    }

    /**
     * Helper method for loading properties files.
     * 
     * @param filename
     *            Filename of the java properties file
     * @return Properties
     * @throws Exception
     */
    public static Properties getProperties(final String filename) throws Exception {
        return PropertyReader.getProperties(filename, null);
    }

    /**
     * Helper method for loading properties files with backing properties.
     * 
     * @param filename
     *            Filename of the java properties file
     * @param backingProperties
     *            The underlying properties
     * @return Properties
     * @throws Exception
     * 
     */
    public static Properties getProperties(final String filename, final Properties backingProperties) throws Exception {
        FileInputStream inputStream = null;
        try {
            Properties properties;
            if (backingProperties == null) {
                properties = new Properties();
            } else {
                properties = new Properties(backingProperties);
            }
            inputStream = new FileInputStream(filename);
            properties.load(inputStream);
            return properties;
        } catch (final IOException ioe) {
            if (PropertyReader.LOGGER.isLoggable(Level.SEVERE)) {
                PropertyReader.LOGGER.log(Level.SEVERE, "Unable to load properties file {" + filename + "}: ", ioe);
            }
            throw ioe;
        } catch (final IllegalArgumentException iae) {
            if (PropertyReader.LOGGER.isLoggable(Level.SEVERE)) {
                PropertyReader.LOGGER.log(Level.SEVERE, "Illegal data in properties file {" + filename + "}: ", iae);
            }
            throw iae;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (final IOException e) {
                    if (PropertyReader.LOGGER.isLoggable(Level.CONFIG)) {
                        PropertyReader.LOGGER.log(Level.CONFIG, "failed to close property file {" + filename + "}", e);
                    }
                }
            }
        }
    }
}
