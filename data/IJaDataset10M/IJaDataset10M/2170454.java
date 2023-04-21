package org.omg.HL7ProviderVersion2_3;

public final class PatientRelationHelper {

    public static void insert(org.omg.CORBA.Any any, PatientRelation _ob_v) {
        org.omg.CORBA.portable.OutputStream out = any.create_output_stream();
        write(out, _ob_v);
        any.read_value(out.create_input_stream(), type());
    }

    public static PatientRelation extract(org.omg.CORBA.Any any) {
        if (any.type().equal(type())) return read(any.create_input_stream()); else throw new org.omg.CORBA.BAD_OPERATION();
    }

    private static org.omg.CORBA.TypeCode typeCode_;

    public static org.omg.CORBA.TypeCode type() {
        if (typeCode_ == null) {
            org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init();
            org.omg.CORBA.StructMember[] members = new org.omg.CORBA.StructMember[4];
            members[0] = new org.omg.CORBA.StructMember();
            members[0].name = "PersonId";
            members[0].type = org.omg.PersonIdService.QualifiedPersonIdHelper.type();
            members[1] = new org.omg.CORBA.StructMember();
            members[1].name = "role";
            members[1].type = orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_string);
            members[2] = new org.omg.CORBA.StructMember();
            members[2].name = "start_date";
            members[2].type = orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_string);
            members[3] = new org.omg.CORBA.StructMember();
            members[3].name = "stop_date";
            members[3].type = orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_string);
            typeCode_ = orb.create_struct_tc(id(), "PatientRelation", members);
        }
        return typeCode_;
    }

    public static String id() {
        return "IDL:lanl.gov/HL7ProviderVersion2_3/PatientRelation:1.0";
    }

    public static PatientRelation read(org.omg.CORBA.portable.InputStream in) {
        PatientRelation _ob_v = new PatientRelation();
        _ob_v.PersonId = org.omg.PersonIdService.QualifiedPersonIdHelper.read(in);
        _ob_v.role = in.read_string();
        _ob_v.start_date = in.read_string();
        _ob_v.stop_date = in.read_string();
        return _ob_v;
    }

    public static void write(org.omg.CORBA.portable.OutputStream out, PatientRelation _ob_v) {
        org.omg.PersonIdService.QualifiedPersonIdHelper.write(out, _ob_v.PersonId);
        out.write_string(_ob_v.role);
        out.write_string(_ob_v.start_date);
        out.write_string(_ob_v.stop_date);
    }
}
