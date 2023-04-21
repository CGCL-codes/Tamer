package org.omg.DsObservationTimeSeries;

/**
 * Holder class for : NotImplemented
 * 
 * @author OpenORB Compiler
 */
public final class NotImplementedHolder implements org.omg.CORBA.portable.Streamable {

    /**
     * Internal NotImplemented value
     */
    public org.omg.DsObservationTimeSeries.NotImplemented value;

    /**
     * Default constructor
     */
    public NotImplementedHolder() {
    }

    /**
     * Constructor with value initialisation
     * @param initial the initial value
     */
    public NotImplementedHolder(org.omg.DsObservationTimeSeries.NotImplemented initial) {
        value = initial;
    }

    /**
     * Read NotImplemented from a marshalled stream
     * @param istream the input stream
     */
    public void _read(org.omg.CORBA.portable.InputStream istream) {
        value = NotImplementedHelper.read(istream);
    }

    /**
     * Write NotImplemented into a marshalled stream
     * @param ostream the output stream
     */
    public void _write(org.omg.CORBA.portable.OutputStream ostream) {
        NotImplementedHelper.write(ostream, value);
    }

    /**
     * Return the NotImplemented TypeCode
     * @return a TypeCode
     */
    public org.omg.CORBA.TypeCode _type() {
        return NotImplementedHelper.type();
    }
}
