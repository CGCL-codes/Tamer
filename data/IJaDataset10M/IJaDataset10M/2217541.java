package org.webgraphlab.algorithm;

/**
 *	Generated from IDL definition of interface "CCM_AlgorithmComponentHomeImplicit"
 *	@author JacORB IDL compiler 
 */
public final class CCM_AlgorithmComponentHomeImplicitHelper {

    public static void insert(final org.omg.CORBA.Any any, final org.webgraphlab.algorithm.CCM_AlgorithmComponentHomeImplicit s) {
        any.insert_Object(s);
    }

    public static org.webgraphlab.algorithm.CCM_AlgorithmComponentHomeImplicit extract(final org.omg.CORBA.Any any) {
        return narrow(any.extract_Object());
    }

    public static org.omg.CORBA.TypeCode type() {
        return org.omg.CORBA.ORB.init().create_interface_tc("IDL:org/webgraphlab/algorithm/CCM_AlgorithmComponentHomeImplicit:1.0", "CCM_AlgorithmComponentHomeImplicit");
    }

    public static String id() {
        return "IDL:org/webgraphlab/algorithm/CCM_AlgorithmComponentHomeImplicit:1.0";
    }

    public static CCM_AlgorithmComponentHomeImplicit read(final org.omg.CORBA.portable.InputStream in) {
        throw new org.omg.CORBA.MARSHAL();
    }

    public static void write(final org.omg.CORBA.portable.OutputStream _out, final org.webgraphlab.algorithm.CCM_AlgorithmComponentHomeImplicit s) {
        throw new org.omg.CORBA.MARSHAL();
    }

    public static org.webgraphlab.algorithm.CCM_AlgorithmComponentHomeImplicit narrow(final java.lang.Object obj) {
        if (obj instanceof org.webgraphlab.algorithm.CCM_AlgorithmComponentHomeImplicit) {
            return (org.webgraphlab.algorithm.CCM_AlgorithmComponentHomeImplicit) obj;
        } else if (obj instanceof org.omg.CORBA.Object) {
            return narrow((org.omg.CORBA.Object) obj);
        }
        throw new org.omg.CORBA.BAD_PARAM("Failed to narrow in helper");
    }

    public static org.webgraphlab.algorithm.CCM_AlgorithmComponentHomeImplicit narrow(final org.omg.CORBA.Object obj) {
        if (obj == null) return null;
        if (obj instanceof org.webgraphlab.algorithm.CCM_AlgorithmComponentHomeImplicit) return (org.webgraphlab.algorithm.CCM_AlgorithmComponentHomeImplicit) obj; else throw new org.omg.CORBA.BAD_PARAM("Narrow failed, not a org.webgraphlab.algorithm.CCM_AlgorithmComponentHomeImplicit");
    }

    public static org.webgraphlab.algorithm.CCM_AlgorithmComponentHomeImplicit unchecked_narrow(final org.omg.CORBA.Object obj) {
        if (obj == null) return null;
        if (obj instanceof org.webgraphlab.algorithm.CCM_AlgorithmComponentHomeImplicit) return (org.webgraphlab.algorithm.CCM_AlgorithmComponentHomeImplicit) obj; else throw new org.omg.CORBA.BAD_PARAM("unchecked_narrow failed, not a org.webgraphlab.algorithm.CCM_AlgorithmComponentHomeImplicit");
    }
}
