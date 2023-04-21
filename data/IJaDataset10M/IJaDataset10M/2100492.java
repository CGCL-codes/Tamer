package com.volantis.styling.engine;

/**
 * Minimal interface required by styling for accessing attributes associated
 * with an element being styled.
 *
 * @mock.generate 
 */
public interface Attributes {

    /**
     * Get the value of the attribute.
     *
     * @param namespace The namespace of the attribute, null if the attribute
     * does not belong in a namespace, i.e. it belongs to the element.
     * @param localName The local name of the attribute, may not be null.
     *
     * @return The value of the attribute, or null if the attribute does not
     * exist.
     */
    String getAttributeValue(String namespace, String localName);
}
