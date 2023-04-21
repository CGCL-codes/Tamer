package org.omg.TerminologyServices;

/**
 * Holder class for : WeightedResult
 * 
 * @author OpenORB Compiler
 */
public final class WeightedResultHolder implements org.omg.CORBA.portable.Streamable {

    /**
     * Internal WeightedResult value
     */
    public org.omg.TerminologyServices.WeightedResult value;

    /**
     * Default constructor
     */
    public WeightedResultHolder() {
    }

    /**
     * Constructor with value initialisation
     * @param initial the initial value
     */
    public WeightedResultHolder(org.omg.TerminologyServices.WeightedResult initial) {
        value = initial;
    }

    /**
     * Read WeightedResult from a marshalled stream
     * @param istream the input stream
     */
    public void _read(org.omg.CORBA.portable.InputStream istream) {
        value = WeightedResultHelper.read(istream);
    }

    /**
     * Write WeightedResult into a marshalled stream
     * @param ostream the output stream
     */
    public void _write(org.omg.CORBA.portable.OutputStream ostream) {
        WeightedResultHelper.write(ostream, value);
    }

    /**
     * Return the WeightedResult TypeCode
     * @return a TypeCode
     */
    public org.omg.CORBA.TypeCode _type() {
        return WeightedResultHelper.type();
    }
}
