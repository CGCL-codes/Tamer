package org.fudaa.dodico.corba.objet;

public final class IPersonneHolder implements org.omg.CORBA.portable.Streamable {

    public org.fudaa.dodico.corba.objet.IPersonne value = null;

    public IPersonneHolder() {
    }

    public IPersonneHolder(org.fudaa.dodico.corba.objet.IPersonne initialValue) {
        value = initialValue;
    }

    public void _read(org.omg.CORBA.portable.InputStream i) {
        value = org.fudaa.dodico.corba.objet.IPersonneHelper.read(i);
    }

    public void _write(org.omg.CORBA.portable.OutputStream o) {
        org.fudaa.dodico.corba.objet.IPersonneHelper.write(o, value);
    }

    public org.omg.CORBA.TypeCode _type() {
        return org.fudaa.dodico.corba.objet.IPersonneHelper.type();
    }
}
