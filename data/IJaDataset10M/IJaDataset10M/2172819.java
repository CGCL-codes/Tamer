package com.volantis.mcs.themes;

import com.volantis.mcs.model.property.PropertyIdentifier;

/**
 * A common superinterface for element selectors (type and universal).
 *
 * @mock.generate base="Selector"
 */
public interface ElementSelector extends Selector, Namespaced {

    /**
     * Used to identify the namespace property of this class when logging
     * validation errors.
     */
    PropertyIdentifier NAMESPACE = new PropertyIdentifier(ElementSelector.class, "namespace");
}
