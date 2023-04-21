package org.fudaa.dodico.corba.objet;

public final class ITacheHolder implements org.omg.CORBA.portable.Streamable {

    public org.fudaa.dodico.corba.objet.ITache value = null;

    public ITacheHolder() {
    }

    public ITacheHolder(org.fudaa.dodico.corba.objet.ITache initialValue) {
        value = initialValue;
    }

    public void _read(org.omg.CORBA.portable.InputStream i) {
        value = org.fudaa.dodico.corba.objet.ITacheHelper.read(i);
    }

    public void _write(org.omg.CORBA.portable.OutputStream o) {
        org.fudaa.dodico.corba.objet.ITacheHelper.write(o, value);
    }

    public org.omg.CORBA.TypeCode _type() {
        return org.fudaa.dodico.corba.objet.ITacheHelper.type();
    }
}
