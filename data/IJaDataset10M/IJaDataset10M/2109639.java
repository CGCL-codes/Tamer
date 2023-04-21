package org.omg.CORBA.FT;

/**
 *	Generated from IDL definition of interface "ReplicationManagerEx"
 *	@author JacORB IDL compiler 
 */
public final class ReplicationManagerExHolder implements org.omg.CORBA.portable.Streamable {

    public ReplicationManagerEx value;

    public ReplicationManagerExHolder() {
    }

    public ReplicationManagerExHolder(final ReplicationManagerEx initial) {
        value = initial;
    }

    public org.omg.CORBA.TypeCode _type() {
        return ReplicationManagerExHelper.type();
    }

    public void _read(final org.omg.CORBA.portable.InputStream in) {
        value = ReplicationManagerExHelper.read(in);
    }

    public void _write(final org.omg.CORBA.portable.OutputStream _out) {
        ReplicationManagerExHelper.write(_out, value);
    }
}
