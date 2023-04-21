package org.asam.ods;

/**
 *	Generated from IDL definition of alias "NameSequence"
 *	@author JacORB IDL compiler 
 */
public final class NameSequenceHolder implements org.omg.CORBA.portable.Streamable {

    public java.lang.String[] value;

    public NameSequenceHolder() {
    }

    public NameSequenceHolder(final java.lang.String[] initial) {
        value = initial;
    }

    public org.omg.CORBA.TypeCode _type() {
        return NameSequenceHelper.type();
    }

    public void _read(final org.omg.CORBA.portable.InputStream in) {
        value = NameSequenceHelper.read(in);
    }

    public void _write(final org.omg.CORBA.portable.OutputStream out) {
        NameSequenceHelper.write(out, value);
    }
}
