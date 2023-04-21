package org.fudaa.dodico.corba.hydraulique1d.qualitedeau;

public final class IParametresModeleQualiteEauHolder implements org.omg.CORBA.portable.Streamable {

    public org.fudaa.dodico.corba.hydraulique1d.qualitedeau.IParametresModeleQualiteEau value = null;

    public IParametresModeleQualiteEauHolder() {
    }

    public IParametresModeleQualiteEauHolder(org.fudaa.dodico.corba.hydraulique1d.qualitedeau.IParametresModeleQualiteEau initialValue) {
        value = initialValue;
    }

    public void _read(org.omg.CORBA.portable.InputStream i) {
        value = org.fudaa.dodico.corba.hydraulique1d.qualitedeau.IParametresModeleQualiteEauHelper.read(i);
    }

    public void _write(org.omg.CORBA.portable.OutputStream o) {
        org.fudaa.dodico.corba.hydraulique1d.qualitedeau.IParametresModeleQualiteEauHelper.write(o, value);
    }

    public org.omg.CORBA.TypeCode _type() {
        return org.fudaa.dodico.corba.hydraulique1d.qualitedeau.IParametresModeleQualiteEauHelper.type();
    }
}
