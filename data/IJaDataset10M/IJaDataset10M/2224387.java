package org.asam.ods;

/**
 *	Generated from IDL definition of enum "SelOperator"
 *	@author JacORB IDL compiler 
 */
public final class SelOperatorHolder implements org.omg.CORBA.portable.Streamable {

    public SelOperator value;

    public SelOperatorHolder() {
    }

    public SelOperatorHolder(final SelOperator initial) {
        value = initial;
    }

    public org.omg.CORBA.TypeCode _type() {
        return SelOperatorHelper.type();
    }

    public void _read(final org.omg.CORBA.portable.InputStream in) {
        value = SelOperatorHelper.read(in);
    }

    public void _write(final org.omg.CORBA.portable.OutputStream out) {
        SelOperatorHelper.write(out, value);
    }
}
