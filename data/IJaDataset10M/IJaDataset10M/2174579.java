package com.cbsgmbh.xi.eDIFACT;

public class Type7009_Type extends com.sap.aii.proxy.xiruntime.core.EnumerationType implements java.io.Serializable {

    private static final long serialVersionUID = 297498813L;

    private final java.lang.String name;

    public static final java.lang.String _value1 = "3";

    /** instance for value 3 */
    public static final Type7009_Type value1 = new Type7009_Type(_value1);

    public static final java.lang.String _value2 = "5";

    /** instance for value 5 */
    public static final Type7009_Type value2 = new Type7009_Type(_value2);

    public static final java.lang.String _value3 = "2";

    /** instance for value 2 */
    public static final Type7009_Type value3 = new Type7009_Type(_value3);

    public static final java.lang.String _value4 = "4";

    /** instance for value 4 */
    public static final Type7009_Type value4 = new Type7009_Type(_value4);

    public static final java.lang.String _value5 = "1";

    /** instance for value 1 */
    public static final Type7009_Type value5 = new Type7009_Type(_value5);

    private Type7009_Type(java.lang.String name) {
        this.name = name;
    }

    public static Type7009_Type fromString(java.lang.String name) {
        if (name.equals("3")) {
            return value1;
        }
        ;
        if (name.equals("5")) {
            return value2;
        }
        ;
        if (name.equals("2")) {
            return value3;
        }
        ;
        if (name.equals("4")) {
            return value4;
        }
        ;
        if (name.equals("1")) {
            return value5;
        }
        ;
        throw createIllegalArgumentException(name);
    }

    public static Type7009_Type fromValue(java.lang.String name) {
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
        if (name.equals(_value4)) {
            return value4;
        }
        ;
        if (name.equals(_value5)) {
            return value5;
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
