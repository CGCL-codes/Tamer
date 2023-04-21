package org.fudaa.dodico.corba.navigation;

/**
   * Definition generale d'un bassin.
   */
public final class IBassinHolder implements org.omg.CORBA.portable.Streamable {

    public org.fudaa.dodico.corba.navigation.IBassin value = null;

    public IBassinHolder() {
    }

    public IBassinHolder(org.fudaa.dodico.corba.navigation.IBassin initialValue) {
        value = initialValue;
    }

    public void _read(org.omg.CORBA.portable.InputStream i) {
        value = org.fudaa.dodico.corba.navigation.IBassinHelper.read(i);
    }

    public void _write(org.omg.CORBA.portable.OutputStream o) {
        org.fudaa.dodico.corba.navigation.IBassinHelper.write(o, value);
    }

    public org.omg.CORBA.TypeCode _type() {
        return org.fudaa.dodico.corba.navigation.IBassinHelper.type();
    }
}
