package com.cbsgmbh.xi.eDIFACT;

public class Type7077_Type extends com.sap.aii.proxy.xiruntime.core.EnumerationType implements java.io.Serializable {

    private static final long serialVersionUID = -2137168314L;

    private final java.lang.String name;

    public static final java.lang.String _S = "S";

    /** instance for value S */
    public static final Type7077_Type S = new Type7077_Type(_S);

    public static final java.lang.String _D = "D";

    /** instance for value D */
    public static final Type7077_Type D = new Type7077_Type(_D);

    public static final java.lang.String _A = "A";

    /** instance for value A */
    public static final Type7077_Type A = new Type7077_Type(_A);

    public static final java.lang.String _F = "F";

    /** instance for value F */
    public static final Type7077_Type F = new Type7077_Type(_F);

    public static final java.lang.String _C = "C";

    /** instance for value C */
    public static final Type7077_Type C = new Type7077_Type(_C);

    public static final java.lang.String _B = "B";

    /** instance for value B */
    public static final Type7077_Type B = new Type7077_Type(_B);

    public static final java.lang.String _X = "X";

    /** instance for value X */
    public static final Type7077_Type X = new Type7077_Type(_X);

    public static final java.lang.String _E = "E";

    /** instance for value E */
    public static final Type7077_Type E = new Type7077_Type(_E);

    private Type7077_Type(java.lang.String name) {
        this.name = name;
    }

    public static Type7077_Type fromString(java.lang.String name) {
        if (name.equals("S")) {
            return S;
        }
        ;
        if (name.equals("D")) {
            return D;
        }
        ;
        if (name.equals("A")) {
            return A;
        }
        ;
        if (name.equals("F")) {
            return F;
        }
        ;
        if (name.equals("C")) {
            return C;
        }
        ;
        if (name.equals("B")) {
            return B;
        }
        ;
        if (name.equals("X")) {
            return X;
        }
        ;
        if (name.equals("E")) {
            return E;
        }
        ;
        throw createIllegalArgumentException(name);
    }

    public static Type7077_Type fromValue(java.lang.String name) {
        if (name.equals(_S)) {
            return S;
        }
        ;
        if (name.equals(_D)) {
            return D;
        }
        ;
        if (name.equals(_A)) {
            return A;
        }
        ;
        if (name.equals(_F)) {
            return F;
        }
        ;
        if (name.equals(_C)) {
            return C;
        }
        ;
        if (name.equals(_B)) {
            return B;
        }
        ;
        if (name.equals(_X)) {
            return X;
        }
        ;
        if (name.equals(_E)) {
            return E;
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
