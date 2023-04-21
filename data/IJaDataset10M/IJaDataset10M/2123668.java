package com.volantis.xml.namespace;

/**
 * Represents a mutable XML qualified name.
 *
 */
public class ImmutableQName extends QName {

    /**
     * Create an Immutable QName
     * @param prefix the qualified name prefix
     * @param localName the local name of the qualified name
     */
    public ImmutableQName(String prefix, String localName) {
        super(prefix, localName);
    }

    /**
     * Create an Immutable QName from a combine localname and prefix
     * @param longName the name in the form "prefix:localname"
     * @return a QName
     */
    public ImmutableQName(String longName) {
        super(longName);
    }

    /**
     * Create a mutable QName from an existing name
     * @param prefix the qualified name prefix
     * @param localName the local name of the qualified name
     */
    public ImmutableQName(QName name) {
        super(name.getPrefix(), name.getLocalName());
    }
}
