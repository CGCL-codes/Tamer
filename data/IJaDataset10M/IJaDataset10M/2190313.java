package com.sun.org.apache.xerces.internal.impl.dtd;

/**
 * @version $Id: XMLEntityDecl.java,v 1.1.2.1 2005/08/01 03:36:45 jeffsuttor Exp $
 */
public class XMLEntityDecl {

    /** name */
    public String name;

    /** publicId */
    public String publicId;

    /** systemId */
    public String systemId;

    /** baseSystemId */
    public String baseSystemId;

    /** notation */
    public String notation;

    /** isPE */
    public boolean isPE;

    /** <strong>Note:</strong> flag of where the entity is defined, not whether it is a external entity */
    public boolean inExternal;

    /** Value. */
    public String value;

    /**
     * setValues
     * 
     * @param name 
     * @param publicId 
     * @param systemId 
     * @param baseSystemId 
     * @param notation 
     * @param isPE 
     * @param inExternal
     */
    public void setValues(String name, String publicId, String systemId, String baseSystemId, String notation, boolean isPE, boolean inExternal) {
        setValues(name, publicId, systemId, baseSystemId, notation, null, isPE, inExternal);
    }

    /**
     * setValues
     * 
     * @param name 
     * @param publicId 
     * @param systemId 
     * @param baseSystemId 
     * @param value
     * @param notation 
     * @param isPE 
     * @param inExternal
     */
    public void setValues(String name, String publicId, String systemId, String baseSystemId, String notation, String value, boolean isPE, boolean inExternal) {
        this.name = name;
        this.publicId = publicId;
        this.systemId = systemId;
        this.baseSystemId = baseSystemId;
        this.notation = notation;
        this.value = value;
        this.isPE = isPE;
        this.inExternal = inExternal;
    }

    /**
     * clear
     */
    public void clear() {
        this.name = null;
        this.publicId = null;
        this.systemId = null;
        this.baseSystemId = null;
        this.notation = null;
        this.value = null;
        this.isPE = false;
        this.inExternal = false;
    }
}
