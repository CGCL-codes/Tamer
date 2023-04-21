package org.xmlcml.cml.base;

import nu.xom.Attribute;

/**
 * attribute representing an int value.
 * 
 */
public class DoubleAttribute extends CMLAttribute {

    /** */
    public static final String JAVA_TYPE = "double";

    /** */
    public static final String JAVA_GET_METHOD = "getDouble";

    /** */
    public static final String JAVA_SHORT_CLASS = "DoubleAttribute";

    protected Double d;

    /**
     * constructor.
     * 
     * @param name
     */
    public DoubleAttribute(String name) {
        super(name);
    }

    /**
     * from DOM.
     * 
     * @param att
     */
    public DoubleAttribute(Attribute att) {
        this(att.getLocalName());
        String v = att.getValue();
        if (v != null && !v.trim().equals(S_EMPTY)) {
            this.setCMLValue(v);
        }
    }

    /**
     * from DOM.
     * 
     * @param att
     *            to copy, except value
     * @param value
     */
    public DoubleAttribute(Attribute att, String value) {
        super(att, value.trim());
    }

    /**
     * copy constructor
     * 
     * @param att
     */
    public DoubleAttribute(DoubleAttribute att) {
        super(att);
        if (att.d != null) {
            this.d = new Double(att.d.doubleValue());
        }
    }

    /**
     * get java type.
     * 
     * @return java type
     */
    public String getJavaType() {
        return "double";
    }

    /**
     * sets value. throws exception if of wrong type or violates restriction
     * 
     * @param s
     *            the value
     * @throws CMLRuntimeException
     */
    public void setCMLValue(String s) {
        double d;
        try {
            d = new Double(s.trim()).doubleValue();
        } catch (NumberFormatException nfe) {
            throw new CMLRuntimeException("" + nfe);
        }
        this.setCMLValue(d);
    }

    /**
     * checks value of simpleType. throws CMLException if value does not check
     * against SimpleType uses CMLType.checkvalue() fails if type is String or
     * int or is a list
     * 
     * @param d
     *            the double
     * @throws CMLRuntimeException
     *             wrong type or value fails
     */
    public void checkValue(double d) throws CMLRuntimeException {
        if (schemaType != null) {
            schemaType.checkValue(d);
        }
    }

    /**
     * set and check value.
     * 
     * @param d
     */
    public void setCMLValue(double d) {
        checkValue(d);
        this.d = new Double(d);
        this.setValue("" + d);
    }

    /**
     * get double.
     * 
     * @return value
     */
    public double getDouble() {
        return d.doubleValue();
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
