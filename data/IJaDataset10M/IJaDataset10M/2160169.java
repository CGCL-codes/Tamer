package org.omg.Components;

/**
 *	Generated from IDL definition of valuetype "ConsumerDescription"
 *	@author JacORB IDL compiler 
 */
public abstract class ConsumerDescriptionHelper {

    private static org.omg.CORBA.TypeCode type = null;

    public static void insert(org.omg.CORBA.Any a, org.omg.Components.ConsumerDescription v) {
        a.insert_Value(v, v._type());
    }

    public static org.omg.Components.ConsumerDescription extract(org.omg.CORBA.Any a) {
        return (org.omg.Components.ConsumerDescription) a.extract_Value();
    }

    public static org.omg.CORBA.TypeCode type() {
        if (type == null) type = org.omg.CORBA.ORB.init().create_value_tc("IDL:omg.org/Components/ConsumerDescription:1.0", "ConsumerDescription", (short) 0, null, new org.omg.CORBA.ValueMember[] { new org.omg.CORBA.ValueMember("", "IDL:omg.org/Components/EventConsumerBase:1.0", "ConsumerDescription", "1.0", org.omg.CORBA.ORB.init().create_interface_tc("IDL:omg.org/Components/EventConsumerBase:1.0", "EventConsumerBase"), null, (short) 1) });
        return type;
    }

    public static String id() {
        return "IDL:omg.org/Components/ConsumerDescription:1.0";
    }

    public static org.omg.Components.ConsumerDescription read(org.omg.CORBA.portable.InputStream is) {
        return (org.omg.Components.ConsumerDescription) ((org.omg.CORBA_2_3.portable.InputStream) is).read_value("IDL:omg.org/Components/ConsumerDescription:1.0");
    }

    public static void write(org.omg.CORBA.portable.OutputStream os, org.omg.Components.ConsumerDescription val) {
        ((org.omg.CORBA_2_3.portable.OutputStream) os).write_value(val, "IDL:omg.org/Components/ConsumerDescription:1.0");
    }
}
