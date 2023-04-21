package org.fudaa.dodico.corba.mathematiques;

public abstract class IMathematiquesHelper {

    private static String _id = "IDL:mathematiques/IMathematiques:1.0";

    public static void insert(org.omg.CORBA.Any a, org.fudaa.dodico.corba.mathematiques.IMathematiques that) {
        org.omg.CORBA.portable.OutputStream out = a.create_output_stream();
        a.type(type());
        write(out, that);
        a.read_value(out.create_input_stream(), type());
    }

    public static org.fudaa.dodico.corba.mathematiques.IMathematiques extract(org.omg.CORBA.Any a) {
        return read(a.create_input_stream());
    }

    private static org.omg.CORBA.TypeCode __typeCode = null;

    public static synchronized org.omg.CORBA.TypeCode type() {
        if (__typeCode == null) {
            __typeCode = org.omg.CORBA.ORB.init().create_interface_tc(org.fudaa.dodico.corba.mathematiques.IMathematiquesHelper.id(), "IMathematiques");
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static org.fudaa.dodico.corba.mathematiques.IMathematiques read(org.omg.CORBA.portable.InputStream istream) {
        return narrow(istream.read_Object(_IMathematiquesStub.class));
    }

    public static void write(org.omg.CORBA.portable.OutputStream ostream, org.fudaa.dodico.corba.mathematiques.IMathematiques value) {
        ostream.write_Object((org.omg.CORBA.Object) value);
    }

    public static org.fudaa.dodico.corba.mathematiques.IMathematiques narrow(org.omg.CORBA.Object obj) {
        if (obj == null) return null; else if (obj instanceof org.fudaa.dodico.corba.mathematiques.IMathematiques) return (org.fudaa.dodico.corba.mathematiques.IMathematiques) obj; else if (!obj._is_a(id())) throw new org.omg.CORBA.BAD_PARAM(); else {
            org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl) obj)._get_delegate();
            org.fudaa.dodico.corba.mathematiques._IMathematiquesStub stub = new org.fudaa.dodico.corba.mathematiques._IMathematiquesStub();
            stub._set_delegate(delegate);
            return stub;
        }
    }

    public static org.fudaa.dodico.corba.mathematiques.IMathematiques unchecked_narrow(org.omg.CORBA.Object obj) {
        if (obj == null) return null; else if (obj instanceof org.fudaa.dodico.corba.mathematiques.IMathematiques) return (org.fudaa.dodico.corba.mathematiques.IMathematiques) obj; else {
            org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl) obj)._get_delegate();
            org.fudaa.dodico.corba.mathematiques._IMathematiquesStub stub = new org.fudaa.dodico.corba.mathematiques._IMathematiquesStub();
            stub._set_delegate(delegate);
            return stub;
        }
    }
}
