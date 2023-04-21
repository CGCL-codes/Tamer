package org.fudaa.dodico.corba.ef;

/**
   * Une interface definissant un noeud.
   */
public final class INoeudHolder implements org.omg.CORBA.portable.Streamable {

    public org.fudaa.dodico.corba.ef.INoeud value = null;

    public INoeudHolder() {
    }

    public INoeudHolder(org.fudaa.dodico.corba.ef.INoeud initialValue) {
        value = initialValue;
    }

    public void _read(org.omg.CORBA.portable.InputStream i) {
        value = org.fudaa.dodico.corba.ef.INoeudHelper.read(i);
    }

    public void _write(org.omg.CORBA.portable.OutputStream o) {
        org.fudaa.dodico.corba.ef.INoeudHelper.write(o, value);
    }

    public org.omg.CORBA.TypeCode _type() {
        return org.fudaa.dodico.corba.ef.INoeudHelper.type();
    }
}
