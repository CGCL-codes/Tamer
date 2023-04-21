package tei.cr.utils.xml;

import javax.xml.namespace.QName;

public final class QNameUtils {

    private static final String EMPTY = "";

    private static final String COLON = ":";

    /**
     * Create the qualified name (prefix + ":" + local part) according to a
     * {@link QName}.
     * 
     * @return the qualified name : the local part alone if there is no prefix,
     *         or (prefix + ":" + local part) if there is a prefix.
     */
    public static final String getQName(QName name) {
        String prefix = name.getPrefix();
        String local = name.getLocalPart();
        if ((prefix == null) || (prefix.length() == 0)) {
            return local;
        } else {
            return prefix + COLON + local;
        }
    }

    /**
     * Create the namespace URI according to a {@link QName}.
     * 
     * @return the namespace URI : an empty String if {@link
     *         QName#getNamespaceURI()} is null or empty, the URI otherwise.
     */
    public static final String getNamespaceURI(QName name) {
        String URI = name.getNamespaceURI();
        if ((URI == null) || (URI.length() == 0)) {
            return EMPTY;
        } else {
            return URI;
        }
    }

    /**
     * retrieve the prefix part from a qname
     * copied from staxutils\StAXContentHandler.java
     */
    public static final String getPrefix(String qname) {
        String prefix;
        int idx = qname.indexOf(COLON);
        if (idx >= 0) {
            prefix = qname.substring(0, idx);
        } else {
            prefix = "";
        }
        return prefix;
    }
}
