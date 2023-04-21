package org.omg.CosTypedEventChannelAdmin;

public final class TypedProxyPullSupplierHelper {

    public static void insert(org.omg.CORBA.Any any, TypedProxyPullSupplier _ob_v) {
        org.omg.CORBA.portable.OutputStream out = any.create_output_stream();
        write(out, _ob_v);
        any.read_value(out.create_input_stream(), type());
    }

    public static TypedProxyPullSupplier extract(org.omg.CORBA.Any any) {
        if (any.type().equal(type())) return read(any.create_input_stream()); else throw new org.omg.CORBA.BAD_OPERATION();
    }

    private static org.omg.CORBA.TypeCode typeCode_;

    public static org.omg.CORBA.TypeCode type() {
        if (typeCode_ == null) {
            org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init();
            typeCode_ = orb.create_interface_tc(id(), "TypedProxyPullSupplier");
        }
        return typeCode_;
    }

    public static String id() {
        return "IDL:omg.org/CosTypedEventChannelAdmin/TypedProxyPullSupplier:1.0";
    }

    public static TypedProxyPullSupplier read(org.omg.CORBA.portable.InputStream in) {
        org.omg.CORBA.Object _ob_v = in.read_Object();
        if (_ob_v == null) return null;
        try {
            return (TypedProxyPullSupplier) _ob_v;
        } catch (ClassCastException ex) {
        }
        org.omg.CORBA.portable.ObjectImpl _ob_impl;
        _ob_impl = (org.omg.CORBA.portable.ObjectImpl) _ob_v;
        _TypedProxyPullSupplierStub _ob_stub = new _TypedProxyPullSupplierStub();
        _ob_stub._set_delegate(_ob_impl._get_delegate());
        return _ob_stub;
    }

    public static void write(org.omg.CORBA.portable.OutputStream out, TypedProxyPullSupplier _ob_v) {
        out.write_Object(_ob_v);
    }

    public static TypedProxyPullSupplier narrow(org.omg.CORBA.Object _ob_v) {
        if (_ob_v != null) {
            try {
                return (TypedProxyPullSupplier) _ob_v;
            } catch (ClassCastException ex) {
            }
            if (_ob_v._is_a(id())) {
                org.omg.CORBA.portable.ObjectImpl _ob_impl;
                _TypedProxyPullSupplierStub _ob_stub = new _TypedProxyPullSupplierStub();
                _ob_impl = (org.omg.CORBA.portable.ObjectImpl) _ob_v;
                _ob_stub._set_delegate(_ob_impl._get_delegate());
                return _ob_stub;
            }
            throw new org.omg.CORBA.BAD_PARAM();
        }
        return null;
    }
}
