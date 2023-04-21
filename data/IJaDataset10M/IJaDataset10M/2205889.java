package net.infonode.properties.util;

import net.infonode.properties.base.*;

/**
 * Sets and gets property values to and from value objects.
 *
 * @author $Author: jesper $
 * @version $Revision: 1.4 $
 */
public interface PropertyValueHandler {

    /**
   * Gets the value of a property from a value container.
   *
   * @param property       the property
   * @param valueContainer the object containing the value
   * @return the property value, null if the container doesn't contain the value
   */
    Object getValue(Property property, Object valueContainer);

    /**
   * Sets the value of a property in a value container.
   *
   * @param property       the property
   * @param valueContainer the object that will contain the value
   * @param value          the property value
   */
    void setValue(Property property, Object valueContainer, Object value);
}
