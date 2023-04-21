package org.omg.TimeBase;

public final class ulonglongHolder implements org.omg.CORBA.portable.Streamable {

    public ulonglong value;

    public ulonglongHolder() {
    }

    public ulonglongHolder(ulonglong initial) {
        value = initial;
    }

    public void _read(org.omg.CORBA.portable.InputStream in) {
        value = ulonglongHelper.read(in);
    }

    public void _write(org.omg.CORBA.portable.OutputStream out) {
        ulonglongHelper.write(out, value);
    }

    public org.omg.CORBA.TypeCode _type() {
        return ulonglongHelper.type();
    }
}
