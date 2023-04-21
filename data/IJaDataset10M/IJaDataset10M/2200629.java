package org.omg.CosTrading.LookupPackage;

public class PreferenceHolder implements org.omg.CORBA.portable.Streamable {

    public java.lang.String value;

    public PreferenceHolder() {
    }

    public PreferenceHolder(java.lang.String initial) {
        value = initial;
    }

    public void _read(org.omg.CORBA.portable.InputStream istream) {
        value = PreferenceHelper.read(istream);
    }

    public void _write(org.omg.CORBA.portable.OutputStream ostream) {
        PreferenceHelper.write(ostream, value);
    }

    public org.omg.CORBA.TypeCode _type() {
        return PreferenceHelper.type();
    }
}
