package net.jxta.document;

import java.util.Enumeration;

/**
 * Extends {@link Element} to provide {@link java.lang.String} oriented
 * accessors for instances of {@link StructuredTextDocument}
 *
 * @see net.jxta.document.Document
 * @see net.jxta.document.Element
 * @see net.jxta.document.StructuredDocument
 * @see net.jxta.document.StructuredTextDocument
 */
public interface TextElement<T extends TextElement<T>> extends Element<T> {

    /**
     * {@inheritDoc}
     */
    String getKey();

    /**
     * {@inheritDoc}
     */
    String getValue();

    /**
     * {@inheritDoc}
     */
    StructuredTextDocument getRoot();

    /**
     * Get the name associated with an element.
     *
     * @return A string containing the name of this element.
     */
    String getName();

    /**
     * Get the value (if any) associated with an element.
     *
     * @return A string containing the value of this element, if any, otherwise null.
     */
    String getTextValue();

    /**
     * Returns an enumeration of the immediate children of this element whose
     * name match the specified string.
     *
     * @param name The name which will be matched against.
     * @return An enumeration containing all of the children of this element.
     */
    Enumeration<T> getChildren(String name);
}
