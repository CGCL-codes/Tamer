package org.opoo.oqs.type;

/**
 * <tt>true_false</tt>: A type that maps an SQL CHAR(1) to a Java Boolean.
 *
 * @author Alex Lin
 * @version 1.0
 */
public class TrueFalseType extends CharBooleanType {

    public TrueFalseType() {
        super();
    }

    protected final String getFalseString() {
        return "F";
    }

    protected final String getTrueString() {
        return "T";
    }
}
