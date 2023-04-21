package org.omg.PersonIdService;

/**
 * Holder class for : IndexSeq
 * 
 * @author OpenORB Compiler
 */
public final class IndexSeqHolder implements org.omg.CORBA.portable.Streamable {

    /**
     * Internal IndexSeq value
     */
    public int[] value;

    /**
     * Default constructor
     */
    public IndexSeqHolder() {
    }

    /**
     * Constructor with value initialisation
     * @param initial the initial value
     */
    public IndexSeqHolder(int[] initial) {
        value = initial;
    }

    /**
     * Read IndexSeq from a marshalled stream
     * @param istream the input stream
     */
    public void _read(org.omg.CORBA.portable.InputStream istream) {
        value = IndexSeqHelper.read(istream);
    }

    /**
     * Write IndexSeq into a marshalled stream
     * @param ostream the output stream
     */
    public void _write(org.omg.CORBA.portable.OutputStream ostream) {
        IndexSeqHelper.write(ostream, value);
    }

    /**
     * Return the IndexSeq TypeCode
     * @return a TypeCode
     */
    public org.omg.CORBA.TypeCode _type() {
        return IndexSeqHelper.type();
    }
}
