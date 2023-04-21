package org.omg.DfResourceAccessDecision;

public final class InternalErrorHolder implements org.omg.CORBA.portable.Streamable {

    public InternalError value;

    public InternalErrorHolder() {
    }

    public InternalErrorHolder(InternalError initial) {
        value = initial;
    }

    public void _read(org.omg.CORBA.portable.InputStream in) {
        value = InternalErrorHelper.read(in);
    }

    public void _write(org.omg.CORBA.portable.OutputStream out) {
        InternalErrorHelper.write(out, value);
    }

    public org.omg.CORBA.TypeCode _type() {
        return InternalErrorHelper.type();
    }
}
