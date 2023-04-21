package org.omg.DsObservationTimeSeries;

/**
 * Struct definition: TimeSeriesRemoteAttributes.
 * 
 * @author OpenORB Compiler
*/
public final class TimeSeriesRemoteAttributes implements org.omg.CORBA.portable.IDLEntity {

    /**
     * Struct member code
     */
    public String code;

    /**
     * Struct member units
     */
    public String units;

    /**
     * Struct member accuracy
     */
    public String[] accuracy;

    /**
     * Struct member precision
     */
    public float[] precision;

    /**
     * Struct member corner_frequency
     */
    public float[] corner_frequency;

    /**
     * Struct member highest_frequency
     */
    public float[] highest_frequency;

    /**
     * Struct member time_span
     */
    public org.omg.DsObservationAccess.TimeSpan time_span;

    /**
     * Struct member time_delta
     */
    public org.omg.DsObservationTimeSeries.TimeDelta time_delta;

    /**
     * Struct member total_size
     */
    public long total_size;

    /**
     * Struct member supported_filters
     */
    public String[] supported_filters;

    /**
     * Struct member supported_policies
     */
    public org.omg.DsObservationAccess.NameValuePair[] supported_policies;

    /**
     * Default constructor
     */
    public TimeSeriesRemoteAttributes() {
    }

    /**
     * Constructor with fields initialization
     * @param code code struct member
     * @param units units struct member
     * @param accuracy accuracy struct member
     * @param precision precision struct member
     * @param corner_frequency corner_frequency struct member
     * @param highest_frequency highest_frequency struct member
     * @param time_span time_span struct member
     * @param time_delta time_delta struct member
     * @param total_size total_size struct member
     * @param supported_filters supported_filters struct member
     * @param supported_policies supported_policies struct member
     */
    public TimeSeriesRemoteAttributes(String code, String units, String[] accuracy, float[] precision, float[] corner_frequency, float[] highest_frequency, org.omg.DsObservationAccess.TimeSpan time_span, org.omg.DsObservationTimeSeries.TimeDelta time_delta, long total_size, String[] supported_filters, org.omg.DsObservationAccess.NameValuePair[] supported_policies) {
        this.code = code;
        this.units = units;
        this.accuracy = accuracy;
        this.precision = precision;
        this.corner_frequency = corner_frequency;
        this.highest_frequency = highest_frequency;
        this.time_span = time_span;
        this.time_delta = time_delta;
        this.total_size = total_size;
        this.supported_filters = supported_filters;
        this.supported_policies = supported_policies;
    }
}
