package org.asam.ods;

/**
 *	Generated from IDL interface "SMatLink"
 *	@author JacORB IDL compiler V 2.2.3, 10-Dec-2005
 */
public final class SMatLinkHelper {

    public static void insert(final org.omg.CORBA.Any any, final org.asam.ods.SMatLink s) {
        any.insert_Object(s);
    }

    public static org.asam.ods.SMatLink extract(final org.omg.CORBA.Any any) {
        return narrow(any.extract_Object());
    }

    public static org.omg.CORBA.TypeCode type() {
        return org.omg.CORBA.ORB.init().create_interface_tc("IDL:org/asam/ods/SMatLink:1.0", "SMatLink");
    }

    public static String id() {
        return "IDL:org/asam/ods/SMatLink:1.0";
    }

    public static SMatLink read(final org.omg.CORBA.portable.InputStream in) {
        return narrow(in.read_Object());
    }

    public static void write(final org.omg.CORBA.portable.OutputStream _out, final org.asam.ods.SMatLink s) {
        _out.write_Object(s);
    }

    public static org.asam.ods.SMatLink narrow(final java.lang.Object obj) {
        if (obj instanceof org.asam.ods.SMatLink) {
            return (org.asam.ods.SMatLink) obj;
        } else if (obj instanceof org.omg.CORBA.Object) {
            return narrow((org.omg.CORBA.Object) obj);
        }
        throw new org.omg.CORBA.BAD_PARAM("Failed to narrow in helper");
    }

    public static org.asam.ods.SMatLink narrow(final org.omg.CORBA.Object obj) {
        if (obj == null) return null;
        try {
            return (org.asam.ods.SMatLink) obj;
        } catch (ClassCastException c) {
            if (obj._is_a("IDL:org/asam/ods/SMatLink:1.0")) {
                org.asam.ods._SMatLinkStub stub;
                stub = new org.asam.ods._SMatLinkStub();
                stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl) obj)._get_delegate());
                return stub;
            }
        }
        throw new org.omg.CORBA.BAD_PARAM("Narrow failed");
    }

    public static org.asam.ods.SMatLink unchecked_narrow(final org.omg.CORBA.Object obj) {
        if (obj == null) return null;
        try {
            return (org.asam.ods.SMatLink) obj;
        } catch (ClassCastException c) {
            org.asam.ods._SMatLinkStub stub;
            stub = new org.asam.ods._SMatLinkStub();
            stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl) obj)._get_delegate());
            return stub;
        }
    }
}
