package org.omg.DsObservationPolicies;

/** 
 * Helper class for : ReturnMaxSequenceForValueIteratorsPolicyType
 *  
 * @author OpenORB Compiler
 */
public class ReturnMaxSequenceForValueIteratorsPolicyTypeHelper {

    /**
     * Insert ReturnMaxSequenceForValueIteratorsPolicyType into an any
     * @param a an any
     * @param t ReturnMaxSequenceForValueIteratorsPolicyType value
     */
    public static void insert(org.omg.CORBA.Any a, int t) {
        a.type(type());
        write(a.create_output_stream(), t);
    }

    /**
     * Extract ReturnMaxSequenceForValueIteratorsPolicyType from an any
     *
     * @param a an any
     * @return the extracted ReturnMaxSequenceForValueIteratorsPolicyType value
     */
    public static int extract(org.omg.CORBA.Any a) {
        if (!a.type().equivalent(type())) {
            throw new org.omg.CORBA.MARSHAL();
        }
        return read(a.create_input_stream());
    }

    private static org.omg.CORBA.TypeCode _tc = null;

    /**
     * Return the ReturnMaxSequenceForValueIteratorsPolicyType TypeCode
     * @return a TypeCode
     */
    public static org.omg.CORBA.TypeCode type() {
        if (_tc == null) {
            org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init();
            _tc = orb.create_alias_tc(id(), "ReturnMaxSequenceForValueIteratorsPolicyType", orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_ulong));
        }
        return _tc;
    }

    /**
     * Return the ReturnMaxSequenceForValueIteratorsPolicyType IDL ID
     * @return an ID
     */
    public static String id() {
        return _id;
    }

    private static final String _id = "IDL:omg.org/DsObservationPolicies/ReturnMaxSequenceForValueIteratorsPolicyType:1.0";

    /**
     * Read ReturnMaxSequenceForValueIteratorsPolicyType from a marshalled stream
     * @param istream the input stream
     * @return the readed ReturnMaxSequenceForValueIteratorsPolicyType value
     */
    public static int read(org.omg.CORBA.portable.InputStream istream) {
        int new_one;
        new_one = istream.read_ulong();
        return new_one;
    }

    /**
     * Write ReturnMaxSequenceForValueIteratorsPolicyType into a marshalled stream
     * @param ostream the output stream
     * @param value ReturnMaxSequenceForValueIteratorsPolicyType value
     */
    public static void write(org.omg.CORBA.portable.OutputStream ostream, int value) {
        ostream.write_ulong(value);
    }
}
