package org.omg.CosNotifyChannelAdmin;

public abstract class SequenceProxyPullConsumerHelper {

    private static String _id = "IDL:omg.org/CosNotifyChannelAdmin/SequenceProxyPullConsumer:1.0";

    public static void insert(org.omg.CORBA.Any a, org.omg.CosNotifyChannelAdmin.SequenceProxyPullConsumer that) {
        org.omg.CORBA.portable.OutputStream out = a.create_output_stream();
        a.type(type());
        write(out, that);
        a.read_value(out.create_input_stream(), type());
    }

    public static org.omg.CosNotifyChannelAdmin.SequenceProxyPullConsumer extract(org.omg.CORBA.Any a) {
        return read(a.create_input_stream());
    }

    private static org.omg.CORBA.TypeCode __typeCode = null;

    public static synchronized org.omg.CORBA.TypeCode type() {
        if (__typeCode == null) {
            __typeCode = org.omg.CORBA.ORB.init().create_interface_tc(org.omg.CosNotifyChannelAdmin.SequenceProxyPullConsumerHelper.id(), "SequenceProxyPullConsumer");
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static org.omg.CosNotifyChannelAdmin.SequenceProxyPullConsumer read(org.omg.CORBA.portable.InputStream istream) {
        return narrow(istream.read_Object(_SequenceProxyPullConsumerStub.class));
    }

    public static void write(org.omg.CORBA.portable.OutputStream ostream, org.omg.CosNotifyChannelAdmin.SequenceProxyPullConsumer value) {
        ostream.write_Object((org.omg.CORBA.Object) value);
    }

    public static org.omg.CosNotifyChannelAdmin.SequenceProxyPullConsumer narrow(org.omg.CORBA.Object obj) {
        if (obj == null) return null; else if (obj instanceof org.omg.CosNotifyChannelAdmin.SequenceProxyPullConsumer) return (org.omg.CosNotifyChannelAdmin.SequenceProxyPullConsumer) obj; else if (!obj._is_a(id())) throw new org.omg.CORBA.BAD_PARAM(); else {
            org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl) obj)._get_delegate();
            org.omg.CosNotifyChannelAdmin._SequenceProxyPullConsumerStub stub = new org.omg.CosNotifyChannelAdmin._SequenceProxyPullConsumerStub();
            stub._set_delegate(delegate);
            return stub;
        }
    }

    public static org.omg.CosNotifyChannelAdmin.SequenceProxyPullConsumer unchecked_narrow(org.omg.CORBA.Object obj) {
        if (obj == null) return null; else if (obj instanceof org.omg.CosNotifyChannelAdmin.SequenceProxyPullConsumer) return (org.omg.CosNotifyChannelAdmin.SequenceProxyPullConsumer) obj; else {
            org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl) obj)._get_delegate();
            org.omg.CosNotifyChannelAdmin._SequenceProxyPullConsumerStub stub = new org.omg.CosNotifyChannelAdmin._SequenceProxyPullConsumerStub();
            stub._set_delegate(delegate);
            return stub;
        }
    }
}
