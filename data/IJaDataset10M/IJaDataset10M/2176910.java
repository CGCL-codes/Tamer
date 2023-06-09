package org.fudaa.dodico.corba.mascaret;

/**
* org/fudaa/dodico/corba/mascaret/SParametresResultHelper.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from C:/devel/fudaa/fudaa_devel/dodico/idl/code/mascaret.idl
* mercredi 14 janvier 2009 02 h 05 CET
*/
public abstract class SParametresResultHelper {

    private static String _id = "IDL:mascaret/SParametresResult:1.0";

    public static void insert(org.omg.CORBA.Any a, org.fudaa.dodico.corba.mascaret.SParametresResult that) {
        org.omg.CORBA.portable.OutputStream out = a.create_output_stream();
        a.type(type());
        write(out, that);
        a.read_value(out.create_input_stream(), type());
    }

    public static org.fudaa.dodico.corba.mascaret.SParametresResult extract(org.omg.CORBA.Any a) {
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
                    org.omg.CORBA.StructMember[] _members0 = new org.omg.CORBA.StructMember[4];
                    org.omg.CORBA.TypeCode _tcOf_members0 = null;
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_string_tc(0);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_alias_tc(org.fudaa.dodico.corba.base.ChaineHelper.id(), "Chaine", _tcOf_members0);
                    _members0[0] = new org.omg.CORBA.StructMember("fichResultat", _tcOf_members0, null);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_string_tc(0);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_alias_tc(org.fudaa.dodico.corba.base.ChaineHelper.id(), "Chaine", _tcOf_members0);
                    _members0[1] = new org.omg.CORBA.StructMember("fichResultat2", _tcOf_members0, null);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_string_tc(0);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_alias_tc(org.fudaa.dodico.corba.base.ChaineHelper.id(), "Chaine", _tcOf_members0);
                    _members0[2] = new org.omg.CORBA.StructMember("formatBinFich", _tcOf_members0, null);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.tk_long);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_alias_tc(org.fudaa.dodico.corba.base.EntierHelper.id(), "Entier", _tcOf_members0);
                    _members0[3] = new org.omg.CORBA.StructMember("postProcesseur", _tcOf_members0, null);
                    __typeCode = org.omg.CORBA.ORB.init().create_struct_tc(org.fudaa.dodico.corba.mascaret.SParametresResultHelper.id(), "SParametresResult", _members0);
                    __active = false;
                }
            }
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static org.fudaa.dodico.corba.mascaret.SParametresResult read(org.omg.CORBA.portable.InputStream istream) {
        org.fudaa.dodico.corba.mascaret.SParametresResult value = new org.fudaa.dodico.corba.mascaret.SParametresResult();
        value.fichResultat = istream.read_string();
        value.fichResultat2 = istream.read_string();
        value.formatBinFich = istream.read_string();
        value.postProcesseur = istream.read_long();
        return value;
    }

    public static void write(org.omg.CORBA.portable.OutputStream ostream, org.fudaa.dodico.corba.mascaret.SParametresResult value) {
        ostream.write_string(value.fichResultat);
        ostream.write_string(value.fichResultat2);
        ostream.write_string(value.formatBinFich);
        ostream.write_long(value.postProcesseur);
    }
}
