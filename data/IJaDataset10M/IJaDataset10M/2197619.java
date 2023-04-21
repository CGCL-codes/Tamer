package org.omg.CosTrading;

public class ConstraintHolder implements org.omg.CORBA.portable.Streamable {

    public java.lang.String value;

    public ConstraintHolder() {
    }

    public ConstraintHolder(java.lang.String initial) {
        value = initial;
    }

    public void _read(org.omg.CORBA.portable.InputStream istream) {
        value = ConstraintHelper.read(istream);
    }

    public void _write(org.omg.CORBA.portable.OutputStream ostream) {
        ConstraintHelper.write(ostream, value);
    }

    public org.omg.CORBA.TypeCode _type() {
        return ConstraintHelper.type();
    }
}
