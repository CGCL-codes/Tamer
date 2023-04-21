package net.infonode.properties.base.exception;

import net.infonode.properties.base.*;

/**
 * An invalid property value was given.
 *
 * @author $Author: jesper $
 * @version $Revision: 1.3 $
 */
public class InvalidPropertyValueException extends PropertyException {

    /**
     * Constructor.
     *
     * @param property the property that was assigned a value
     * @param value    the value
     */
    public InvalidPropertyValueException(Property property, Object value) {
        super(property, "Property '" + property + "' can't be assigned the value '" + value + "'!");
    }
}
