package org.omg.TerminologyServices;

/**
 * Holder class for : QualifiedCode
 * 
 * @author OpenORB Compiler
 */
public final class QualifiedCodeHolder implements org.omg.CORBA.portable.Streamable {

    /**
     * Internal QualifiedCode value
     */
    public org.omg.TerminologyServices.QualifiedCode value;

    /**
     * Default constructor
     */
    public QualifiedCodeHolder() {
    }

    /**
     * Constructor with value initialisation
     * @param initial the initial value
     */
    public QualifiedCodeHolder(org.omg.TerminologyServices.QualifiedCode initial) {
        value = initial;
    }

    /**
     * Read QualifiedCode from a marshalled stream
     * @param istream the input stream
     */
    public void _read(org.omg.CORBA.portable.InputStream istream) {
        value = QualifiedCodeHelper.read(istream);
    }

    /**
     * Write QualifiedCode into a marshalled stream
     * @param ostream the output stream
     */
    public void _write(org.omg.CORBA.portable.OutputStream ostream) {
        QualifiedCodeHelper.write(ostream, value);
    }

    /**
     * Return the QualifiedCode TypeCode
     * @return a TypeCode
     */
    public org.omg.CORBA.TypeCode _type() {
        return QualifiedCodeHelper.type();
    }
}
