package com.rapidminer.tools.xml;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;

/**
 * This is a {@link NamespaceContext} to use with the Java XML API that is
 * based upon a map. That means, that each uri can only have one prefix.
 * It is able to define a default namespace.
 * 
 * @author Sebastian Land
 */
public final class MapBasedNamespaceContext implements NamespaceContext {

    private final Map<String, String> idNamespacesMap;

    private final Map<String, String> namespaceIdsMap;

    private String defaultNamespaceURI = null;

    public MapBasedNamespaceContext(Map<String, String> idNamespacesMap) {
        this(idNamespacesMap, null);
    }

    /**
     * This creates a {@link NamespaceContext} with the given map. The map maps from
     * ids to the namespaces' URIs. A default namespace can be used.
     * @param idNamespaceURIsMap
     * @param defaultNamespaceURI
     */
    public MapBasedNamespaceContext(Map<String, String> idNamespaceURIsMap, String defaultNamespaceURI) {
        this.defaultNamespaceURI = defaultNamespaceURI;
        this.idNamespacesMap = idNamespaceURIsMap;
        namespaceIdsMap = new HashMap<String, String>();
        for (Entry<String, String> entry : idNamespaceURIsMap.entrySet()) {
            namespaceIdsMap.put(entry.getValue(), entry.getKey());
        }
    }

    @Override
    public String getNamespaceURI(String prefix) {
        if (XMLConstants.DEFAULT_NS_PREFIX.equals(prefix)) {
            if (defaultNamespaceURI == null) return XMLConstants.NULL_NS_URI; else return defaultNamespaceURI;
        } else if (XMLConstants.XML_NS_PREFIX.equals(prefix)) {
            return XMLConstants.XML_NS_URI;
        } else if (XMLConstants.XMLNS_ATTRIBUTE.equals(prefix)) {
            return XMLConstants.XMLNS_ATTRIBUTE_NS_URI;
        } else if (prefix == null) {
            throw new IllegalArgumentException("Null prefix not allowed");
        } else {
            String uri = idNamespacesMap.get(prefix);
            if (uri == null) return XMLConstants.NULL_NS_URI; else return uri;
        }
    }

    @Override
    public String getPrefix(String uri) {
        if (defaultNamespaceURI != null && defaultNamespaceURI.equals(uri)) {
            return XMLConstants.DEFAULT_NS_PREFIX;
        } else if (uri == null) {
            throw new IllegalArgumentException("Null not allowed as prefix");
        } else if (uri.equals(XMLConstants.XML_NS_URI)) {
            return XMLConstants.XML_NS_PREFIX;
        } else if (uri.equals(XMLConstants.XMLNS_ATTRIBUTE_NS_URI)) {
            return XMLConstants.XMLNS_ATTRIBUTE;
        } else {
            return namespaceIdsMap.get(uri);
        }
    }

    @Override
    public Iterator getPrefixes(String uri) {
        return Collections.singletonList(getPrefix(uri)).iterator();
    }
}
