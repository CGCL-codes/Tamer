package org.rickmurphy.exchangepackage.disclosure.impl.runtime;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.xml.XMLConstants;
import org.xml.sax.SAXException;
import com.sun.xml.bind.marshaller.NamespacePrefixMapper;
import com.sun.xml.bind.marshaller.NamespaceSupport;

/**
 * Implementation of the NamespaceContext2.
 * 
 * This class also provides several utility methods for
 * XMLSerializer-derived classes.
 * 
 * The startElement method and the endElement method need to be called
 * appropriately when used. See javadoc for those methods for details.
 */
public class NamespaceContextImpl implements NamespaceContext2 {

    /**
     * Sequence generator. Used as the last resort to generate
     * unique prefix.
     */
    private int iota = 1;

    /**
     * Used to maintain association between prefixes and URIs.
     */
    private final NamespaceSupport nss = new NamespaceSupport();

    /**
     * A flag that indicates the current mode of this object.
     */
    private boolean inCollectingMode;

    /** Assigns prefixes to URIs. Can be null. */
    private final NamespacePrefixMapper prefixMapper;

    /**
     * Used during the collecting mode to sort out the namespace
     * URIs we need for this element.
     * 
     * A map from prefixes to namespace URIs.
     */
    private final Map decls = new HashMap();

    private final Map reverseDecls = new HashMap();

    public NamespaceContextImpl(NamespacePrefixMapper _prefixMapper) {
        this.prefixMapper = _prefixMapper;
        nss.declarePrefix("", "");
        nss.declarePrefix("xmlns", XMLConstants.XMLNS_ATTRIBUTE_NS_URI);
    }

    public final NamespacePrefixMapper getNamespacePrefixMapper() {
        return prefixMapper;
    }

    /**
     * @param requirePrefix
     *      true if this is called for attribute name. false otherwise.
     */
    public String declareNamespace(String namespaceUri, String preferedPrefix, boolean requirePrefix) {
        if (!inCollectingMode) {
            if (!requirePrefix && nss.getURI("").equals(namespaceUri)) return "";
            if (requirePrefix) return nss.getPrefix2(namespaceUri); else return nss.getPrefix(namespaceUri);
        } else {
            if (requirePrefix && namespaceUri.length() == 0) return "";
            String prefix = (String) reverseDecls.get(namespaceUri);
            if (prefix != null) {
                if (!requirePrefix || prefix.length() != 0) {
                    return prefix;
                } else {
                    decls.remove(prefix);
                    reverseDecls.remove(namespaceUri);
                }
            }
            if (namespaceUri.length() == 0) {
                prefix = "";
            } else {
                prefix = nss.getPrefix(namespaceUri);
                if (prefix == null) prefix = (String) reverseDecls.get(namespaceUri);
                if (prefix == null) {
                    if (prefixMapper != null) prefix = prefixMapper.getPreferredPrefix(namespaceUri, preferedPrefix, requirePrefix); else prefix = preferedPrefix;
                    if (prefix == null) prefix = "ns" + (iota++);
                }
            }
            if (requirePrefix && prefix.length() == 0) prefix = "ns" + (iota++);
            while (true) {
                String existingUri = (String) decls.get(prefix);
                if (existingUri == null) {
                    decls.put(prefix, namespaceUri);
                    reverseDecls.put(namespaceUri, prefix);
                    return prefix;
                }
                if (existingUri.length() == 0) {
                    ;
                } else {
                    decls.put(prefix, namespaceUri);
                    reverseDecls.put(namespaceUri, prefix);
                    namespaceUri = existingUri;
                }
                prefix = "ns" + (iota++);
            }
        }
    }

    public String getPrefix(String namespaceUri) {
        return declareNamespace(namespaceUri, null, false);
    }

    /**
     * Obtains the namespace URI currently associated to the given prefix.
     * If no namespace URI is associated, return null.
     */
    public String getNamespaceURI(String prefix) {
        String uri = (String) decls.get(prefix);
        if (uri != null) return uri;
        return nss.getURI(prefix);
    }

    public Iterator getPrefixes(String namespaceUri) {
        Set s = new HashSet();
        String prefix = (String) reverseDecls.get(namespaceUri);
        if (prefix != null) s.add(prefix);
        if (nss.getURI("").equals(namespaceUri)) s.add("");
        for (Enumeration e = nss.getPrefixes(namespaceUri); e.hasMoreElements(); ) s.add(e.nextElement());
        return s.iterator();
    }

    /**
     * Sets the current bindings aside and starts a new element context.
     * 
     * This method should be called at the beginning of the startElement method
     * of the Serializer implementation.
     */
    public void startElement() {
        nss.pushContext();
        inCollectingMode = true;
    }

    /**
     * Reconciles the namespace URI/prefix mapping requests since the
     * last startElement method invocation and finalizes them.
     * 
     * This method must be called after all the necessary namespace URIs 
     * for this element is reported through the declareNamespace method
     * or the getPrefix method.
     */
    public void endNamespaceDecls() {
        if (!decls.isEmpty()) {
            for (Iterator itr = decls.entrySet().iterator(); itr.hasNext(); ) {
                Map.Entry e = (Map.Entry) itr.next();
                String prefix = (String) e.getKey();
                String uri = (String) e.getValue();
                if (!uri.equals(nss.getURI(prefix))) nss.declarePrefix(prefix, uri);
            }
            decls.clear();
            reverseDecls.clear();
        }
        inCollectingMode = false;
    }

    /**
     * Ends the current element context and gets back to the parent context.
     * 
     * This method should be called at the end of the endElement method
     * of derived classes.
     */
    public void endElement() {
        nss.popContext();
    }

    /** Iterates all newly declared namespace prefixes for this element. */
    public void iterateDeclaredPrefixes(PrefixCallback callback) throws SAXException {
        for (Enumeration e = nss.getDeclaredPrefixes(); e.hasMoreElements(); ) {
            String p = (String) e.nextElement();
            String uri = nss.getURI(p);
            callback.onPrefixMapping(p, uri);
        }
    }
}
