package org.omg.TerminologyServices;

/**
 * Holder class for : WeightedResultsIter
 * 
 * @author OpenORB Compiler
 */
public final class WeightedResultsIterHolder implements org.omg.CORBA.portable.Streamable {

    /**
     * Internal WeightedResultsIter value
     */
    public org.omg.TerminologyServices.WeightedResultsIter value;

    /**
     * Default constructor
     */
    public WeightedResultsIterHolder() {
    }

    /**
     * Constructor with value initialisation
     * @param initial the initial value
     */
    public WeightedResultsIterHolder(org.omg.TerminologyServices.WeightedResultsIter initial) {
        value = initial;
    }

    /**
     * Read WeightedResultsIter from a marshalled stream
     * @param istream the input stream
     */
    public void _read(org.omg.CORBA.portable.InputStream istream) {
        value = WeightedResultsIterHelper.read(istream);
    }

    /**
     * Write WeightedResultsIter into a marshalled stream
     * @param ostream the output stream
     */
    public void _write(org.omg.CORBA.portable.OutputStream ostream) {
        WeightedResultsIterHelper.write(ostream, value);
    }

    /**
     * Return the WeightedResultsIter TypeCode
     * @return a TypeCode
     */
    public org.omg.CORBA.TypeCode _type() {
        return WeightedResultsIterHelper.type();
    }
}
