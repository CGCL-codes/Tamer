package com.mangobop.impl.consequence.atomics;

import com.mangobop.functions.Function;
import com.mangobop.types.Type;

/**
 * 
 * @author Stefan Meyer
 */
public class AtomicValueConsequence implements AtomicConsequence {

    /** Holds value of property target. */
    private Function target;

    /** Holds value of property parameter. */
    private ValueParameter parameter;

    /** Creates a new instance of AtomicMapConsequence */
    public AtomicValueConsequence() {
    }

    public void perform() {
        Type t = parameter.getOperand().getReturnType();
        if (t instanceof mangobop.types.lang.Integer) {
            parameter.getOperand().setValue(parameter.getValue(), parameter.getTarget());
        }
        if (t instanceof mangobop.types.lang.Text) {
            parameter.getOperand().setValue(parameter.getValue(), getTarget());
        }
    }

    /**
     * Getter for property target.
     * 
     * @return Value of property target.
     *  
     */
    public Function getTarget() {
        return this.target;
    }

    /**
     * Setter for property target.
     * 
     * @param target
     *            New value of property target.
     *  
     */
    public void setTarget(Function target) {
        this.target = target;
    }

    /**
     * Getter for property parameter.
     * 
     * @return Value of property parameter.
     *  
     */
    public Parameter getParameter() {
        return this.parameter;
    }

    /**
     * Setter for property parameter.
     * 
     * @param parameter
     *            New value of property parameter.
     *  
     */
    public void setParameter(Parameter parameter) {
        if (!(parameter instanceof ValueParameter)) throw new IllegalArgumentException();
        this.parameter = (ValueParameter) parameter;
    }
}
