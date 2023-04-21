package org.omg.TerminologyServices;

/**
 * Exception definition: PolicyTypeMismatch.
 * 
 * @author OpenORB Compiler
 */
public final class PolicyTypeMismatch extends org.omg.CORBA.UserException {

    /**
     * Exception member bad_policy
     */
    public org.omg.TerminologyServices.Policy bad_policy;

    /**
     * Default constructor
     */
    public PolicyTypeMismatch() {
        super(PolicyTypeMismatchHelper.id());
    }

    /**
     * Constructor with fields initialization
     * @param bad_policy bad_policy exception member
     */
    public PolicyTypeMismatch(org.omg.TerminologyServices.Policy bad_policy) {
        super(PolicyTypeMismatchHelper.id());
        this.bad_policy = bad_policy;
    }

    /**
     * Full constructor with fields initialization
     * @param bad_policy bad_policy exception member
     */
    public PolicyTypeMismatch(String orb_reason, org.omg.TerminologyServices.Policy bad_policy) {
        super(PolicyTypeMismatchHelper.id() + " " + orb_reason);
        this.bad_policy = bad_policy;
    }
}
