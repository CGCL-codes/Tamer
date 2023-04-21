package org.fudaa.dodico.corba.mesure;

/**
   * Service information grandeur.
   */
public final class IInformationsGrandeurHolder implements org.omg.CORBA.portable.Streamable {

    public org.fudaa.dodico.corba.mesure.IInformationsGrandeur value = null;

    public IInformationsGrandeurHolder() {
    }

    public IInformationsGrandeurHolder(org.fudaa.dodico.corba.mesure.IInformationsGrandeur initialValue) {
        value = initialValue;
    }

    public void _read(org.omg.CORBA.portable.InputStream i) {
        value = org.fudaa.dodico.corba.mesure.IInformationsGrandeurHelper.read(i);
    }

    public void _write(org.omg.CORBA.portable.OutputStream o) {
        org.fudaa.dodico.corba.mesure.IInformationsGrandeurHelper.write(o, value);
    }

    public org.omg.CORBA.TypeCode _type() {
        return org.fudaa.dodico.corba.mesure.IInformationsGrandeurHelper.type();
    }
}
