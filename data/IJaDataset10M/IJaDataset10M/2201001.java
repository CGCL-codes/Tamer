package org.asam.ods;

/**
 *	Generated from IDL definition of enum "SeverityFlag"
 *	@author JacORB IDL compiler 
 */
public final class SeverityFlagHolder implements org.omg.CORBA.portable.Streamable {

    public SeverityFlag value;

    public SeverityFlagHolder() {
    }

    public SeverityFlagHolder(final SeverityFlag initial) {
        value = initial;
    }

    public org.omg.CORBA.TypeCode _type() {
        return SeverityFlagHelper.type();
    }

    public void _read(final org.omg.CORBA.portable.InputStream in) {
        value = SeverityFlagHelper.read(in);
    }

    public void _write(final org.omg.CORBA.portable.OutputStream out) {
        SeverityFlagHelper.write(out, value);
    }
}
