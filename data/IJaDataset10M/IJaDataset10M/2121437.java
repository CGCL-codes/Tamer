package org.fudaa.dodico.corba.hydraulique1d.calageauto;

public abstract class LTypeLitHelper {

    private static String _id = "IDL:hydraulique1d/calageauto/LTypeLit:1.0";

    public static void insert(org.omg.CORBA.Any a, org.fudaa.dodico.corba.hydraulique1d.calageauto.LTypeLit that) {
        org.omg.CORBA.portable.OutputStream out = a.create_output_stream();
        a.type(type());
        write(out, that);
        a.read_value(out.create_input_stream(), type());
    }

    public static org.fudaa.dodico.corba.hydraulique1d.calageauto.LTypeLit extract(org.omg.CORBA.Any a) {
        return read(a.create_input_stream());
    }

    private static org.omg.CORBA.TypeCode __typeCode = null;

    public static synchronized org.omg.CORBA.TypeCode type() {
        if (__typeCode == null) {
            __typeCode = org.omg.CORBA.ORB.init().create_enum_tc(org.fudaa.dodico.corba.hydraulique1d.calageauto.LTypeLitHelper.id(), "LTypeLit", new String[] { "MINEUR", "MAJEUR" });
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static org.fudaa.dodico.corba.hydraulique1d.calageauto.LTypeLit read(org.omg.CORBA.portable.InputStream istream) {
        return org.fudaa.dodico.corba.hydraulique1d.calageauto.LTypeLit.from_int(istream.read_long());
    }

    public static void write(org.omg.CORBA.portable.OutputStream ostream, org.fudaa.dodico.corba.hydraulique1d.calageauto.LTypeLit value) {
        ostream.write_long(value.value());
    }
}
