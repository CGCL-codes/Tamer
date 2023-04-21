package org.omg.CosTrading.LinkPackage;

public final class LinkInfoHelper {

    public static void insert(org.omg.CORBA.Any any, LinkInfo _ob_v) {
        org.omg.CORBA.portable.OutputStream out = any.create_output_stream();
        write(out, _ob_v);
        any.read_value(out.create_input_stream(), type());
    }

    public static LinkInfo extract(org.omg.CORBA.Any any) {
        if (any.type().equal(type())) return read(any.create_input_stream()); else throw new org.omg.CORBA.BAD_OPERATION();
    }

    private static org.omg.CORBA.TypeCode typeCode_;

    public static org.omg.CORBA.TypeCode type() {
        if (typeCode_ == null) {
            org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init();
            org.omg.CORBA.StructMember[] members = new org.omg.CORBA.StructMember[4];
            members[0] = new org.omg.CORBA.StructMember();
            members[0].name = "target";
            members[0].type = org.omg.CosTrading.LookupHelper.type();
            members[1] = new org.omg.CORBA.StructMember();
            members[1].name = "target_reg";
            members[1].type = org.omg.CosTrading.RegisterHelper.type();
            members[2] = new org.omg.CORBA.StructMember();
            members[2].name = "def_pass_on_follow_rule";
            members[2].type = org.omg.CosTrading.FollowOptionHelper.type();
            members[3] = new org.omg.CORBA.StructMember();
            members[3].name = "limiting_follow_rule";
            members[3].type = org.omg.CosTrading.FollowOptionHelper.type();
            typeCode_ = orb.create_struct_tc(id(), "LinkInfo", members);
        }
        return typeCode_;
    }

    public static String id() {
        return "IDL:omg.org/CosTrading/Link/LinkInfo:1.0";
    }

    public static LinkInfo read(org.omg.CORBA.portable.InputStream in) {
        LinkInfo _ob_v = new LinkInfo();
        _ob_v.target = org.omg.CosTrading.LookupHelper.read(in);
        _ob_v.target_reg = org.omg.CosTrading.RegisterHelper.read(in);
        _ob_v.def_pass_on_follow_rule = org.omg.CosTrading.FollowOptionHelper.read(in);
        _ob_v.limiting_follow_rule = org.omg.CosTrading.FollowOptionHelper.read(in);
        return _ob_v;
    }

    public static void write(org.omg.CORBA.portable.OutputStream out, LinkInfo _ob_v) {
        org.omg.CosTrading.LookupHelper.write(out, _ob_v.target);
        org.omg.CosTrading.RegisterHelper.write(out, _ob_v.target_reg);
        org.omg.CosTrading.FollowOptionHelper.write(out, _ob_v.def_pass_on_follow_rule);
        org.omg.CosTrading.FollowOptionHelper.write(out, _ob_v.limiting_follow_rule);
    }
}
