package org.fcrepo.common.xml.namespace;

/**
 * The XMLNS XML namespace.
 * 
 * <pre>
 * Namespace URI    : http://www.w3.org/2000/xmlns/ 
 * Preferred Prefix : xmlns
 * </pre>
 * 
 * @author Chris Wilper
 */
public class XMLNSNamespace extends XMLNamespace {

    /** The <code>xmlns</code> attribute. */
    public final QName XMLNS;

    /** The only instance of this class. */
    private static final XMLNSNamespace ONLY_INSTANCE = new XMLNSNamespace();

    /**
     * Constructs the instance.
     */
    private XMLNSNamespace() {
        super("http://www.w3.org/2000/xmlns/", "xmlns");
        XMLNS = new QName(this, "xmlns");
    }

    /**
     * Gets the only instance of this class.
     * 
     * @return the instance.
     */
    public static XMLNSNamespace getInstance() {
        return ONLY_INSTANCE;
    }
}
