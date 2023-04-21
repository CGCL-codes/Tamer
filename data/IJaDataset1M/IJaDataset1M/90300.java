package org.omg.CORBA.FT;

/**
 *	Generated from IDL definition of alias "Properties"
 *	@author JacORB IDL compiler 
 */
public final class PropertiesHelper {

    private static org.omg.CORBA.TypeCode _type = org.omg.CORBA.ORB.init().create_alias_tc(org.omg.CORBA.FT.PropertiesHelper.id(), "Properties", org.omg.CORBA.ORB.init().create_sequence_tc(0, org.omg.CORBA.ORB.init().create_struct_tc(org.omg.CORBA.FT.PropertyHelper.id(), "Property", new org.omg.CORBA.StructMember[] { new org.omg.CORBA.StructMember("nam", org.omg.CORBA.ORB.init().create_alias_tc(org.omg.CORBA.FT.NameHelper.id(), "Name", org.omg.CORBA.ORB.init().create_alias_tc(org.omg.CosNaming.NameHelper.id(), "Name", org.omg.CORBA.ORB.init().create_sequence_tc(0, org.omg.CORBA.ORB.init().create_struct_tc(org.omg.CosNaming.NameComponentHelper.id(), "NameComponent", new org.omg.CORBA.StructMember[] { new org.omg.CORBA.StructMember("id", org.omg.CORBA.ORB.init().create_alias_tc(org.omg.CosNaming.IstringHelper.id(), "Istring", org.omg.CORBA.ORB.init().create_string_tc(0)), null), new org.omg.CORBA.StructMember("kind", org.omg.CORBA.ORB.init().create_alias_tc(org.omg.CosNaming.IstringHelper.id(), "Istring", org.omg.CORBA.ORB.init().create_string_tc(0)), null) })))), null), new org.omg.CORBA.StructMember("val", org.omg.CORBA.ORB.init().create_alias_tc(org.omg.CORBA.FT.ValueHelper.id(), "Value", org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.from_int(11))), null) })));

    public static void insert(final org.omg.CORBA.Any any, final org.omg.CORBA.FT.Property[] s) {
        any.type(type());
        write(any.create_output_stream(), s);
    }

    public static org.omg.CORBA.FT.Property[] extract(final org.omg.CORBA.Any any) {
        return read(any.create_input_stream());
    }

    public static org.omg.CORBA.TypeCode type() {
        return _type;
    }

    public static String id() {
        return "IDL:omg.org/CORBA/FT/Properties:1.0";
    }

    public static org.omg.CORBA.FT.Property[] read(final org.omg.CORBA.portable.InputStream _in) {
        org.omg.CORBA.FT.Property[] _result;
        int _l_result0 = _in.read_long();
        _result = new org.omg.CORBA.FT.Property[_l_result0];
        for (int i = 0; i < _result.length; i++) {
            _result[i] = org.omg.CORBA.FT.PropertyHelper.read(_in);
        }
        return _result;
    }

    public static void write(final org.omg.CORBA.portable.OutputStream _out, org.omg.CORBA.FT.Property[] _s) {
        _out.write_long(_s.length);
        for (int i = 0; i < _s.length; i++) {
            org.omg.CORBA.FT.PropertyHelper.write(_out, _s[i]);
        }
    }
}
