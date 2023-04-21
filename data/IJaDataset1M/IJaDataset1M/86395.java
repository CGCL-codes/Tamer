package org.omg.CosTrading.LookupPackage;

public final class InvalidPolicyValueHolder implements org.omg.CORBA.portable.Streamable {

    public InvalidPolicyValue value;

    public InvalidPolicyValueHolder() {
    }

    public InvalidPolicyValueHolder(InvalidPolicyValue initial) {
        value = initial;
    }

    public void _read(org.omg.CORBA.portable.InputStream in) {
        value = InvalidPolicyValueHelper.read(in);
    }

    public void _write(org.omg.CORBA.portable.OutputStream out) {
        InvalidPolicyValueHelper.write(out, value);
    }

    public org.omg.CORBA.TypeCode _type() {
        return InvalidPolicyValueHelper.type();
    }
}
