package net.infonode.properties.types;

import javax.swing.*;
import net.infonode.properties.base.*;
import net.infonode.properties.util.*;

/**
 * A property of type {@link Icon}.
 *
 * @author $Author: jesper $
 * @version $Revision: 1.7 $
 */
public class IconProperty extends ValueHandlerProperty {

    /**
   * Constructor.
   *
   * @param group        the property group
   * @param name         the property name
   * @param description  the property description
   * @param valueHandler handles values for this property
   */
    public IconProperty(PropertyGroup group, String name, String description, PropertyValueHandler valueHandler) {
        super(group, name, Icon.class, description, valueHandler);
    }

    /**
   * Sets the icon value of this property in a value container.
   *
   * @param valueContainer the value container
   * @param icon           the icon value
   */
    public void set(Object valueContainer, Icon icon) {
        setValue(valueContainer, icon);
    }

    /**
   * Returns the icon value of this property in a value container.
   *
   * @param valueContainer the value container
   * @return the icon value of this property
   */
    public Icon get(Object valueContainer) {
        return (Icon) getValue(valueContainer);
    }
}
