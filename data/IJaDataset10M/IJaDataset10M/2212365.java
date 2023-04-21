package org.asam.ods;

/**
 *	Generated from IDL interface "SecurityLevel"
 *	@author JacORB IDL compiler V 2.2.3, 10-Dec-2005
 */
public final class SecurityLevelHelper {

    public static void insert(final org.omg.CORBA.Any any, final org.asam.ods.SecurityLevel s) {
        any.insert_Object(s);
    }

    public static org.asam.ods.SecurityLevel extract(final org.omg.CORBA.Any any) {
        return narrow(any.extract_Object());
    }

    public static org.omg.CORBA.TypeCode type() {
        return org.omg.CORBA.ORB.init().create_interface_tc("IDL:org/asam/ods/SecurityLevel:1.0", "SecurityLevel");
    }

    public static String id() {
        return "IDL:org/asam/ods/SecurityLevel:1.0";
    }

    public static SecurityLevel read(final org.omg.CORBA.portable.InputStream in) {
        return narrow(in.read_Object());
    }

    public static void write(final org.omg.CORBA.portable.OutputStream _out, final org.asam.ods.SecurityLevel s) {
        _out.write_Object(s);
    }

    public static org.asam.ods.SecurityLevel narrow(final java.lang.Object obj) {
        if (obj instanceof org.asam.ods.SecurityLevel) {
            return (org.asam.ods.SecurityLevel) obj;
        } else if (obj instanceof org.omg.CORBA.Object) {
            return narrow((org.omg.CORBA.Object) obj);
        }
        throw new org.omg.CORBA.BAD_PARAM("Failed to narrow in helper");
    }

    public static org.asam.ods.SecurityLevel narrow(final org.omg.CORBA.Object obj) {
        if (obj == null) return null;
        try {
            return (org.asam.ods.SecurityLevel) obj;
        } catch (ClassCastException c) {
            if (obj._is_a("IDL:org/asam/ods/SecurityLevel:1.0")) {
                org.asam.ods._SecurityLevelStub stub;
                stub = new org.asam.ods._SecurityLevelStub();
                stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl) obj)._get_delegate());
                return stub;
            }
        }
        throw new org.omg.CORBA.BAD_PARAM("Narrow failed");
    }

    public static org.asam.ods.SecurityLevel unchecked_narrow(final org.omg.CORBA.Object obj) {
        if (obj == null) return null;
        try {
            return (org.asam.ods.SecurityLevel) obj;
        } catch (ClassCastException c) {
            org.asam.ods._SecurityLevelStub stub;
            stub = new org.asam.ods._SecurityLevelStub();
            stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl) obj)._get_delegate());
            return stub;
        }
    }
}
