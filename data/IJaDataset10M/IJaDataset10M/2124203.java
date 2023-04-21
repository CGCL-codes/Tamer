package org.omg.CosPropertyService;

public final class ExceptionReasonHolder implements org.omg.CORBA.portable.Streamable {

    public ExceptionReason value;

    public ExceptionReasonHolder() {
    }

    public ExceptionReasonHolder(ExceptionReason initial) {
        value = initial;
    }

    public void _read(org.omg.CORBA.portable.InputStream in) {
        value = ExceptionReasonHelper.read(in);
    }

    public void _write(org.omg.CORBA.portable.OutputStream out) {
        ExceptionReasonHelper.write(out, value);
    }

    public org.omg.CORBA.TypeCode _type() {
        return ExceptionReasonHelper.type();
    }
}
