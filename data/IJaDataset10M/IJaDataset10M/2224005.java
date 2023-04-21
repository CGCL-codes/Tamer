package org.omg.DfResourceAccessDecision;

/**
 * Holder class for : InvalidResourceNamePattern
 * 
 * @author OpenORB Compiler
 */
public final class InvalidResourceNamePatternHolder implements org.omg.CORBA.portable.Streamable {

    /**
     * Internal InvalidResourceNamePattern value
     */
    public org.omg.DfResourceAccessDecision.InvalidResourceNamePattern value;

    /**
     * Default constructor
     */
    public InvalidResourceNamePatternHolder() {
    }

    /**
     * Constructor with value initialisation
     * @param initial the initial value
     */
    public InvalidResourceNamePatternHolder(org.omg.DfResourceAccessDecision.InvalidResourceNamePattern initial) {
        value = initial;
    }

    /**
     * Read InvalidResourceNamePattern from a marshalled stream
     * @param istream the input stream
     */
    public void _read(org.omg.CORBA.portable.InputStream istream) {
        value = InvalidResourceNamePatternHelper.read(istream);
    }

    /**
     * Write InvalidResourceNamePattern into a marshalled stream
     * @param ostream the output stream
     */
    public void _write(org.omg.CORBA.portable.OutputStream ostream) {
        InvalidResourceNamePatternHelper.write(ostream, value);
    }

    /**
     * Return the InvalidResourceNamePattern TypeCode
     * @return a TypeCode
     */
    public org.omg.CORBA.TypeCode _type() {
        return InvalidResourceNamePatternHelper.type();
    }
}
