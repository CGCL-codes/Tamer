package org.omg.TerminologyServices;

/**
 * Holder class for : UnknownCodingScheme
 * 
 * @author OpenORB Compiler
 */
public final class UnknownCodingSchemeHolder implements org.omg.CORBA.portable.Streamable {

    /**
     * Internal UnknownCodingScheme value
     */
    public org.omg.TerminologyServices.UnknownCodingScheme value;

    /**
     * Default constructor
     */
    public UnknownCodingSchemeHolder() {
    }

    /**
     * Constructor with value initialisation
     * @param initial the initial value
     */
    public UnknownCodingSchemeHolder(org.omg.TerminologyServices.UnknownCodingScheme initial) {
        value = initial;
    }

    /**
     * Read UnknownCodingScheme from a marshalled stream
     * @param istream the input stream
     */
    public void _read(org.omg.CORBA.portable.InputStream istream) {
        value = UnknownCodingSchemeHelper.read(istream);
    }

    /**
     * Write UnknownCodingScheme into a marshalled stream
     * @param ostream the output stream
     */
    public void _write(org.omg.CORBA.portable.OutputStream ostream) {
        UnknownCodingSchemeHelper.write(ostream, value);
    }

    /**
     * Return the UnknownCodingScheme TypeCode
     * @return a TypeCode
     */
    public org.omg.CORBA.TypeCode _type() {
        return UnknownCodingSchemeHelper.type();
    }
}
