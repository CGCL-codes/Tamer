package org.omg.CORBA.FT;

/**
 *	Generated from IDL definition of struct "TagFTGroupTaggedComponent"
 *	@author JacORB IDL compiler 
 */
public final class TagFTGroupTaggedComponentHolder implements org.omg.CORBA.portable.Streamable {

    public org.omg.CORBA.FT.TagFTGroupTaggedComponent value;

    public TagFTGroupTaggedComponentHolder() {
    }

    public TagFTGroupTaggedComponentHolder(final org.omg.CORBA.FT.TagFTGroupTaggedComponent initial) {
        value = initial;
    }

    public org.omg.CORBA.TypeCode _type() {
        return org.omg.CORBA.FT.TagFTGroupTaggedComponentHelper.type();
    }

    public void _read(final org.omg.CORBA.portable.InputStream _in) {
        value = org.omg.CORBA.FT.TagFTGroupTaggedComponentHelper.read(_in);
    }

    public void _write(final org.omg.CORBA.portable.OutputStream _out) {
        org.omg.CORBA.FT.TagFTGroupTaggedComponentHelper.write(_out, value);
    }
}
