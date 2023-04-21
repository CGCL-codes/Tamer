package org.fudaa.dodico.corba.ef;

/**
   * Trous.
   */
public final class ITrouHolder implements org.omg.CORBA.portable.Streamable {

    public org.fudaa.dodico.corba.ef.ITrou value = null;

    public ITrouHolder() {
    }

    public ITrouHolder(org.fudaa.dodico.corba.ef.ITrou initialValue) {
        value = initialValue;
    }

    public void _read(org.omg.CORBA.portable.InputStream i) {
        value = org.fudaa.dodico.corba.ef.ITrouHelper.read(i);
    }

    public void _write(org.omg.CORBA.portable.OutputStream o) {
        org.fudaa.dodico.corba.ef.ITrouHelper.write(o, value);
    }

    public org.omg.CORBA.TypeCode _type() {
        return org.fudaa.dodico.corba.ef.ITrouHelper.type();
    }
}
