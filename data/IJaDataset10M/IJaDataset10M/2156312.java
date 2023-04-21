package org.omg.DsObservationTimeSeries;

/**
 * Holder class for : ValueSeqType
 * 
 * @author OpenORB Compiler
 */
public final class ValueSeqTypeHolder implements org.omg.CORBA.portable.Streamable {

    /**
     * Internal ValueSeqType value
     */
    public org.omg.DsObservationTimeSeries.ValueSeqType value;

    /**
     * Default constructor
     */
    public ValueSeqTypeHolder() {
    }

    /**
     * Constructor with value initialisation
     * @param initial the initial value
     */
    public ValueSeqTypeHolder(org.omg.DsObservationTimeSeries.ValueSeqType initial) {
        value = initial;
    }

    /**
     * Read ValueSeqType from a marshalled stream
     * @param istream the input stream
     */
    public void _read(org.omg.CORBA.portable.InputStream istream) {
        value = ValueSeqTypeHelper.read(istream);
    }

    /**
     * Write ValueSeqType into a marshalled stream
     * @param ostream the output stream
     */
    public void _write(org.omg.CORBA.portable.OutputStream ostream) {
        ValueSeqTypeHelper.write(ostream, value);
    }

    /**
     * Return the ValueSeqType TypeCode
     * @return a TypeCode
     */
    public org.omg.CORBA.TypeCode _type() {
        return ValueSeqTypeHelper.type();
    }
}
