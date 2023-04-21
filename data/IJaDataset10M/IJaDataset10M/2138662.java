package org.xmlcml.cml.base;

import nu.xom.Attribute;
import org.xmlcml.euclid.Util;

/**
 * attribute representing a boolean value..
 */
public class BooleanArrayAttribute extends CMLAttribute {

    /** */
    public static final String JAVA_TYPE = "boolean[]";

    /** */
    public static final String JAVA_GET_METHOD = "getBooleanArray";

    /** */
    public static final String JAVA_SHORT_CLASS = "BooleanArrayAttribute";

    protected boolean[] bb = null;

    protected int length = -1;

    /**
     * constructor.
     * 
     * @param name
     */
    public BooleanArrayAttribute(String name) {
        super(name);
    }

    /**
     * from DOM.
     * 
     * @param att
     */
    public BooleanArrayAttribute(Attribute att) {
        this(att.getLocalName());
        this.setCMLValue(att.getValue());
    }

    /**
     * copy constructor
     * 
     * @param att
     */
    public BooleanArrayAttribute(BooleanArrayAttribute att) {
        super(att);
        if (att.bb != null) {
            this.bb = new boolean[att.bb.length];
            for (int i = 0; i < bb.length; i++) {
                this.bb[i] = att.bb[i];
            }
        }
        this.length = att.length;
    }

    /**
     * from DOM.
     * 
     * @param att
     *            to copy, except value
     * @param value
     */
    public BooleanArrayAttribute(Attribute att, String value) {
        super(att, value.trim().replace(S_WHITEREGEX, S_SPACE));
    }

    /**
     * sets and checks value.
     * 
     * @param bb
     */
    public void setCMLValue(boolean[] bb) {
        checkValue(bb);
        this.bb = new boolean[bb.length];
        for (int i = 0; i < bb.length; i++) {
            this.bb[i] = bb[i];
        }
        this.setValue(Util.concatenate(bb, " "));
    }

    /**
     * checks value of simpleType. throws CMLException if value does not check
     * against SimpleType uses CMLType.checkvalue() fails if type is String or
     * double or is not a list
     * 
     * @param bb
     *            the boolean array
     * @throws CMLRuntimeException
     *             wrong type or value fails
     */
    public void checkValue(boolean[] bb) {
        if (schemaType != null) {
            schemaType.checkValue(bb);
        }
    }

    /**
     * splits string into booleans.
     * 
     * @param s
     *            the string
     * @param delim
     *            delimiter
     * @return array
     * @throws CMLRuntimeException
     *             cannot parse as ints
     */
    public static boolean[] split(String s, String delim) {
        String sss = s.trim().replace(S_WHITEREGEX, S_SPACE);
        if (delim == null || delim.trim().equals(S_EMPTY) || delim.equals(S_WHITEREGEX)) {
            delim = S_WHITEREGEX;
            sss = sss.trim();
        }
        String[] ss = sss.split(delim);
        boolean[] bb = new boolean[ss.length];
        for (int i = 0; i < ss.length; i++) {
            try {
                bb[i] = Boolean.parseBoolean(ss[i]);
            } catch (NumberFormatException nfe) {
                throw new CMLRuntimeException("" + nfe);
            }
        }
        return bb;
    }

    /**
     * sets value. throws exception if of wrong type or violates restriction
     * 
     * @param s
     *            the value // *
     * @throws CMLRuntimeException
     */
    public void setCMLValue(String s) {
        boolean[] bb = split(s.trim(), S_WHITEREGEX);
        this.setCMLValue(bb);
    }

    /**
     * get array.
     * 
     * @return null if not set
     */
    public Object getCMLValue() {
        return bb;
    }

    /**
     * get array.
     * 
     * @return null if not set
     */
    public boolean[] getIntegerArray() {
        return bb;
    }

    /**
     * get java type.
     * 
     * @return java type
     */
    public String getJavaType() {
        return JAVA_TYPE;
    }

    /**
     * get java method.
     * 
     * @return java method
     */
    public String getJavaGetMethod() {
        return JAVA_GET_METHOD;
    }

    /**
     * get java short class name.
     * 
     * @return java short className
     */
    public String getJavaShortClassName() {
        return JAVA_SHORT_CLASS;
    }
}

;
