package com.rapidminer.operator;

/**
 * Will be thrown if an operator chain has the wrong number of inner operators.
 * Should be thrown during validation.
 * 
 * @author Ingo Mierswa, Simon Fischer
 *          15:35:42 ingomierswa Exp $
 */
public class IllegalNumberOfInnerOperatorsException extends Exception {

    private static final long serialVersionUID = 8042272058326397126L;

    private transient OperatorChain operatorChain;

    public IllegalNumberOfInnerOperatorsException(String message, OperatorChain operatorChain) {
        super(message);
        this.operatorChain = operatorChain;
    }

    public OperatorChain getOperatorChain() {
        return operatorChain;
    }
}
