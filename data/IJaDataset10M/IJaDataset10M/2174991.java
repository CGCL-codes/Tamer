package org.omg.TerminologyServices;

/**
 * Holder class for : AssociatableElement
 * 
 * @author OpenORB Compiler
 */
public final class AssociatableElementHolder implements org.omg.CORBA.portable.Streamable {

    /**
     * Internal AssociatableElement value
     */
    public org.omg.TerminologyServices.AssociatableElement value;

    /**
     * Default constructor
     */
    public AssociatableElementHolder() {
    }

    /**
     * Constructor with value initialisation
     * @param initial the initial value
     */
    public AssociatableElementHolder(org.omg.TerminologyServices.AssociatableElement initial) {
        value = initial;
    }

    /**
     * Read AssociatableElement from a marshalled stream
     * @param istream the input stream
     */
    public void _read(org.omg.CORBA.portable.InputStream istream) {
        value = AssociatableElementHelper.read(istream);
    }

    /**
     * Write AssociatableElement into a marshalled stream
     * @param ostream the output stream
     */
    public void _write(org.omg.CORBA.portable.OutputStream ostream) {
        AssociatableElementHelper.write(ostream, value);
    }

    /**
     * Return the AssociatableElement TypeCode
     * @return a TypeCode
     */
    public org.omg.CORBA.TypeCode _type() {
        return AssociatableElementHelper.type();
    }
}
