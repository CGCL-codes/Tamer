package org.omg.DfResourceAccessDecision;

/**
 * Holder class for : AccessDecisionAdmin
 * 
 * @author OpenORB Compiler
 */
public final class AccessDecisionAdminHolder implements org.omg.CORBA.portable.Streamable {

    /**
     * Internal AccessDecisionAdmin value
     */
    public org.omg.DfResourceAccessDecision.AccessDecisionAdmin value;

    /**
     * Default constructor
     */
    public AccessDecisionAdminHolder() {
    }

    /**
     * Constructor with value initialisation
     * @param initial the initial value
     */
    public AccessDecisionAdminHolder(org.omg.DfResourceAccessDecision.AccessDecisionAdmin initial) {
        value = initial;
    }

    /**
     * Read AccessDecisionAdmin from a marshalled stream
     * @param istream the input stream
     */
    public void _read(org.omg.CORBA.portable.InputStream istream) {
        value = AccessDecisionAdminHelper.read(istream);
    }

    /**
     * Write AccessDecisionAdmin into a marshalled stream
     * @param ostream the output stream
     */
    public void _write(org.omg.CORBA.portable.OutputStream ostream) {
        AccessDecisionAdminHelper.write(ostream, value);
    }

    /**
     * Return the AccessDecisionAdmin TypeCode
     * @return a TypeCode
     */
    public org.omg.CORBA.TypeCode _type() {
        return AccessDecisionAdminHelper.type();
    }
}
