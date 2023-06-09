package org.fudaa.dodico.corba.sipor;

/**
* org/fudaa/dodico/corba/sipor/SParametresResultatOccupationCategHelper.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from C:/devel/fudaa/fudaa_devel/dodico/idl/code/sipor.idl
* mercredi 6 ao�t 2008 22 h 27 CEST
*/
public abstract class SParametresResultatOccupationCategHelper {

    private static String _id = "IDL:sipor/SParametresResultatOccupationCateg:1.0";

    public static void insert(org.omg.CORBA.Any a, org.fudaa.dodico.corba.sipor.SParametresResultatOccupationCateg that) {
        org.omg.CORBA.portable.OutputStream out = a.create_output_stream();
        a.type(type());
        write(out, that);
        a.read_value(out.create_input_stream(), type());
    }

    public static org.fudaa.dodico.corba.sipor.SParametresResultatOccupationCateg extract(org.omg.CORBA.Any a) {
        return read(a.create_input_stream());
    }

    private static org.omg.CORBA.TypeCode __typeCode = null;

    private static boolean __active = false;

    public static synchronized org.omg.CORBA.TypeCode type() {
        if (__typeCode == null) {
            synchronized (org.omg.CORBA.TypeCode.class) {
                if (__typeCode == null) {
                    if (__active) {
                        return org.omg.CORBA.ORB.init().create_recursive_tc(_id);
                    }
                    __active = true;
                    org.omg.CORBA.StructMember[] _members0 = new org.omg.CORBA.StructMember[7];
                    org.omg.CORBA.TypeCode _tcOf_members0 = null;
                    _tcOf_members0 = org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.tk_long);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_alias_tc(org.fudaa.dodico.corba.base.EntierHelper.id(), "Entier", _tcOf_members0);
                    _members0[0] = new org.omg.CORBA.StructMember("nbNaviresUtiliseQuai", _tcOf_members0, null);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.tk_double);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_alias_tc(org.fudaa.dodico.corba.base.ReelHelper.id(), "Reel", _tcOf_members0);
                    _members0[1] = new org.omg.CORBA.StructMember("tonnage", _tcOf_members0, null);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.tk_double);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_alias_tc(org.fudaa.dodico.corba.base.ReelHelper.id(), "Reel", _tcOf_members0);
                    _members0[2] = new org.omg.CORBA.StructMember("tempsServiceTotalAuQuai", _tcOf_members0, null);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.tk_double);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_alias_tc(org.fudaa.dodico.corba.base.ReelHelper.id(), "Reel", _tcOf_members0);
                    _members0[3] = new org.omg.CORBA.StructMember("tempsServiceMiniAuQuai", _tcOf_members0, null);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.tk_double);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_alias_tc(org.fudaa.dodico.corba.base.ReelHelper.id(), "Reel", _tcOf_members0);
                    _members0[4] = new org.omg.CORBA.StructMember("tempsServiceMaxiAuQuai", _tcOf_members0, null);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.tk_double);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_alias_tc(org.fudaa.dodico.corba.base.ReelHelper.id(), "Reel", _tcOf_members0);
                    _members0[5] = new org.omg.CORBA.StructMember("tonnageMin", _tcOf_members0, null);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.tk_double);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_alias_tc(org.fudaa.dodico.corba.base.ReelHelper.id(), "Reel", _tcOf_members0);
                    _members0[6] = new org.omg.CORBA.StructMember("tonnageMax", _tcOf_members0, null);
                    __typeCode = org.omg.CORBA.ORB.init().create_struct_tc(org.fudaa.dodico.corba.sipor.SParametresResultatOccupationCategHelper.id(), "SParametresResultatOccupationCateg", _members0);
                    __active = false;
                }
            }
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static org.fudaa.dodico.corba.sipor.SParametresResultatOccupationCateg read(org.omg.CORBA.portable.InputStream istream) {
        org.fudaa.dodico.corba.sipor.SParametresResultatOccupationCateg value = new org.fudaa.dodico.corba.sipor.SParametresResultatOccupationCateg();
        value.nbNaviresUtiliseQuai = istream.read_long();
        value.tonnage = istream.read_double();
        value.tempsServiceTotalAuQuai = istream.read_double();
        value.tempsServiceMiniAuQuai = istream.read_double();
        value.tempsServiceMaxiAuQuai = istream.read_double();
        value.tonnageMin = istream.read_double();
        value.tonnageMax = istream.read_double();
        return value;
    }

    public static void write(org.omg.CORBA.portable.OutputStream ostream, org.fudaa.dodico.corba.sipor.SParametresResultatOccupationCateg value) {
        ostream.write_long(value.nbNaviresUtiliseQuai);
        ostream.write_double(value.tonnage);
        ostream.write_double(value.tempsServiceTotalAuQuai);
        ostream.write_double(value.tempsServiceMiniAuQuai);
        ostream.write_double(value.tempsServiceMaxiAuQuai);
        ostream.write_double(value.tonnageMin);
        ostream.write_double(value.tonnageMax);
    }
}
