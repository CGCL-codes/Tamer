package net.noderunner.exml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This Element stores its name and attributes, and may contain
 * children.  In that way, this Element object is similar to the
 * <code>org.w3c.dom.Element</code>.  It has a few convenience functions
 * for getting at the character data of children.
 * The {@link Namespace} interface is implemented, however, its
 * methods, excepting {@link Namespace#getName}, merely return
 * <code>null</code>.
 */
public class Element implements Node, Namespace {

    private String name;

    private List<Attribute> attributes;

    private List<Node> children;

    private boolean open;

    /**
	 * Constructs a simple open element from a string name.
	 * @param name name of this element
	 */
    public Element(String name) {
        this.name = name;
        this.open = true;
    }

    /**
	 * Returns <code>ELEMENT_NODE</code>.
	 */
    public final short getNodeType() {
        return ELEMENT_NODE;
    }

    /**
	 * Constructs an Element with typical parameters.
	 * Note:  For performance reasons, the passed in List object is not
	 * copied.
	 * @param name Name of element
	 * @param attributes List of attributes, or null if no attributes
	 * @param open If this tag is open or closed 
	 */
    public Element(String name, List<Attribute> attributes, boolean open) {
        this.name = name;
        this.attributes = attributes;
        this.open = open;
    }

    /**
	 * Returns true if this element is open.  A closed element
	 * can have no children.
	 */
    public boolean isOpen() {
        return open;
    }

    /**
	 * Makes this element open or closed.  If this element is closed,
	 * all child elements are erased.
	 * @see #clearChildren
	 */
    public void setOpen(boolean open) {
        if (!open) clearChildren();
        this.open = open;
    }

    /**
	 * Returns the name of this element.
	 */
    public String getName() {
        return name;
    }

    /**
	 * Sets the name of this element.
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * Sets the attributes of this element.
	 */
    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    /**
	 * Returns the attributes of this element as a list.
	 */
    public List<Attribute> getAttributes() {
        return attributes;
    }

    /**
	 * Returns true if there is no namespace URI defined in ns1 and
	 * the qualified names are the same.
	 * Or, returns true if the namespace URI match and the local names
	 * are the same.
	 */
    boolean equal(Namespace ns1, Namespace ns2) {
        if (ns1.getNamespaceURI() == null && ns1.getName() != null && ns1.getName().equals(ns2.getName())) {
            return true;
        }
        if (ns1.getNamespaceURI() == null || ns1.getLocalName() == null) return false;
        return ns1.getNamespaceURI().equals(ns2.getNamespaceURI()) && ns1.getLocalName().equals(ns2.getLocalName());
    }

    /**
	 * Returns the value of an attribute with the given name, or <code>null</code>
	 * if no matching attribute was found.
	 * @param name name of the attribute
	 */
    public String getAttValue(String name) {
        return getAttValue(name, null);
    }

    /**
	 * Returns the value of an attribute with the given name.
	 * @param name name of the attribute
	 * @param dflt value returned if no matching attribute was found
	 */
    public String getAttValue(String name, String dflt) {
        if (attributes == null) return dflt;
        for (int i = 0; i < attributes.size(); i++) {
            Attribute a = attributes.get(i);
            if (a.getName().equals(name)) return a.getValue();
        }
        return dflt;
    }

    /**
	 * Returns the value of an attribute with the given namespace,
	 * or <code>null</code> if no matching attribute was found.
	 * @param ns namespace of the attribute
	 */
    public String getAttValue(Namespace ns) {
        return getAttValue(ns, null);
    }

    /**
	 * Returns the value of an attribute with the given namespace.
	 * @param ns namespace of the attribute
	 * @param dflt value returned if no matching attribute was found
	 */
    public String getAttValue(Namespace ns, String dflt) {
        if (attributes == null) return dflt;
        for (int i = 0; i < attributes.size(); i++) {
            Attribute a = attributes.get(i);
            if (equal(ns, a)) return a.getValue();
        }
        return dflt;
    }

    /**
	 * Returns a character data string concatenated from all character
	 * data child nodes.
	 * @throws ElementException
	 * @return a character data string;
	 * <code>null</code> is never returned, an empty
	 * string is returned even if no character data was found.
	 */
    public String getCharacterData() {
        if (children == null) return "";
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < children.size(); i++) {
            Node n = children.get(i);
            if (n.getNodeType() == TEXT_NODE) {
                CharacterData d = (CharacterData) n;
                XmlCharArrayWriter w = d.getWriter();
                sb.append(w.getBuffer(), 0, w.size());
            }
        }
        return sb.toString();
    }

    /**
	 * Returns a list of the child nodes.
	 * @return null if no children are in this object
	 */
    public List<Node> getChildNodes() {
        return children;
    }

    /**
	 * Appends an attribute to this Element.
	 * @param a attribute to append
	 * @throws ElementException
	 */
    public void appendAttribute(Attribute a) {
        if (attributes == null) {
            attributes = new ArrayList<Attribute>();
        }
        attributes.add(a);
    }

    /**
	 * Appends a child node to this Element.
	 * @param n child node to append
	 * @throws ElementException
	 */
    public void appendChild(Node n) {
        if (!open) throw new ElementException("Attempt to add child to closed element");
        if (children == null) {
            children = new ArrayList<Node>();
        }
        children.add(n);
    }

    /**
	 * Returns the n-th child of this Element, starting with 0.
	 * @return a node or null if no such node exists
	 */
    public Node getChild(int n) {
        if (children == null) return null;
        if (children.size() <= n) return null;
        return children.get(n);
    }

    /**
	 * Returns the n-th child element of this element, starting with 0.
	 * @return an element or null if no such element exists
	 */
    public Element getChildElement(int n) {
        if (children == null) return null;
        int count = 0;
        Iterator<Node> i = children.iterator();
        while (i.hasNext()) {
            Node node = i.next();
            if (node.getNodeType() == ELEMENT_NODE) {
                if (count == n) return (Element) node;
                count++;
            }
        }
        return null;
    }

    /**
	 * Returns the n-th child element of this element, starting with 0,
	 * with the given Namespace.
	 * @return an element or null if no such element
	 */
    public Element getChildElement(Namespace ns, int n) {
        if (children == null) return null;
        int count = 0;
        Iterator<Node> i = children.iterator();
        while (i.hasNext()) {
            Node node = i.next();
            if (node.getNodeType() == ELEMENT_NODE) {
                Element e = (Element) node;
                if (equal(ns, e)) {
                    if (count == n) return e;
                    count++;
                }
            }
        }
        return null;
    }

    /**
	 * Returns the n-th child element of this element, starting with 0,
	 * with the given name.
	 * @return an element or null if no such element
	 */
    public Element getChildElement(String name, int n) {
        if (children == null) return null;
        int count = 0;
        Iterator<Node> i = children.iterator();
        while (i.hasNext()) {
            Node node = i.next();
            if (node.getNodeType() == ELEMENT_NODE) {
                Element e = (Element) node;
                if (e.getName().equals(name)) {
                    if (count == n) return e;
                    count++;
                }
            }
        }
        return null;
    }

    /**
	 * Clears the children of this element.  This can be used to release
	 * memory held by this object.
	 */
    public void clearChildren() {
        children = null;
    }

    /**
	 * By default, returns null.
	 * @see ElementNS#getNamespaceURI
	 */
    public String getNamespaceURI() {
        return null;
    }

    /**
	 * By default, returns null.
	 * @see ElementNS#getPrefix
	 */
    public String getPrefix() {
        return null;
    }

    /**
	 * By default, returns null.
	 * @see ElementNS#getLocalName
	 */
    public String getLocalName() {
        return null;
    }

    /**
	 * Returns a string representation of this Element.
	 */
    @Override
    public String toString() {
        XmlCharArrayWriter w = new XmlCharArrayWriter();
        XmlWriter xw = new XmlWriter(w);
        try {
            xw.element(this);
            xw.flush();
        } catch (IOException e) {
            throw new ElementException(e.toString());
        }
        return w.toString();
    }
}
