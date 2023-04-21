package org.apache.xmlbeans.xml.stream;

/**
 * This event signals that a prefix mapping has gone out of scope
 *
 * @since Weblogic XML Input Stream 1.0
 * @version 1.0
 * @see org.apache.xmlbeans.xml.stream.StartPrefixMapping
 * @see org.apache.xmlbeans.xml.stream.ChangePrefixMapping
 */
public interface EndPrefixMapping extends XMLEvent {

    public String getPrefix();
}
