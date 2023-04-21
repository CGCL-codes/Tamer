package org.omg.TerminologyServices;

/**
 * Holder class for : ConstraintLanguageIdSeq
 * 
 * @author OpenORB Compiler
 */
public final class ConstraintLanguageIdSeqHolder implements org.omg.CORBA.portable.Streamable {

    /**
     * Internal ConstraintLanguageIdSeq value
     */
    public String[] value;

    /**
     * Default constructor
     */
    public ConstraintLanguageIdSeqHolder() {
    }

    /**
     * Constructor with value initialisation
     * @param initial the initial value
     */
    public ConstraintLanguageIdSeqHolder(String[] initial) {
        value = initial;
    }

    /**
     * Read ConstraintLanguageIdSeq from a marshalled stream
     * @param istream the input stream
     */
    public void _read(org.omg.CORBA.portable.InputStream istream) {
        value = ConstraintLanguageIdSeqHelper.read(istream);
    }

    /**
     * Write ConstraintLanguageIdSeq into a marshalled stream
     * @param ostream the output stream
     */
    public void _write(org.omg.CORBA.portable.OutputStream ostream) {
        ConstraintLanguageIdSeqHelper.write(ostream, value);
    }

    /**
     * Return the ConstraintLanguageIdSeq TypeCode
     * @return a TypeCode
     */
    public org.omg.CORBA.TypeCode _type() {
        return ConstraintLanguageIdSeqHelper.type();
    }
}
