package org.fudaa.dodico.corba.reflux2dv;

/**
* org/fudaa/dodico/corba/reflux2dv/VSParametresReflux2dvSollicitationsConcentreesHelper.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from C:/devel/fudaa/fudaa_devel/dodico/idl/code/reflux2dv.idl
* mercredi 6 ao�t 2008 22 h 27 CEST
*/
public abstract class VSParametresReflux2dvSollicitationsConcentreesHelper {

    private static String _id = "IDL:reflux2dv/VSParametresReflux2dvSollicitationsConcentrees:1.0";

    public static void insert(org.omg.CORBA.Any a, org.fudaa.dodico.corba.reflux2dv.SParametresReflux2dvSollicitationsConcentrees[] that) {
        org.omg.CORBA.portable.OutputStream out = a.create_output_stream();
        a.type(type());
        write(out, that);
        a.read_value(out.create_input_stream(), type());
    }

    public static org.fudaa.dodico.corba.reflux2dv.SParametresReflux2dvSollicitationsConcentrees[] extract(org.omg.CORBA.Any a) {
        return read(a.create_input_stream());
    }

    private static org.omg.CORBA.TypeCode __typeCode = null;

    public static synchronized org.omg.CORBA.TypeCode type() {
        if (__typeCode == null) {
            __typeCode = org.fudaa.dodico.corba.reflux2dv.SParametresReflux2dvSollicitationsConcentreesHelper.type();
            __typeCode = org.omg.CORBA.ORB.init().create_sequence_tc(0, __typeCode);
            __typeCode = org.omg.CORBA.ORB.init().create_alias_tc(org.fudaa.dodico.corba.reflux2dv.VSParametresReflux2dvSollicitationsConcentreesHelper.id(), "VSParametresReflux2dvSollicitationsConcentrees", __typeCode);
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static org.fudaa.dodico.corba.reflux2dv.SParametresReflux2dvSollicitationsConcentrees[] read(org.omg.CORBA.portable.InputStream istream) {
        org.fudaa.dodico.corba.reflux2dv.SParametresReflux2dvSollicitationsConcentrees value[] = null;
        int _len0 = istream.read_long();
        value = new org.fudaa.dodico.corba.reflux2dv.SParametresReflux2dvSollicitationsConcentrees[_len0];
        for (int _o1 = 0; _o1 < value.length; ++_o1) value[_o1] = org.fudaa.dodico.corba.reflux2dv.SParametresReflux2dvSollicitationsConcentreesHelper.read(istream);
        return value;
    }

    public static void write(org.omg.CORBA.portable.OutputStream ostream, org.fudaa.dodico.corba.reflux2dv.SParametresReflux2dvSollicitationsConcentrees[] value) {
        ostream.write_long(value.length);
        for (int _i0 = 0; _i0 < value.length; ++_i0) org.fudaa.dodico.corba.reflux2dv.SParametresReflux2dvSollicitationsConcentreesHelper.write(ostream, value[_i0]);
    }
}
