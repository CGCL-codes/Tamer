package org.apache.xerces.impl.dtd;

import org.apache.xerces.xni.QName;

/**
 * @xerces.internal
 * 
 * @version $Id: XMLAttributeDecl.java 446755 2006-09-15 21:56:27Z mrglavas $
 */
public class XMLAttributeDecl {

    /** name */
    public final QName name = new QName();

    /** simpleType */
    public final XMLSimpleType simpleType = new XMLSimpleType();

    /** optional */
    public boolean optional;

    /**
     * setValues
     * 
     * @param name 
     * @param simpleType 
     * @param optional 
     */
    public void setValues(QName name, XMLSimpleType simpleType, boolean optional) {
        this.name.setValues(name);
        this.simpleType.setValues(simpleType);
        this.optional = optional;
    }

    /**
     * clear
     */
    public void clear() {
        this.name.clear();
        this.simpleType.clear();
        this.optional = false;
    }
}
