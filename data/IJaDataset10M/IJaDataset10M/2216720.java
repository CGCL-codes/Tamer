package com.volantis.mcs.protocols.widgets.attributes;

import java.util.LinkedList;
import java.util.List;

/**
 * Holds properties specific to MapLocationMarkersElement
 */
public final class MapLocationMarkersAttributes extends WidgetAttributes {

    /**
     * List of attributes of elements that are within this element. 
     */
    private final List contentAttributes = new LinkedList();

    /**
     * @return Return list of attributes of elements that are
     * within this element.
     */
    public List getContentAttributes() {
        return contentAttributes;
    }
}
