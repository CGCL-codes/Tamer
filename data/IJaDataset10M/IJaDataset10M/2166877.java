package org.omg.DfResourceAccessDecision;

/**
 * Exception definition: RadComponentError.
 * 
 * @author OpenORB Compiler
 */
public final class RadComponentError extends org.omg.CORBA.UserException {

    /**
     * Exception member ed
     */
    public org.omg.DfResourceAccessDecision.ExceptionData ed;

    /**
     * Exception member it
     */
    public org.omg.DfResourceAccessDecision.InternalErrorType it;

    /**
     * Default constructor
     */
    public RadComponentError() {
        super(RadComponentErrorHelper.id());
    }

    /**
     * Constructor with fields initialization
     * @param ed ed exception member
     * @param it it exception member
     */
    public RadComponentError(org.omg.DfResourceAccessDecision.ExceptionData ed, org.omg.DfResourceAccessDecision.InternalErrorType it) {
        super(RadComponentErrorHelper.id());
        this.ed = ed;
        this.it = it;
    }

    /**
     * Full constructor with fields initialization
     * @param ed ed exception member
     * @param it it exception member
     */
    public RadComponentError(String orb_reason, org.omg.DfResourceAccessDecision.ExceptionData ed, org.omg.DfResourceAccessDecision.InternalErrorType it) {
        super(RadComponentErrorHelper.id() + " " + orb_reason);
        this.ed = ed;
        this.it = it;
    }
}
