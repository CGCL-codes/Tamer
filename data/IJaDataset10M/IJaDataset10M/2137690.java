package org.omg.DfResourceAccessDecision;

/**
 * Holder class for : PolicyEvaluatorLocatorBasicAdmin
 * 
 * @author OpenORB Compiler
 */
public final class PolicyEvaluatorLocatorBasicAdminHolder implements org.omg.CORBA.portable.Streamable {

    /**
     * Internal PolicyEvaluatorLocatorBasicAdmin value
     */
    public org.omg.DfResourceAccessDecision.PolicyEvaluatorLocatorBasicAdmin value;

    /**
     * Default constructor
     */
    public PolicyEvaluatorLocatorBasicAdminHolder() {
    }

    /**
     * Constructor with value initialisation
     * @param initial the initial value
     */
    public PolicyEvaluatorLocatorBasicAdminHolder(org.omg.DfResourceAccessDecision.PolicyEvaluatorLocatorBasicAdmin initial) {
        value = initial;
    }

    /**
     * Read PolicyEvaluatorLocatorBasicAdmin from a marshalled stream
     * @param istream the input stream
     */
    public void _read(org.omg.CORBA.portable.InputStream istream) {
        value = PolicyEvaluatorLocatorBasicAdminHelper.read(istream);
    }

    /**
     * Write PolicyEvaluatorLocatorBasicAdmin into a marshalled stream
     * @param ostream the output stream
     */
    public void _write(org.omg.CORBA.portable.OutputStream ostream) {
        PolicyEvaluatorLocatorBasicAdminHelper.write(ostream, value);
    }

    /**
     * Return the PolicyEvaluatorLocatorBasicAdmin TypeCode
     * @return a TypeCode
     */
    public org.omg.CORBA.TypeCode _type() {
        return PolicyEvaluatorLocatorBasicAdminHelper.type();
    }
}
