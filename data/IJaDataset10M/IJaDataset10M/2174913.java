package org.fudaa.dodico.corba.navmer;

/**
* org/fudaa/dodico/corba/navmer/SCommandeNavireHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from C:/devel/fudaa/fudaa_devel/dodico/idl/code/navmer.idl
* mercredi 6 ao�t 2008 22 h 26 CEST
*/
public final class SCommandeNavireHolder implements org.omg.CORBA.portable.Streamable {

    public org.fudaa.dodico.corba.navmer.SCommandeNavire value = null;

    public SCommandeNavireHolder() {
    }

    public SCommandeNavireHolder(org.fudaa.dodico.corba.navmer.SCommandeNavire initialValue) {
        value = initialValue;
    }

    public void _read(org.omg.CORBA.portable.InputStream i) {
        value = org.fudaa.dodico.corba.navmer.SCommandeNavireHelper.read(i);
    }

    public void _write(org.omg.CORBA.portable.OutputStream o) {
        org.fudaa.dodico.corba.navmer.SCommandeNavireHelper.write(o, value);
    }

    public org.omg.CORBA.TypeCode _type() {
        return org.fudaa.dodico.corba.navmer.SCommandeNavireHelper.type();
    }
}
