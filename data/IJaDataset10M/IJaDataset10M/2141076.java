package org.omg.CosTrading;

public class PolicyNameHolder implements org.omg.CORBA.portable.Streamable {

    public java.lang.String value;

    public PolicyNameHolder() {
    }

    public PolicyNameHolder(java.lang.String initial) {
        value = initial;
    }

    public void _read(org.omg.CORBA.portable.InputStream istream) {
        value = PolicyNameHelper.read(istream);
    }

    public void _write(org.omg.CORBA.portable.OutputStream ostream) {
        PolicyNameHelper.write(ostream, value);
    }

    public org.omg.CORBA.TypeCode _type() {
        return PolicyNameHelper.type();
    }
}
