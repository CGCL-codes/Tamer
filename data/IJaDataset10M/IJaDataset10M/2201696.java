package org.omg.CosNotifyChannelAdmin;

/**
* org/omg/CosNotifyChannelAdmin/ConsumerAdminHelper.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from CosNotifyChannelAdmin.idl
* Friday, December 17, 2004 1:02:17 PM EST
*/
public abstract class ConsumerAdminHelper {

    private static String _id = "IDL:omg.org/CosNotifyChannelAdmin/ConsumerAdmin:1.0";

    public static void insert(org.omg.CORBA.Any a, org.omg.CosNotifyChannelAdmin.ConsumerAdmin that) {
        org.omg.CORBA.portable.OutputStream out = a.create_output_stream();
        a.type(type());
        write(out, that);
        a.read_value(out.create_input_stream(), type());
    }

    public static org.omg.CosNotifyChannelAdmin.ConsumerAdmin extract(org.omg.CORBA.Any a) {
        return read(a.create_input_stream());
    }

    private static org.omg.CORBA.TypeCode __typeCode = null;

    public static synchronized org.omg.CORBA.TypeCode type() {
        if (__typeCode == null) {
            __typeCode = org.omg.CORBA.ORB.init().create_interface_tc(org.omg.CosNotifyChannelAdmin.ConsumerAdminHelper.id(), "ConsumerAdmin");
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static org.omg.CosNotifyChannelAdmin.ConsumerAdmin read(org.omg.CORBA.portable.InputStream istream) {
        return narrow(istream.read_Object(_ConsumerAdminStub.class));
    }

    public static void write(org.omg.CORBA.portable.OutputStream ostream, org.omg.CosNotifyChannelAdmin.ConsumerAdmin value) {
        ostream.write_Object((org.omg.CORBA.Object) value);
    }

    public static org.omg.CosNotifyChannelAdmin.ConsumerAdmin narrow(org.omg.CORBA.Object obj) {
        if (obj == null) return null; else if (obj instanceof org.omg.CosNotifyChannelAdmin.ConsumerAdmin) return (org.omg.CosNotifyChannelAdmin.ConsumerAdmin) obj; else if (!obj._is_a(id())) throw new org.omg.CORBA.BAD_PARAM(); else {
            org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl) obj)._get_delegate();
            org.omg.CosNotifyChannelAdmin._ConsumerAdminStub stub = new org.omg.CosNotifyChannelAdmin._ConsumerAdminStub();
            stub._set_delegate(delegate);
            return stub;
        }
    }

    public static org.omg.CosNotifyChannelAdmin.ConsumerAdmin unchecked_narrow(org.omg.CORBA.Object obj) {
        if (obj == null) return null; else if (obj instanceof org.omg.CosNotifyChannelAdmin.ConsumerAdmin) return (org.omg.CosNotifyChannelAdmin.ConsumerAdmin) obj; else {
            org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl) obj)._get_delegate();
            org.omg.CosNotifyChannelAdmin._ConsumerAdminStub stub = new org.omg.CosNotifyChannelAdmin._ConsumerAdminStub();
            stub._set_delegate(delegate);
            return stub;
        }
    }
}