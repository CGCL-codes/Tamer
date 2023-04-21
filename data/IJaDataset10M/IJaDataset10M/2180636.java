package org.omg.TerminologyServices;

/** 
 * Helper class for : SystemizationAccess
 *  
 * @author OpenORB Compiler
 */
public class SystemizationAccessHelper {

    /**
     * Insert SystemizationAccess into an any
     * @param a an any
     * @param t SystemizationAccess value
     */
    public static void insert(org.omg.CORBA.Any a, org.omg.TerminologyServices.SystemizationAccess t) {
        a.insert_Object(t, type());
    }

    /**
     * Extract SystemizationAccess from an any
     *
     * @param a an any
     * @return the extracted SystemizationAccess value
     */
    public static org.omg.TerminologyServices.SystemizationAccess extract(org.omg.CORBA.Any a) {
        if (!a.type().equivalent(type())) {
            throw new org.omg.CORBA.MARSHAL();
        }
        try {
            return org.omg.TerminologyServices.SystemizationAccessHelper.narrow(a.extract_Object());
        } catch (final org.omg.CORBA.BAD_PARAM e) {
            throw new org.omg.CORBA.MARSHAL(e.getMessage());
        }
    }

    private static org.omg.CORBA.TypeCode _tc = null;

    /**
     * Return the SystemizationAccess TypeCode
     * @return a TypeCode
     */
    public static org.omg.CORBA.TypeCode type() {
        if (_tc == null) {
            org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init();
            _tc = orb.create_interface_tc(id(), "SystemizationAccess");
        }
        return _tc;
    }

    /**
     * Return the SystemizationAccess IDL ID
     * @return an ID
     */
    public static String id() {
        return _id;
    }

    private static final String _id = "IDL:omg.org/TerminologyServices/SystemizationAccess:1.0";

    /**
     * Read SystemizationAccess from a marshalled stream
     * @param istream the input stream
     * @return the readed SystemizationAccess value
     */
    public static org.omg.TerminologyServices.SystemizationAccess read(org.omg.CORBA.portable.InputStream istream) {
        return (org.omg.TerminologyServices.SystemizationAccess) istream.read_Object(org.omg.TerminologyServices._SystemizationAccessStub.class);
    }

    /**
     * Write SystemizationAccess into a marshalled stream
     * @param ostream the output stream
     * @param value SystemizationAccess value
     */
    public static void write(org.omg.CORBA.portable.OutputStream ostream, org.omg.TerminologyServices.SystemizationAccess value) {
        ostream.write_Object((org.omg.CORBA.portable.ObjectImpl) value);
    }

    /**
     * Narrow CORBA::Object to SystemizationAccess
     * @param obj the CORBA Object
     * @return SystemizationAccess Object
     */
    public static SystemizationAccess narrow(org.omg.CORBA.Object obj) {
        if (obj == null) return null;
        if (obj instanceof SystemizationAccess) return (SystemizationAccess) obj;
        if (obj._is_a(id())) {
            _SystemizationAccessStub stub = new _SystemizationAccessStub();
            stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl) obj)._get_delegate());
            return stub;
        }
        throw new org.omg.CORBA.BAD_PARAM();
    }

    /**
     * Unchecked Narrow CORBA::Object to SystemizationAccess
     * @param obj the CORBA Object
     * @return SystemizationAccess Object
     */
    public static SystemizationAccess unchecked_narrow(org.omg.CORBA.Object obj) {
        if (obj == null) return null;
        if (obj instanceof SystemizationAccess) return (SystemizationAccess) obj;
        _SystemizationAccessStub stub = new _SystemizationAccessStub();
        stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl) obj)._get_delegate());
        return stub;
    }
}
