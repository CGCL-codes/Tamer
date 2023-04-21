package com.anaxima.eslink.model.core;

/**
 * Type save enumeration of data column types.
 * <p>
 * @author Thomas Vater
 */
public class EColumnType {

    /**
     * Constant for type character.
     */
    public static final EColumnType CHAR = new EColumnType("CHAR");

    /**
     * Constant for type numeric.
     */
    public static final EColumnType NUM = new EColumnType("NUM");

    /**
     * Internal value.
     */
    private String _value;

    /**
     * Internal constructor.
     */
    private EColumnType(String argValue) {
        _value = argValue;
    }

    /**
     * Returns a type from a string value.
     * The type {@link #CHAR} is returned if there is no
     * type matching the given value.
     */
    public static EColumnType fromString(String argValue) {
        if ("1".equals(argValue)) return NUM;
        if ("2".equals(argValue)) return CHAR;
        if (NUM._value.equalsIgnoreCase(argValue)) return NUM;
        return CHAR;
    }
}
