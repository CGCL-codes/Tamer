package org.omg.TerminologyServices;

/**
 * Exception definition: UnableToEvaluate.
 * 
 * @author OpenORB Compiler
 */
public final class UnableToEvaluate extends org.omg.CORBA.UserException {

    /**
     * Exception member the_expression
     */
    public org.omg.TerminologyServices.ConceptExpressionElement[] the_expression;

    /**
     * Default constructor
     */
    public UnableToEvaluate() {
        super(UnableToEvaluateHelper.id());
    }

    /**
     * Constructor with fields initialization
     * @param the_expression the_expression exception member
     */
    public UnableToEvaluate(org.omg.TerminologyServices.ConceptExpressionElement[] the_expression) {
        super(UnableToEvaluateHelper.id());
        this.the_expression = the_expression;
    }

    /**
     * Full constructor with fields initialization
     * @param the_expression the_expression exception member
     */
    public UnableToEvaluate(String orb_reason, org.omg.TerminologyServices.ConceptExpressionElement[] the_expression) {
        super(UnableToEvaluateHelper.id() + " " + orb_reason);
        this.the_expression = the_expression;
    }
}
