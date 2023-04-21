package org.omg.CosTrading;

public final class LinkNameHelper {

    public static void insert(org.omg.CORBA.Any any, String _ob_v) {
        org.omg.CORBA.portable.OutputStream out = any.create_output_stream();
        write(out, _ob_v);
        any.read_value(out.create_input_stream(), type());
    }

    public static String extract(org.omg.CORBA.Any any) {
        if (any.type().equal(type())) return read(any.create_input_stream()); else throw new org.omg.CORBA.BAD_OPERATION();
    }

    private static org.omg.CORBA.TypeCode typeCode_;

    public static org.omg.CORBA.TypeCode type() {
        if (typeCode_ == null) {
            org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init();
            typeCode_ = orb.create_alias_tc(id(), "LinkName", IstringHelper.type());
        }
        return typeCode_;
    }

    public static String id() {
        return "IDL:omg.org/CosTrading/LinkName:1.0";
    }

    public static String read(org.omg.CORBA.portable.InputStream in) {
        String _ob_v;
        _ob_v = IstringHelper.read(in);
        return _ob_v;
    }

    public static void write(org.omg.CORBA.portable.OutputStream out, String _ob_v) {
        IstringHelper.write(out, _ob_v);
    }
}
