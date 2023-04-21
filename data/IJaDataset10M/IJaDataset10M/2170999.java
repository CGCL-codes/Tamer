package org.xmlhammer.gui.xpath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;

/**
 * Implementation of the a Namespace Context as a HashMap.
 * 
 * @version $Revision: 1.3 $, $Date: 2006/05/13 10:25:24 $
 * @author Edwin Dankert <edankert@gmail.com>
 */
public class NamespaceContextMap extends HashMap<String, String> implements NamespaceContext {

    private static final long serialVersionUID = 3257568403886650425L;

    public NamespaceContextMap() {
        put(XMLConstants.XML_NS_PREFIX, XMLConstants.XML_NS_URI);
        put(XMLConstants.XMLNS_ATTRIBUTE, XMLConstants.XMLNS_ATTRIBUTE_NS_URI);
    }

    /**
	 * Get Namespace URI bound to a prefix in the current scope.
	 * 
	 * @param prefix the namespace prefix.
	 * @return the URI found for the prefix. 
	 * 
	 * @see javax.xml.namespace.NamespaceContext#getNamespaceURI(java.lang.String)
	 */
    public String getNamespaceURI(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException("Prefix cannot be null!");
        }
        String uri = get(prefix);
        if (uri != null) {
            return uri;
        }
        return XMLConstants.NULL_NS_URI;
    }

    /**
	 * Return the prefix bound to the namespace uri, null if no prefix could be found.
	 * 
	 * @param namespaceURI the namespace URI.
	 * @return the prefix found for the URI.
	 * 
	 * @see javax.xml.namespace.NamespaceContext#getPrefix(java.lang.String)
	 */
    public String getPrefix(String namespaceURI) {
        if (namespaceURI == null) {
            throw new IllegalArgumentException("Namespace URI cannot be null.");
        }
        for (String prefix : keySet()) {
            if (get(prefix).equals(namespaceURI)) {
                return prefix;
            }
        }
        return null;
    }

    /**
	 * Return the list of prefixes bound to the namespace uri, null if no prefix 
	 * could be found.
	 * 
	 * @param namespaceURI the namespace URI.
	 * @return the prefixs bound to the URI.
	 * 
	 * @see javax.xml.namespace.NamespaceContext#getPrefix(java.lang.String)
	 */
    public Iterator getPrefixes(String namespaceURI) {
        if (namespaceURI == null) {
            throw new IllegalArgumentException("Namespace URI cannot be null.");
        }
        List<String> prefixes = new ArrayList<String>();
        for (String prefix : keySet()) {
            if (get(prefix).equals(namespaceURI)) {
                prefixes.add(prefix);
            }
        }
        return prefixes.iterator();
    }
}
