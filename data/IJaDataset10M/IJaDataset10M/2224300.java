package org.omg.DfResourceAccessDecision;

/**
 * Holder class for : NamedPolicyEvaluator
 * 
 * @author OpenORB Compiler
 */
public final class NamedPolicyEvaluatorHolder implements org.omg.CORBA.portable.Streamable {

    /**
     * Internal NamedPolicyEvaluator value
     */
    public org.omg.DfResourceAccessDecision.NamedPolicyEvaluator value;

    /**
     * Default constructor
     */
    public NamedPolicyEvaluatorHolder() {
    }

    /**
     * Constructor with value initialisation
     * @param initial the initial value
     */
    public NamedPolicyEvaluatorHolder(org.omg.DfResourceAccessDecision.NamedPolicyEvaluator initial) {
        value = initial;
    }

    /**
     * Read NamedPolicyEvaluator from a marshalled stream
     * @param istream the input stream
     */
    public void _read(org.omg.CORBA.portable.InputStream istream) {
        value = NamedPolicyEvaluatorHelper.read(istream);
    }

    /**
     * Write NamedPolicyEvaluator into a marshalled stream
     * @param ostream the output stream
     */
    public void _write(org.omg.CORBA.portable.OutputStream ostream) {
        NamedPolicyEvaluatorHelper.write(ostream, value);
    }

    /**
     * Return the NamedPolicyEvaluator TypeCode
     * @return a TypeCode
     */
    public org.omg.CORBA.TypeCode _type() {
        return NamedPolicyEvaluatorHelper.type();
    }
}
