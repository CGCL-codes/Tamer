package nu.staldal.xtree;

import java.net.URL;
import org.xml.sax.*;

/**
 * Base class for a node in an XTree. 
 */
public abstract class Node implements java.io.Serializable, Locator {

    /**
     * Namespace URI for the implicitly defined "xml" namespace.
     */
    public static final String XML_NS = "http://www.w3.org/XML/1998/namespace";

    private String publicId = null;

    private String systemId = null;

    private int line = -1;

    private int column = -1;

    protected NodeWithChildren parent = null;

    void setParent(NodeWithChildren n) {
        parent = n;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    /**
	 * Get the parent of this node.
	 *
	 * @return  the parent of this node, 
	 * or <code>null</code> if this node has no parent.
	 */
    public NodeWithChildren getParent() {
        return parent;
    }

    /**
	 * Serialize this node, and recursively the (sub)tree beneath, 
	 * into SAX2 events.
	 *
	 * @param sax  the SAX2 ContentHander to fire events on. 
	 */
    public abstract void toSAX(ContentHandler sax) throws SAXException;

    /**
     * Return the public identifier for this node. Useful for error reporting.
     *
     * The return value is the public identifier of the document
     * entity or of the external parsed entity.
     *
     * @return A string containing the public identifier, or null
     *         if none is available.
     */
    public String getPublicId() {
        return publicId;
    }

    /**
     * Return the system identifier for this node. Useful for error reporting.
     *
     * The return value is the system identifier of the document
     * entity or of the external parsed entity.
     *
     * @return A string containing the system identifier, or null
     *         if none is available.
     */
    public String getSystemId() {
        return systemId;
    }

    /**
     * Return the line number where this node ends. Useful for error reporting.
     *
     * The return value is an approximation of the line number
     * in the document entity or external parsed entity.
     *
	 * The first line in the document is line 1.
     *
     * @return The line number, or -1 if none is available.
     * @see #getColumnNumber()
     */
    public int getLineNumber() {
        return line;
    }

    /**
     * Return the column number where this node ends. Useful for error reporting.
     *
     * The return value is an approximation of the column number
     * in the document entity or external parsed entity.
     *
	 * The first column in each line is column 1.
     *
     * @return The column number, or -1 if none is available.
     * @see #getLineNumber()
     */
    public int getColumnNumber() {
        return column;
    }

    /**
	 * Lookup the namespace URI which has been mapped to a prefix.
	 *
	 * @param prefix  the prefix, may be the empty string which denotes
	 *  the default namespace.
	 *
	 * @return the namespace URI, or <code>null</code> 
	 *  if the prefix is not mapped to any namespace URI, 
	 *  or the empty string of prefix is the empty string and there is no
	 *  default namespace mapping.
	 */
    public String lookupNamespaceURI(String prefix) {
        if (parent == null) return null; else return parent.lookupNamespaceURI(prefix);
    }

    /**
	 * Lookup a prefix which has been mapped to a namespace URI.
	 *
	 * @param URI  the namespace URI
	 *
	 * @return any of the prefixes which has been mapped to the namespace URI, 
	 *  or <code>null</code> if no prefix is mapped to the namespace URI. 
	 */
    public String lookupNamespacePrefix(String URI) {
        if (parent == null) return null; else return parent.lookupNamespacePrefix(URI);
    }

    /**
	 * Returns the absolute base URI of this node.
	 *
	 * @return  the absolute base URI of this node,
	 * or <code>null</code> if unknown.
	 */
    public URL getBaseURI() {
        if (parent == null) return null; else return parent.getBaseURI();
    }

    /**
	 * Return the value of any xml:space attribute in force for this node.
	 *
	 * @return  <code>true</code> if an xml:space="preserve" is in effect
	 */
    public boolean getPreserveSpace() {
        if (parent == null) return false; else return parent.getPreserveSpace();
    }

    /**
	 * Return the value of an inherited attribute. If the given attribute 
     * occurs on this node, return its value, otherwise recursivley search
     * the parent of this node (return <code>null</code> if the root is 
     * reached without finding the attribute). Useful for e.g. xml:lang.
     *
	 * @param namespaceURI  the namespace URI, may be the empty string
	 * @param localName  the attribute name
	 *
	 * @return  <code>null</code> if no such attribute is found
	 */
    public String getInheritedAttribute(String namespaceURI, String localName) {
        if (parent == null) return null; else return parent.getInheritedAttribute(namespaceURI, localName);
    }

    /**
     * Check if this node consist of whitespace only.
     *
     * @return <code>true</code> if and only if this is a Text node which
     *                           contains no other characters than whitespace.
     */
    public boolean isWhitespaceNode() {
        return false;
    }
}
