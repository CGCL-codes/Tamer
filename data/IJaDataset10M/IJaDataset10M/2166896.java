package com.volantis.mcs.protocols.styles;

import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.values.MutablePropertyValues;

/**
 * An updater that does nothing to the property.
 */
public class NoopPropertyUpdater implements PropertyUpdater {

    private static final PropertyUpdater DEFAULT_INSTANCE = new NoopPropertyUpdater();

    public static PropertyUpdater getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    public void update(StyleProperty property, MutablePropertyValues propertyValues) {
    }
}
