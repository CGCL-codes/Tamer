package org.asam.ods;

/**
 *	Generated from IDL definition of alias "SS_BYTE"
 *	@author JacORB IDL compiler 
 */
public final class SS_BYTEHolder implements org.omg.CORBA.portable.Streamable {

    public byte[][] value;

    public SS_BYTEHolder() {
    }

    public SS_BYTEHolder(final byte[][] initial) {
        value = initial;
    }

    public org.omg.CORBA.TypeCode _type() {
        return SS_BYTEHelper.type();
    }

    public void _read(final org.omg.CORBA.portable.InputStream in) {
        value = SS_BYTEHelper.read(in);
    }

    public void _write(final org.omg.CORBA.portable.OutputStream out) {
        SS_BYTEHelper.write(out, value);
    }
}
