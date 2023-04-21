package org.omg.TerminologyServices;

/**
 * Holder class for : TerminologyServiceName
 * 
 * @author OpenORB Compiler
 */
public final class TerminologyServiceNameHolder implements org.omg.CORBA.portable.Streamable {

    /**
     * Internal TerminologyServiceName value
     */
    public org.omg.TerminologyServices.TerminologyServiceName value;

    /**
     * Default constructor
     */
    public TerminologyServiceNameHolder() {
    }

    /**
     * Constructor with value initialisation
     * @param initial the initial value
     */
    public TerminologyServiceNameHolder(org.omg.TerminologyServices.TerminologyServiceName initial) {
        value = initial;
    }

    /**
     * Read TerminologyServiceName from a marshalled stream
     * @param istream the input stream
     */
    public void _read(org.omg.CORBA.portable.InputStream istream) {
        value = TerminologyServiceNameHelper.read(istream);
    }

    /**
     * Write TerminologyServiceName into a marshalled stream
     * @param ostream the output stream
     */
    public void _write(org.omg.CORBA.portable.OutputStream ostream) {
        TerminologyServiceNameHelper.write(ostream, value);
    }

    /**
     * Return the TerminologyServiceName TypeCode
     * @return a TypeCode
     */
    public org.omg.CORBA.TypeCode _type() {
        return TerminologyServiceNameHelper.type();
    }
}
