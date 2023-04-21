package org.asam.ods;

/**
 *	Generated from IDL interface "NameValueUnitIterator"
 *	@author JacORB IDL compiler V 2.2.3, 10-Dec-2005
 */
public final class NameValueUnitIteratorHolder implements org.omg.CORBA.portable.Streamable {

    public NameValueUnitIterator value;

    public NameValueUnitIteratorHolder() {
    }

    public NameValueUnitIteratorHolder(final NameValueUnitIterator initial) {
        value = initial;
    }

    public org.omg.CORBA.TypeCode _type() {
        return NameValueUnitIteratorHelper.type();
    }

    public void _read(final org.omg.CORBA.portable.InputStream in) {
        value = NameValueUnitIteratorHelper.read(in);
    }

    public void _write(final org.omg.CORBA.portable.OutputStream _out) {
        NameValueUnitIteratorHelper.write(_out, value);
    }
}
