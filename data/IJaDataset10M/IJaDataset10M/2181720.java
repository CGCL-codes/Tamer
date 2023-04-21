package org.fudaa.dodico.corba.hydraulique1d.calageauto;

public class LTypeLit implements org.omg.CORBA.portable.IDLEntity {

    private int __value;

    private static int __size = 2;

    private static org.fudaa.dodico.corba.hydraulique1d.calageauto.LTypeLit[] __array = new org.fudaa.dodico.corba.hydraulique1d.calageauto.LTypeLit[__size];

    public static final int _MINEUR = 0;

    public static final org.fudaa.dodico.corba.hydraulique1d.calageauto.LTypeLit MINEUR = new org.fudaa.dodico.corba.hydraulique1d.calageauto.LTypeLit(_MINEUR);

    public static final int _MAJEUR = 1;

    public static final org.fudaa.dodico.corba.hydraulique1d.calageauto.LTypeLit MAJEUR = new org.fudaa.dodico.corba.hydraulique1d.calageauto.LTypeLit(_MAJEUR);

    public int value() {
        return __value;
    }

    public static org.fudaa.dodico.corba.hydraulique1d.calageauto.LTypeLit from_int(int value) {
        if (value >= 0 && value < __size) return __array[value]; else throw new org.omg.CORBA.BAD_PARAM();
    }

    protected LTypeLit(int value) {
        __value = value;
        __array[__value] = this;
    }
}
