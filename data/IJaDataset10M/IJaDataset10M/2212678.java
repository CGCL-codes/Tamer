package org.omg.CORBA.FT;

/**
 *	Generated from IDL definition of alias "ObjectGroupId"
 *	@author JacORB IDL compiler 
 */
public final class ObjectGroupIdHelper {

    private static org.omg.CORBA.TypeCode _type = org.omg.CORBA.ORB.init().create_alias_tc(org.omg.CORBA.FT.ObjectGroupIdHelper.id(), "ObjectGroupId", org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.from_int(24)));

    public static void insert(final org.omg.CORBA.Any any, final long s) {
        any.type(type());
        write(any.create_output_stream(), s);
    }

    public static long extract(final org.omg.CORBA.Any any) {
        return read(any.create_input_stream());
    }

    public static org.omg.CORBA.TypeCode type() {
        return _type;
    }

    public static String id() {
        return "IDL:omg.org/CORBA/FT/ObjectGroupId:1.0";
    }

    public static long read(final org.omg.CORBA.portable.InputStream _in) {
        long _result;
        _result = _in.read_ulonglong();
        return _result;
    }

    public static void write(final org.omg.CORBA.portable.OutputStream _out, long _s) {
        _out.write_ulonglong(_s);
    }
}
