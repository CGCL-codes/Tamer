package org.asam.ods;

/**
 *	Generated from IDL interface "EnumerationDefinition"
 *	@author JacORB IDL compiler V 2.2.3, 10-Dec-2005
 */
public final class EnumerationDefinitionHolder implements org.omg.CORBA.portable.Streamable {

    public EnumerationDefinition value;

    public EnumerationDefinitionHolder() {
    }

    public EnumerationDefinitionHolder(final EnumerationDefinition initial) {
        value = initial;
    }

    public org.omg.CORBA.TypeCode _type() {
        return EnumerationDefinitionHelper.type();
    }

    public void _read(final org.omg.CORBA.portable.InputStream in) {
        value = EnumerationDefinitionHelper.read(in);
    }

    public void _write(final org.omg.CORBA.portable.OutputStream _out) {
        EnumerationDefinitionHelper.write(_out, value);
    }
}
