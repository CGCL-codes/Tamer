package com.sun.org.apache.xerces.internal.impl.dtd;

import com.sun.org.apache.xerces.internal.xni.QName;
import com.sun.org.apache.xerces.internal.impl.dtd.models.ContentModelValidator;

/**
 * @version $Id: XMLElementDecl.java,v 1.1.2.1 2005/08/01 03:36:34 jeffsuttor Exp $
 */
public class XMLElementDecl {

    /** TYPE_ANY */
    public static final short TYPE_ANY = 0;

    /** TYPE_EMPTY */
    public static final short TYPE_EMPTY = 1;

    /** TYPE_MIXED */
    public static final short TYPE_MIXED = 2;

    /** TYPE_CHILDREN */
    public static final short TYPE_CHILDREN = 3;

    /** TYPE_SIMPLE */
    public static final short TYPE_SIMPLE = 4;

    /** name */
    public final QName name = new QName();

    /** scope */
    public int scope = -1;

    /** type */
    public short type = -1;

    /** contentModelValidator */
    public ContentModelValidator contentModelValidator;

    /** simpleType */
    public final XMLSimpleType simpleType = new XMLSimpleType();

    /**
     * setValues
     * 
     * @param name 
     * @param scope 
     * @param type 
     * @param contentModelValidator 
     * @param simpleType 
     */
    public void setValues(QName name, int scope, short type, ContentModelValidator contentModelValidator, XMLSimpleType simpleType) {
        this.name.setValues(name);
        this.scope = scope;
        this.type = type;
        this.contentModelValidator = contentModelValidator;
        this.simpleType.setValues(simpleType);
    }

    /**
     * clear
     */
    public void clear() {
        this.name.clear();
        this.type = -1;
        this.scope = -1;
        this.contentModelValidator = null;
        this.simpleType.clear();
    }
}
