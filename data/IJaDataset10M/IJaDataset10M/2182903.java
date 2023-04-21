package org.asam.ods;

/**
 *	Generated from IDL definition of alias "S_SHORT"
 *	@author JacORB IDL compiler 
 */
public final class S_SHORTHelper {

    private static org.omg.CORBA.TypeCode _type = null;

    public static void insert(org.omg.CORBA.Any any, short[] s) {
        any.type(type());
        write(any.create_output_stream(), s);
    }

    public static short[] extract(final org.omg.CORBA.Any any) {
        return read(any.create_input_stream());
    }

    public static org.omg.CORBA.TypeCode type() {
        if (_type == null) {
            _type = org.omg.CORBA.ORB.init().create_alias_tc(org.asam.ods.S_SHORTHelper.id(), "S_SHORT", org.omg.CORBA.ORB.init().create_sequence_tc(0, org.asam.ods.T_SHORTHelper.type()));
        }
        return _type;
    }

    public static String id() {
        return "IDL:org/asam/ods/S_SHORT:1.0";
    }

    public static short[] read(final org.omg.CORBA.portable.InputStream _in) {
        short[] _result;
        int _l_result6 = _in.read_long();
        _result = new short[_l_result6];
        for (int i = 0; i < _result.length; i++) {
            _result[i] = _in.read_short();
        }
        return _result;
    }

    public static void write(final org.omg.CORBA.portable.OutputStream _out, short[] _s) {
        _out.write_long(_s.length);
        for (int i = 0; i < _s.length; i++) {
            _out.write_short(_s[i]);
        }
    }
}
