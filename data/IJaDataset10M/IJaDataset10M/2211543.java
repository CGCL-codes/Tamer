package org.modss.facilitator.shared.resource;

import java.util.MissingResourceException;
import java.awt.Color;
import java.net.URL;
import java.io.InputStream;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Interface for resource location and management.
 */
public interface ResourceProvider {

    /**
     * Get a ResourceBundle.
     *
     * @param bundle the name of the resource bundle.
     * @return ResourceBundle (null if the requested bundle does not exist).
     */
    public ResourceBundle getResourceBundle(String bundle);

    /**
     * Check whether a specific property has a value.  Case sensitive.
     *
     * @param property the property requested.
     * @param value the value to compare the resource setting to.
     * @return true if the resource is found and matches the value.
     */
    public boolean isPropertySetTo(String property, String value);

    /**
     * Obtain a property.
     *
     * @param property the property requested.
     * @return the property value, or null if the property does not exist.
     */
    public String getProperty(String property);

    /**
     * Obtain a property.
     *
     * @param property the property requested.
     * @param aDefault a default to use if the property does not exist.
     * @return the property value, or the default if the property does not exist.
     */
    public String getProperty(String property, String aDefault);

    /**
     * Obtain a property.
     *
     * @param property the property requested.
     * @param expected, if true an exception will be thrown if the property
     * does not exist.  If false null will be returned if the property does
     * not exist.
     * @return the property value.
     */
    public String getProperty(String property, boolean expected) throws MissingResourceException;

    /**
     * Return a boolean based on a property.  A default is provided.  The fallback 
     * value is returned if the requested property does not exist or it is not 
     * a valid boolean.
     *
     * @param property the requested property.
     * @param fallback the fallback value.
     */
    public boolean getBooleanProperty(String property, boolean fallback);

    /**
     * Return an int based on a property.  A default is provided.  The fallback value is returned
     * if the requested property does not exist or it is not a valid integer.
     *
     * @param property the requested property.
     * @param fallback the fallback value.
     */
    public int getIntProperty(String property, int fallback);

    /**
     * Return a Color based on a property.  A default is provided.  The fallback value is returned
     * if the requested property does not exist or it is not a valid color.
     * Currently only a single integer representing the 3 colors is permitted.
     *
     * @param property the requested property.
     * @param fallback the fallback value.
     */
    public Color getColorProperty(String property, Color fallback);

    /**
     * Return URL for a system resource.
     * This method uses the ClassLoader associated with the startup of
     * this application to load the requested resource.  It checks first
     * for the resource itself.  If the resource is not found it then iterates
     * through all base resource values prepending them to the resource.
     * 
     * @param resource the system resource to retrieve.
     * @return a URL which references the resource.
     */
    public URL getSystemResource(String resource) throws MissingResourceException;

    /**
     * Return a stream for a system resource.
     * This method uses the ClassLoader associated with the startup of
     * this application to load the requested resource.  It checks first
     * for the resource itself.  If the resource is not found it then iterates
     * through all base resource values prepending them to the resource.
     * 
     * @param resource the system resource to retrieve.
     * @return an input stream which references the resource.
     *
     * @see #getSystemResource
     */
    public InputStream getSystemResourceAsStream(String resource) throws MissingResourceException;

    /**
     * Helper method to provide the full resource path.
     *
     * @param base the base resource path.
     * @param resource the relative resource.
     * @return the full resource path.
     */
    public String getFullResourcePath(String base, String resource);

    /**
     * This started as a hack to provide a limited set of resources to
     * JF's code.
     *
     * @param prefix the prefix of the properties we are interested in.
     * For example "dss.gui.resultview".
     * @return a list of properties with this prefix.
     */
    public Properties getPropertiesWithPrefix(String prefix);

    /**
     * Return all properties from properties lists and resource bundles.
     *
     * @return the Properties object which contains all the properties.
     */
    public Properties getAllProperties();
}
