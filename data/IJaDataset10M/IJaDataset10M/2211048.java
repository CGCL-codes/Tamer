package net.toften.prop4j;

import java.beans.PropertyChangeListener;
import java.io.PrintStream;
import java.util.Map;
import java.util.Properties;
import net.toften.prop4j.loaders.PropertyLoader;

/**
 * All <code>Property Interface</code>s must extend this interface. It provides a basic set of methods.
 * <p>
 * An example of a Property Interface:
 * <pre>
 *  public interface LoginDetails extends PropertyBase {
 *      @PropertyGetter ( name = "username" )
 *      public String getUsername();
 *       
 *      @PropertyGetter ( name = "password" )
 *      public String getPassword();
 *  }
 * </pre>
 * 
 * In the above example, two properties are defined: username and password.
 * 
 * To get the property values, we write the following code:
 * <pre>
 *  {
 *      ...
 *      LoginDetails login = Prop.getProp(LoginDetails.class);
 *      
 *      String username = login.getUsername();
 *      ...
 *  }
 * </pre>
 * 
 * The values returned when invoking the getters are provided by a {@link PropertyLoader}. Prop4j comes
 * with a number of <code>PropertyLoader</code> implementations.
 * 
 * @author thomaslarsen
 * 
 * @see PropertyGetter
 * @see PropertySetter
 */
public interface PropertyBase {

    /**
     * Get the value of a property given it's name.
     * 
     * @param propertyName
     *            the name of the property
     * @return the value of the property if it has a value; <code>null</code> if it doesn't
     * @throws IllegalArgumentException
     *             if the property is not defined for this property interface
     */
    Object getProperty(String propertyName) throws IllegalArgumentException, NullPointerException;

    /**
     * Lists the names and values of all the properties defined by the <code>Property Interface</code>.
     * 
     * @param out
     *            the stream to send the list to
     */
    void listProperties(PrintStream out);

    /**
     * Sets the value of a property given it's name.
     * 
     * @param propertyName
     *            the name of the property
     * @param value
     *            the value to assign to the property
     * @throws IllegalStateException
     *             if the {@link PropertyLoader} is not {@link PropertyLoader#isMutable() mutable}
     * @throws IllegalArgumentException
     *             if the property is not defined for this property interface
     */
    void setProperty(String propertyName, Object value) throws IllegalStateException, IllegalArgumentException;

    /**
     * Returns a {@link Map} containing all the properties defined by the property interface.
     * <p>
     * The map is a deep copy of the properties.
     * 
     * @return Map of names/values of the properties
     */
    Map<String, Object> getProperties();

    /**
     * Sets the property values defined in the <code>Property Interface</code>.
     * <p>
     * Any property key not defined will be ignored, and any value of properties not contained will
     * be retained.
     * 
     * @param newProperties
     */
    void setProperties(Properties newProperties);

    /**
     * Sets the property values defined in the <code>Property Interface</code>.
     * <p>
     * Any property key not defined will be ignored, and any value of properties not contained will
     * be retained.
     * 
     * @param newProperties
     */
    void setProperties(Map<String, Object> newProperties);

    /**
     * @return the PropertyLoader for the <code>Property Interface</code>
     */
    PropertyLoader getLoader();

    void addPropertyChangeListener(PropertyChangeListener listener);

    void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) throws IllegalArgumentException;

    void removePropertyChangeListener(PropertyChangeListener listener);

    void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) throws IllegalArgumentException;
}
