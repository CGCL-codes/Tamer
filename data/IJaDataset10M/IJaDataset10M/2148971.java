package org.omg.TimeBase;

public class InaccuracyTHolder implements org.omg.CORBA.portable.Streamable {

    public org.omg.TimeBase.ulonglong value;

    public InaccuracyTHolder() {
    }

    public InaccuracyTHolder(org.omg.TimeBase.ulonglong initial) {
        value = initial;
    }

    public void _read(org.omg.CORBA.portable.InputStream istream) {
        value = InaccuracyTHelper.read(istream);
    }

    public void _write(org.omg.CORBA.portable.OutputStream ostream) {
        InaccuracyTHelper.write(ostream, value);
    }

    public org.omg.CORBA.TypeCode _type() {
        return InaccuracyTHelper.type();
    }
}
