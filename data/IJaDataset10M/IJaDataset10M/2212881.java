package org.asam.ods;

/**
 *	Generated from IDL definition of alias "ApplAttrSequence"
 *	@author JacORB IDL compiler 
 */
public final class ApplAttrSequenceHolder implements org.omg.CORBA.portable.Streamable {

    public org.asam.ods.ApplAttr[] value;

    public ApplAttrSequenceHolder() {
    }

    public ApplAttrSequenceHolder(final org.asam.ods.ApplAttr[] initial) {
        value = initial;
    }

    public org.omg.CORBA.TypeCode _type() {
        return ApplAttrSequenceHelper.type();
    }

    public void _read(final org.omg.CORBA.portable.InputStream in) {
        value = ApplAttrSequenceHelper.read(in);
    }

    public void _write(final org.omg.CORBA.portable.OutputStream out) {
        ApplAttrSequenceHelper.write(out, value);
    }
}
