package org.omg.DsObservationAccess;

/** 
 * Helper class for : CompositeObservationRemote
 *  
 * @author OpenORB Compiler
 */
public class CompositeObservationRemoteHelper {

    /**
     * Insert CompositeObservationRemote into an any
     * @param a an any
     * @param t CompositeObservationRemote value
     */
    public static void insert(org.omg.CORBA.Any a, org.omg.DsObservationAccess.CompositeObservationRemote t) {
        a.insert_Object(t, type());
    }

    /**
     * Extract CompositeObservationRemote from an any
     *
     * @param a an any
     * @return the extracted CompositeObservationRemote value
     */
    public static org.omg.DsObservationAccess.CompositeObservationRemote extract(org.omg.CORBA.Any a) {
        if (!a.type().equivalent(type())) {
            throw new org.omg.CORBA.MARSHAL();
        }
        try {
            return org.omg.DsObservationAccess.CompositeObservationRemoteHelper.narrow(a.extract_Object());
        } catch (final org.omg.CORBA.BAD_PARAM e) {
            throw new org.omg.CORBA.MARSHAL(e.getMessage());
        }
    }

    private static org.omg.CORBA.TypeCode _tc = null;

    /**
     * Return the CompositeObservationRemote TypeCode
     * @return a TypeCode
     */
    public static org.omg.CORBA.TypeCode type() {
        if (_tc == null) {
            org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init();
            _tc = orb.create_interface_tc(id(), "CompositeObservationRemote");
        }
        return _tc;
    }

    /**
     * Return the CompositeObservationRemote IDL ID
     * @return an ID
     */
    public static String id() {
        return _id;
    }

    private static final String _id = "IDL:omg.org/DsObservationAccess/CompositeObservationRemote:1.0";

    /**
     * Read CompositeObservationRemote from a marshalled stream
     * @param istream the input stream
     * @return the readed CompositeObservationRemote value
     */
    public static org.omg.DsObservationAccess.CompositeObservationRemote read(org.omg.CORBA.portable.InputStream istream) {
        return (org.omg.DsObservationAccess.CompositeObservationRemote) istream.read_Object(org.omg.DsObservationAccess._CompositeObservationRemoteStub.class);
    }

    /**
     * Write CompositeObservationRemote into a marshalled stream
     * @param ostream the output stream
     * @param value CompositeObservationRemote value
     */
    public static void write(org.omg.CORBA.portable.OutputStream ostream, org.omg.DsObservationAccess.CompositeObservationRemote value) {
        ostream.write_Object((org.omg.CORBA.portable.ObjectImpl) value);
    }

    /**
     * Narrow CORBA::Object to CompositeObservationRemote
     * @param obj the CORBA Object
     * @return CompositeObservationRemote Object
     */
    public static CompositeObservationRemote narrow(org.omg.CORBA.Object obj) {
        if (obj == null) return null;
        if (obj instanceof CompositeObservationRemote) return (CompositeObservationRemote) obj;
        if (obj._is_a(id())) {
            _CompositeObservationRemoteStub stub = new _CompositeObservationRemoteStub();
            stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl) obj)._get_delegate());
            return stub;
        }
        throw new org.omg.CORBA.BAD_PARAM();
    }

    /**
     * Unchecked Narrow CORBA::Object to CompositeObservationRemote
     * @param obj the CORBA Object
     * @return CompositeObservationRemote Object
     */
    public static CompositeObservationRemote unchecked_narrow(org.omg.CORBA.Object obj) {
        if (obj == null) return null;
        if (obj instanceof CompositeObservationRemote) return (CompositeObservationRemote) obj;
        _CompositeObservationRemoteStub stub = new _CompositeObservationRemoteStub();
        stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl) obj)._get_delegate());
        return stub;
    }
}
