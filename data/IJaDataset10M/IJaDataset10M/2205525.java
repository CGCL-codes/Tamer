package net.sf.jade4spring.configuration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Utility for easy loading of properties for your application.
 * 
 * Simply call ApplicationProperties.get("myproperty") and the class will handle
 * loading, if not already performed, and return the value represented by the
 * property name you specified.
 * 
 * Note that the default location of the properties file is
 * classpath:/application.properties. You can specify a different location using
 * the {@link ApplicationProperties#setLocation(String)} method.
 * 
 * @author Jaran Nilsen
 * @since 1.0
 */
public class ApplicationProperties {

    private static String location = "classpath:/application.properties";

    private static Properties properties = null;

    /**
     * @return the location
     */
    public static String getLocation() {
        return location;
    }

    /**
     * Set the location of the properties file. Prefix with classpath: to
     * specify a location on your application's classpath.
     * 
     * @param loc
     *            the location to set
     */
    public static void setLocation(final String loc) {
        ApplicationProperties.location = loc;
    }

    /**
     * Get the property represented by the provided <i>propertyName</i>.
     * 
     * @param propertyName
     *            The name of the property you want to retrieve.
     * @return The property with the given propertyName.
     */
    public static String get(final String propertyName) {
        load();
        return properties.getProperty(propertyName);
    }

    /**
     * Check if properties has been loaded, if not - load them.
     */
    private static synchronized void load() {
        if (properties != null) {
            return;
        }
        InputStream stream = null;
        try {
            stream = getStream();
            properties = new Properties();
            properties.load(stream);
        } catch (Exception e) {
            throw new ApplicationPropertiesException("Unable to load properites.", e);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                }
            }
        }
    }

    /**
     * Get stream for the properties file specified by <i>location</i>.
     * 
     * @return Stream representing the resource at <i>location</i> or null if
     *         the resource cannot be found.
     * @throws FileNotFoundException
     *             Thrown if the file cannot be found.
     */
    private static InputStream getStream() throws FileNotFoundException {
        InputStream stream = null;
        if (location.startsWith("classpath:")) {
            stream = ApplicationProperties.class.getResourceAsStream(location.substring(10));
        } else {
            stream = new FileInputStream(location);
        }
        return stream;
    }
}
