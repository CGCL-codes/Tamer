package org.omg.DfResourceAccessDecision;

/**
 * Holder class for : BooleanList
 * 
 * @author OpenORB Compiler
 */
public final class BooleanListHolder implements org.omg.CORBA.portable.Streamable {

    /**
     * Internal BooleanList value
     */
    public boolean[] value;

    /**
     * Default constructor
     */
    public BooleanListHolder() {
    }

    /**
     * Constructor with value initialisation
     * @param initial the initial value
     */
    public BooleanListHolder(boolean[] initial) {
        value = initial;
    }

    /**
     * Read BooleanList from a marshalled stream
     * @param istream the input stream
     */
    public void _read(org.omg.CORBA.portable.InputStream istream) {
        value = BooleanListHelper.read(istream);
    }

    /**
     * Write BooleanList into a marshalled stream
     * @param ostream the output stream
     */
    public void _write(org.omg.CORBA.portable.OutputStream ostream) {
        BooleanListHelper.write(ostream, value);
    }

    /**
     * Return the BooleanList TypeCode
     * @return a TypeCode
     */
    public org.omg.CORBA.TypeCode _type() {
        return BooleanListHelper.type();
    }
}
