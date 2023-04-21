package org.omg.DsObservationPolicies;

/** 
 * Helper class for : TimeSeriesRemoteReturnTypePreferencePolicyType
 *  
 * @author OpenORB Compiler
 */
public class TimeSeriesRemoteReturnTypePreferencePolicyTypeHelper {

    /**
     * Insert TimeSeriesRemoteReturnTypePreferencePolicyType into an any
     * @param a an any
     * @param t TimeSeriesRemoteReturnTypePreferencePolicyType value
     */
    public static void insert(org.omg.CORBA.Any a, org.omg.DsObservationTimeSeries.ValueSeqType t) {
        a.type(type());
        write(a.create_output_stream(), t);
    }

    /**
     * Extract TimeSeriesRemoteReturnTypePreferencePolicyType from an any
     *
     * @param a an any
     * @return the extracted TimeSeriesRemoteReturnTypePreferencePolicyType value
     */
    public static org.omg.DsObservationTimeSeries.ValueSeqType extract(org.omg.CORBA.Any a) {
        if (!a.type().equivalent(type())) {
            throw new org.omg.CORBA.MARSHAL();
        }
        return read(a.create_input_stream());
    }

    private static org.omg.CORBA.TypeCode _tc = null;

    /**
     * Return the TimeSeriesRemoteReturnTypePreferencePolicyType TypeCode
     * @return a TypeCode
     */
    public static org.omg.CORBA.TypeCode type() {
        if (_tc == null) {
            org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init();
            _tc = orb.create_alias_tc(id(), "TimeSeriesRemoteReturnTypePreferencePolicyType", org.omg.DsObservationTimeSeries.ValueSeqTypeHelper.type());
        }
        return _tc;
    }

    /**
     * Return the TimeSeriesRemoteReturnTypePreferencePolicyType IDL ID
     * @return an ID
     */
    public static String id() {
        return _id;
    }

    private static final String _id = "IDL:omg.org/DsObservationPolicies/TimeSeriesRemoteReturnTypePreferencePolicyType:1.0";

    /**
     * Read TimeSeriesRemoteReturnTypePreferencePolicyType from a marshalled stream
     * @param istream the input stream
     * @return the readed TimeSeriesRemoteReturnTypePreferencePolicyType value
     */
    public static org.omg.DsObservationTimeSeries.ValueSeqType read(org.omg.CORBA.portable.InputStream istream) {
        return org.omg.DsObservationTimeSeries.ValueSeqTypeHelper.read(istream);
    }

    /**
     * Write TimeSeriesRemoteReturnTypePreferencePolicyType into a marshalled stream
     * @param ostream the output stream
     * @param value TimeSeriesRemoteReturnTypePreferencePolicyType value
     */
    public static void write(org.omg.CORBA.portable.OutputStream ostream, org.omg.DsObservationTimeSeries.ValueSeqType value) {
        org.omg.DsObservationTimeSeries.ValueSeqTypeHelper.write(ostream, value);
    }
}
