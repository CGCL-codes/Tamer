package gov.usda.gdpc;

import java.io.Serializable;
import java.util.*;

/**
 * Element that came from a database.
 *
 * @author  terryc
 */
public interface DBElement extends Comparable, Map, Serializable {

    /**
     * Returns the value of the specified property.
     *
     * @param property property to retrieve
     *
     * @return property value.  null if not defined.
     */
    public Object getProperty(Property property);

    /**
     * Return list of properties defined for this database element.
     *
     * @return list of properties
     */
    public List properties();

    /**
     * Returns the name of this database element.
     *
     * @return the name
     */
    public String getName();

    /**
     * Returns the data source of this element.
     *
     * @return data source
     */
    public String getDataSource();

    /**
     * Returns identifier of this element.
     *
     * @return identifier
     */
    public Identifier getID();

    /**
     * Returns whether given properties exactly match
     * properties of this instance.
     *
     * @param properties properties to match.
     *
     * @return true if given properties exactly match this
     * instance's properties.
     */
    public boolean propsEqual(Map properties);

    /**
     * Returns the number of properties defined for this element.
     *
     * @return number of properties
     */
    public int numProperties();

    /**
     * Get the unique key for the element.
     *
     * @return key
     */
    public UniqueKey getKey();

    /**
     * Get type of this element.
     *
     * @return type
     */
    public String getType();

    /**
     * Returns map of properties and values.
     *
     * return map
     */
    public Map getPropertyMap();

    /**
     * Return list of possible properties that this
     * element can have.  Property tokens in list must
     * be in order according to their assigned indices.
     *
     * @return list of possible properties.
     */
    public List getPossibleProperties();

    public List getRequiredProperties();

    public int getNumRequiredProperties();
}
