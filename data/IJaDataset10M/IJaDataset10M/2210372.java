package org.omg.CORBA.FT;

/**
 *	Generated from IDL definition of alias "ReplicationStyleValue"
 *	@author JacORB IDL compiler 
 */
public final class ReplicationStyleValueHelper {

    private static org.omg.CORBA.TypeCode _type = org.omg.CORBA.ORB.init().create_alias_tc(org.omg.CORBA.FT.ReplicationStyleValueHelper.id(), "ReplicationStyleValue", org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.from_int(4)));

    public static void insert(final org.omg.CORBA.Any any, final short s) {
        any.type(type());
        write(any.create_output_stream(), s);
    }

    public static short extract(final org.omg.CORBA.Any any) {
        return read(any.create_input_stream());
    }

    public static org.omg.CORBA.TypeCode type() {
        return _type;
    }

    public static String id() {
        return "IDL:omg.org/CORBA/FT/ReplicationStyleValue:1.0";
    }

    public static short read(final org.omg.CORBA.portable.InputStream _in) {
        short _result;
        _result = _in.read_ushort();
        return _result;
    }

    public static void write(final org.omg.CORBA.portable.OutputStream _out, short _s) {
        _out.write_ushort(_s);
    }
}
