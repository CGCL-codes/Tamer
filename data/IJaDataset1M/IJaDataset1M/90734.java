package org.fudaa.dodico.corba.sinavi3;

public abstract class VSParametresCoupleLoiJournaliere2Helper {

    private static String _id = "IDL:sinavi3/VSParametresCoupleLoiJournaliere2:1.0";

    public static void insert(org.omg.CORBA.Any a, org.fudaa.dodico.corba.sinavi3.SParametresCoupleLoiJournaliere2[] that) {
        org.omg.CORBA.portable.OutputStream out = a.create_output_stream();
        a.type(type());
        write(out, that);
        a.read_value(out.create_input_stream(), type());
    }

    public static org.fudaa.dodico.corba.sinavi3.SParametresCoupleLoiJournaliere2[] extract(org.omg.CORBA.Any a) {
        return read(a.create_input_stream());
    }

    private static org.omg.CORBA.TypeCode __typeCode = null;

    public static synchronized org.omg.CORBA.TypeCode type() {
        if (__typeCode == null) {
            __typeCode = org.fudaa.dodico.corba.sinavi3.SParametresCoupleLoiJournaliere2Helper.type();
            __typeCode = org.omg.CORBA.ORB.init().create_sequence_tc(0, __typeCode);
            __typeCode = org.omg.CORBA.ORB.init().create_alias_tc(org.fudaa.dodico.corba.sinavi3.VSParametresCoupleLoiJournaliere2Helper.id(), "VSParametresCoupleLoiJournaliere2", __typeCode);
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static org.fudaa.dodico.corba.sinavi3.SParametresCoupleLoiJournaliere2[] read(org.omg.CORBA.portable.InputStream istream) {
        org.fudaa.dodico.corba.sinavi3.SParametresCoupleLoiJournaliere2 value[] = null;
        int _len0 = istream.read_long();
        value = new org.fudaa.dodico.corba.sinavi3.SParametresCoupleLoiJournaliere2[_len0];
        for (int _o1 = 0; _o1 < value.length; ++_o1) value[_o1] = org.fudaa.dodico.corba.sinavi3.SParametresCoupleLoiJournaliere2Helper.read(istream);
        return value;
    }

    public static void write(org.omg.CORBA.portable.OutputStream ostream, org.fudaa.dodico.corba.sinavi3.SParametresCoupleLoiJournaliere2[] value) {
        ostream.write_long(value.length);
        for (int _i0 = 0; _i0 < value.length; ++_i0) org.fudaa.dodico.corba.sinavi3.SParametresCoupleLoiJournaliere2Helper.write(ostream, value[_i0]);
    }
}
