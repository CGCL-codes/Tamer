package com.siemens.ct.exi;

import javax.xml.namespace.QName;

/**
 * Enhanced qualified name storing namespaceUri and localName using ID's
 * 
 * @author Daniel.Peintner.EXT@siemens.com
 * @author Joerg.Heuer@siemens.com
 * 
 * @version 0.8
 */
public class EnhancedQName {

    protected final QName qname;

    protected int namespaceUriID;

    protected int localNameID;

    public EnhancedQName(final QName qname) {
        this(qname, -1, -1);
    }

    public EnhancedQName(final QName qname, final int namespaceUriID, final int localNameID) {
        this.qname = qname;
        this.namespaceUriID = namespaceUriID;
        this.localNameID = localNameID;
    }

    public void setNamespaceUriID(final int namespaceUriID) {
        this.namespaceUriID = namespaceUriID;
    }

    public int getNamespaceUriID() {
        return namespaceUriID;
    }

    public void setLocalNameID(final int localNameID) {
        this.localNameID = localNameID;
    }

    public int getLocalNameID() {
        return localNameID;
    }

    public QName getQName() {
        return qname;
    }

    @Override
    public final boolean equals(Object objectToTest) {
        if (objectToTest == null || !(objectToTest instanceof EnhancedQName)) {
            return false;
        }
        EnhancedQName eqName = (EnhancedQName) objectToTest;
        return this.qname.equals(eqName.qname);
    }

    @Override
    public final int hashCode() {
        return qname.hashCode();
    }

    @Override
    public String toString() {
        return "{" + namespaceUriID + "," + qname.getNamespaceURI() + "}" + localNameID + "," + qname.getLocalPart();
    }
}
