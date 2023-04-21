package com.volantis.mcs.xml.validation.sax;

/**
 * A class that is used to manage the predicate allocations for a nodes
 * children
 */
public final class XPathChild {

    /**
     * The name of the child
     */
    private String name;

    /**
     * The namespace that this child belongs to.
     */
    private String namespaceURI;

    /**
     * Used to record the number of children that have this name and namespace
     * for a given parent node.
     */
    private int count;

    /**
     * Initializes a <code>XPathChild</code> instance with the give parameters
     * @param name the name of the child. Cannot be null
     * @param namespaceURI the URI of the namespace that the child belongs to.
     * Cannot be null
     * @throws IllegalArgumentException if the name of namespaceURI
     * arguments are null.
     */
    public XPathChild(String name, String namespaceURI) {
        this.count = 1;
        setName(name);
        setNamespaceURI(namespaceURI);
    }

    public boolean equals(Object other) {
        boolean result = false;
        if (other != null) {
            if (other == this) {
                result = true;
            } else if (other.getClass() == getClass()) {
                XPathChild otherChild = (XPathChild) other;
                result = name.equals(otherChild.name) && namespaceURI.equals(otherChild.namespaceURI);
            }
        }
        return result;
    }

    public int hashCode() {
        return namespaceURI.hashCode() + name.hashCode();
    }

    /**
     * Set the name of this child. If the name parameter is null
     * an <code>IllegalArgumentException</code> exception will be
     * thrown.
     * @param name the name of the child
     */
    public void setName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("A non-null name must be provided");
        }
        this.name = name;
    }

    /**
     * Set the namespaceURI of this child. If the  parameter is null
     * an <code>IllegalArgumentException</code> exception will be
     * thrown.
     * @param namespaceURI the namespace of the child
     */
    public void setNamespaceURI(String namespaceURI) {
        if (namespaceURI == null) {
            throw new IllegalArgumentException("A non-null namespace must be provided");
        }
        this.namespaceURI = namespaceURI;
    }

    /**
     * Returns the number of children with this name in this namespace
     * @return the count
     */
    public int getCount() {
        return count;
    }

    /**
     * Increments the number of children with this name in this namespace
     */
    public void incrementCount() {
        this.count++;
    }
}
