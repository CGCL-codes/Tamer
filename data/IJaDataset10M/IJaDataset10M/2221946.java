package com.metanology.mde.core.metaModel;

/**
 * Contains enumerations for the visibility of a model element.
 */
public class ScopeEnum {

    /**
     * Indicates public scope.
     */
    public static final int PUBLIC = 11;

    /**
     * Indicates protected scope.
     */
    public static final int PROTECTED = 12;

    /**
     * Indicates private scope.
     */
    public static final int PRIVATE = 13;

    /**
     * Indicates implementation scope.
     */
    public static final int IMPLEMENTATION = 14;

    /**
     * Return the ENUM value from a string 
     * the string value is the attribute name.
     */
    public static final int valueOf(String val) {
        String lVal = val.toLowerCase();
        if (lVal.indexOf("public") >= 0) {
            return ScopeEnum.PUBLIC;
        } else if (lVal.indexOf("protected") >= 0) {
            return ScopeEnum.PROTECTED;
        } else if (lVal.indexOf("private") >= 0) {
            return ScopeEnum.PRIVATE;
        } else if (lVal.indexOf("implementation") >= 0) {
            return ScopeEnum.IMPLEMENTATION;
        } else {
            return -1;
        }
    }

    /**
     * Return the ENUM value from a zero base index
     * e.g. 0 == first ENUM value
     */
    public static final int valueOf(int index) {
        if (index == 0) {
            return ScopeEnum.PUBLIC;
        } else if (index == 1) {
            return ScopeEnum.PROTECTED;
        } else if (index == 2) {
            return ScopeEnum.PRIVATE;
        } else if (index == 3) {
            return ScopeEnum.IMPLEMENTATION;
        } else {
            return -1;
        }
    }

    /**
     * Return the zero based index from an int value.
     */
    public static final int indexOf(int val) {
        if (val == ScopeEnum.PUBLIC) {
            return 0;
        } else if (val == ScopeEnum.PROTECTED) {
            return 1;
        } else if (val == ScopeEnum.PRIVATE) {
            return 2;
        } else if (val == ScopeEnum.IMPLEMENTATION) {
            return 3;
        } else {
            return -1;
        }
    }

    private static final String[] keys = new String[] { "ScopeEnum.PUBLIC", "ScopeEnum.PROTECTED", "ScopeEnum.PRIVATE", "ScopeEnum.IMPLEMENTATION" };

    /**
     * Returns the string list of key to resource file
     */
    public static String[] getMsgKeys() {
        return keys;
    }

    public static String getMsgKey(int value) {
        try {
            return keys[indexOf(value)];
        } catch (Exception e) {
            return "";
        }
    }
}
