package org.fudaa.dodico.corba.sinavi2;

/**
* org/fudaa/dodico/corba/sinavi2/SParametresIndispoLoiJHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from C:/devel/fudaa/fudaa_devel/dodico/idl/code/sinavi2.idl
* mercredi 6 ao�t 2008 22 h 27 CEST
*/
public final class SParametresIndispoLoiJHolder implements org.omg.CORBA.portable.Streamable {

    public org.fudaa.dodico.corba.sinavi2.SParametresIndispoLoiJ value = null;

    public SParametresIndispoLoiJHolder() {
    }

    public SParametresIndispoLoiJHolder(org.fudaa.dodico.corba.sinavi2.SParametresIndispoLoiJ initialValue) {
        value = initialValue;
    }

    public void _read(org.omg.CORBA.portable.InputStream i) {
        value = org.fudaa.dodico.corba.sinavi2.SParametresIndispoLoiJHelper.read(i);
    }

    public void _write(org.omg.CORBA.portable.OutputStream o) {
        org.fudaa.dodico.corba.sinavi2.SParametresIndispoLoiJHelper.write(o, value);
    }

    public org.omg.CORBA.TypeCode _type() {
        return org.fudaa.dodico.corba.sinavi2.SParametresIndispoLoiJHelper.type();
    }
}