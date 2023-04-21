package com.peterhi.client.ui;

import java.util.List;
import com.peterhi.client.ui.PropertyEditorType;

/**
 * Implement <c>PropertyBound</c> if you wish your object
 * to expose field values that conforms to the Common
 * Property Set.
 * @author YUN TAO HAI (hytparadisee)
 *
 */
public interface PropertyBound {

    /**
	 * Gets the value of a <c>Property</c>.
	 * @param id The <c>Property</c> constant from the Standard Property Set.
	 * @return The value.
	 */
    Object get(Property id);

    /**
	 * Sets the value of a <c>Property</c>.
	 * @param id The <c>Property</c> constant from the Standard Property Set.
	 * @param value The new value to set.
	 * @throws Exception when set operation failed. One should wrap the operation
	 * with try-catch blocks and once an exception is caught, one should
	 * immediately call the user-defined cancel() method to roll back changes.
	 * Although this is not required, but it is the advised way.
	 */
    void set(Property id, Object value) throws Exception;

    /**
	 * Gets the backing <c>Editor</c> of the <c>Property</c>.
	 * This is used by the PeterHi Application System to allow
	 * end users edit the properties.
	 * @param id The <c>Property</c>.
	 * @return The <c>Editor</c> that can edit the <c>Property</c>.
	 */
    PropertyEditorType getEditorType(Property id);

    /**
	 * Retrieves the list of <c>Property</c> items.
	 * @return The <c>Property</c> <c>List</c>.
	 */
    List<Property> ids();

    /**
	 * A utility method to evaluate whether current
	 * property value will be considered 'changed' if
	 * this new value is set. This is a good way to improve
	 * performance which reduce the rate of setting
	 * property with the same value over and over again.
	 */
    boolean hasChanged(Property id, Object value);
}
