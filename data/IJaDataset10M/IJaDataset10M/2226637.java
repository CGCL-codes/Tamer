package com.siemens.ct.exi.core.container;

/**
 * 
 * @author Daniel.Peintner.EXT@siemens.com
 * @author Joerg.Heuer@siemens.com
 * 
 * @version 0.8
 */
public class NamespaceDeclaration {

    public final String namespaceURI;

    public final String prefix;

    public NamespaceDeclaration(String namespaceURI, String prefix) {
        this.namespaceURI = namespaceURI;
        this.prefix = prefix;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof NamespaceDeclaration) {
            NamespaceDeclaration other = (NamespaceDeclaration) o;
            return (namespaceURI.equals(other.namespaceURI) && prefix.equals(other.prefix));
        }
        return false;
    }

    @Override
    public int hashCode() {
        return namespaceURI.hashCode() ^ prefix.hashCode();
    }
}
