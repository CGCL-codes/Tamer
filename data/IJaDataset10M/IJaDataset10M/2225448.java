package org.omg.DsObservationAccess;

/**
 * Exception definition: InvalidPolicies.
 * 
 * @author OpenORB Compiler
 */
public final class InvalidPolicies extends org.omg.CORBA.UserException {

    /**
     * Exception member policies
     */
    public String[] policies;

    /**
     * Default constructor
     */
    public InvalidPolicies() {
        super(InvalidPoliciesHelper.id());
    }

    /**
     * Constructor with fields initialization
     * @param policies policies exception member
     */
    public InvalidPolicies(String[] policies) {
        super(InvalidPoliciesHelper.id());
        this.policies = policies;
    }

    /**
     * Full constructor with fields initialization
     * @param policies policies exception member
     */
    public InvalidPolicies(String orb_reason, String[] policies) {
        super(InvalidPoliciesHelper.id() + " " + orb_reason);
        this.policies = policies;
    }
}
