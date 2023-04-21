package com.xsm.lite.util;

import java.util.Collection;

/**
 * A simple utility to build toString() for an object.
 * 
 * @author Sony Mathew
 */
public class ToStringBuilder {

    private final Class<?> toStringClass;

    private final StringBuffer propsString = new StringBuffer();

    private static final String SEP = "|";

    /**
     * Provide the Class for whose object instance which we are building properties String.
     * 
     * author Sony Mathew
     */
    public ToStringBuilder(Class<?> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("Cannot build String for null Class type");
        }
        toStringClass = clazz;
    }

    /**
     * Provide the Object for which we are building properties String.
     * Shorthand for ToStringBuilder(obj.getClass()).
     * 
     * author Sony Mathew
     */
    public ToStringBuilder(Object obj) {
        this(obj.getClass());
    }

    /**
     * Appends the given property with the given property name.
     * 
     * author Sony Mathew
     */
    public ToStringBuilder append(String propertyName, Object property) {
        return append(propertyName + "=" + property);
    }

    /**
     * Appends the given property using its toString(), 
     * No property name specified which means its toString() provides the neccessary info.
     * 
     * author Sony Mathew
     */
    public ToStringBuilder append(Object property) {
        if (propsString.length() > 0) {
            propsString.append(SEP);
        }
        propsString.append("" + property);
        return this;
    }

    /**
     * Appends the string form of the given collection.
     *  
     * author Sony Mathew
     */
    public ToStringBuilder append(String propName, Collection<? extends Object> col) {
        String colVal = "";
        if (col == null) {
            colVal = "null";
        } else {
            StringBuffer s = new StringBuffer();
            for (Object o : col) {
                if (s.length() > 0) {
                    s.append(SEP);
                }
                s.append(o);
            }
            colVal = "Collection[" + s.toString() + "]";
        }
        return append(propName, colVal);
    }

    private String getClassName() {
        String className = toStringClass.getName();
        int lastDot = className.lastIndexOf(".");
        if (lastDot > 0) {
            return className.substring(lastDot + 1, className.length());
        } else {
            return className;
        }
    }

    @Override
    public String toString() {
        return getClassName() + "[" + propsString + "]";
    }
}
