package org.vexi.widgetdoc.util;

import java.util.Iterator;
import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;

public class NamespaceContextProvider implements NamespaceContext {

    String boundPrefix, boundURI;

    public NamespaceContextProvider(String prefix, String URI) {
        boundPrefix = prefix;
        boundURI = URI;
    }

    public String getNamespaceURI(String prefix) {
        if (prefix.equals(boundPrefix)) {
            return boundURI;
        } else if (prefix.equals(XMLConstants.XML_NS_PREFIX)) {
            return XMLConstants.XML_NS_URI;
        } else if (prefix.equals(XMLConstants.XMLNS_ATTRIBUTE)) {
            return XMLConstants.XMLNS_ATTRIBUTE_NS_URI;
        } else {
            return XMLConstants.NULL_NS_URI;
        }
    }

    public String getPrefix(String namespaceURI) {
        if (namespaceURI.equals(boundURI)) {
            return boundPrefix;
        } else if (namespaceURI.equals(XMLConstants.XML_NS_URI)) {
            return XMLConstants.XML_NS_PREFIX;
        } else if (namespaceURI.equals(XMLConstants.XMLNS_ATTRIBUTE_NS_URI)) {
            return XMLConstants.XMLNS_ATTRIBUTE;
        } else {
            return null;
        }
    }

    public Iterator getPrefixes(String namespaceURI) {
        return null;
    }
}
