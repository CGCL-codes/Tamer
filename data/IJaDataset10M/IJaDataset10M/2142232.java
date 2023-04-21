package org.asam.ods;

/**
 *	Generated from IDL definition of alias "ElemResultSetSequence"
 *	@author JacORB IDL compiler 
 */
public final class ElemResultSetSequenceHelper {

    private static org.omg.CORBA.TypeCode _type = null;

    public static void insert(org.omg.CORBA.Any any, org.asam.ods.ElemResultSet[] s) {
        any.type(type());
        write(any.create_output_stream(), s);
    }

    public static org.asam.ods.ElemResultSet[] extract(final org.omg.CORBA.Any any) {
        return read(any.create_input_stream());
    }

    public static org.omg.CORBA.TypeCode type() {
        if (_type == null) {
            _type = org.omg.CORBA.ORB.init().create_alias_tc(org.asam.ods.ElemResultSetSequenceHelper.id(), "ElemResultSetSequence", org.omg.CORBA.ORB.init().create_sequence_tc(0, org.asam.ods.ElemResultSetHelper.type()));
        }
        return _type;
    }

    public static String id() {
        return "IDL:org/asam/ods/ElemResultSetSequence:1.0";
    }

    public static org.asam.ods.ElemResultSet[] read(final org.omg.CORBA.portable.InputStream _in) {
        org.asam.ods.ElemResultSet[] _result;
        int _l_result62 = _in.read_long();
        _result = new org.asam.ods.ElemResultSet[_l_result62];
        for (int i = 0; i < _result.length; i++) {
            _result[i] = org.asam.ods.ElemResultSetHelper.read(_in);
        }
        return _result;
    }

    public static void write(final org.omg.CORBA.portable.OutputStream _out, org.asam.ods.ElemResultSet[] _s) {
        _out.write_long(_s.length);
        for (int i = 0; i < _s.length; i++) {
            org.asam.ods.ElemResultSetHelper.write(_out, _s[i]);
        }
    }
}
