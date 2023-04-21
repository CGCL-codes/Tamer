package com.cbsgmbh.xi.eDIFACT;

public class Type5273_Type extends com.sap.aii.proxy.xiruntime.core.EnumerationType implements java.io.Serializable {

    private static final long serialVersionUID = 553877382L;

    private final java.lang.String name;

    public static final java.lang.String _value1 = "3";

    /** instance for value 3 */
    public static final Type5273_Type value1 = new Type5273_Type(_value1);

    public static final java.lang.String _value2 = "2";

    /** instance for value 2 */
    public static final Type5273_Type value2 = new Type5273_Type(_value2);

    public static final java.lang.String _value3 = "1";

    /** instance for value 1 */
    public static final Type5273_Type value3 = new Type5273_Type(_value3);

    private Type5273_Type(java.lang.String name) {
        this.name = name;
    }

    public static Type5273_Type fromString(java.lang.String name) {
        if (name.equals("3")) {
            return value1;
        }
        ;
        if (name.equals("2")) {
            return value2;
        }
        ;
        if (name.equals("1")) {
            return value3;
        }
        ;
        throw createIllegalArgumentException(name);
    }

    public static Type5273_Type fromValue(java.lang.String name) {
        if (name.equals(_value1)) {
            return value1;
        }
        ;
        if (name.equals(_value2)) {
            return value2;
        }
        ;
        if (name.equals(_value3)) {
            return value3;
        }
        ;
        throw createIllegalArgumentException(name);
    }

    public java.lang.String toString() {
        return java.lang.String.valueOf(name);
    }

    public java.lang.String getValue() {
        return name;
    }
}
