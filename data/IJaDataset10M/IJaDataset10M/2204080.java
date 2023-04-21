package org.apache.axis.message.addressing;

import org.w3c.dom.Element;

/**
 * Represents classes that know how to serialize themselves
 * into a DOM tree.
 *
 * @author Rodrigo Ruiz
 * @version $Revision: 14 $
 */
public interface DOMAppendable {

    /**
   * Appends a DOM representation of this instance to the specified element.
   *
   * @param version     WS-Addressing version to use
   * @param parent      Parent element
   * @param elementName Name of the element to create
   */
    void append(AddressingVersion version, Element parent, String elementName);

    /**
   * Appends a DOM representation of this instance to
   * the specified element.
   *
   * @param parent Parent element
   * @param elementName The name of the element to create
   */
    void append(Element parent, String elementName);

    /**
   * Appends a DOM representation of this instance to
   * the specified element.
   * <p>
   * This method should call {@link #append(Element, String)}
   * with a default value for the elementName parameter. Abstract
   * classes, or those to be always subclassed may optionally
   * throw {@link java.lang.UnsupportedOperationException}
   * instead.
   *
   * @param parent Parent element
   */
    void append(Element parent);
}
