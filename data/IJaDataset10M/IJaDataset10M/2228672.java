package com.mangobop.impl.functions.types;

import com.mangobop.types.*;

/**
 *
 * @author Stefan Meyer
 */
public abstract class StateInitOperand extends com.mangobop.impl.types.SimpleAbstractOperand implements Operand {

    /** Holds value of property constant. */
    private boolean constant;

    /** Holds value of property returnType. */
    private FunctionType returnType;

    /** Holds value of property type. */
    private FunctionType type;

    /** Holds value of property readOnly. */
    private boolean readOnly;

    /** Creates a new instance of StateInitOperand */
    public StateInitOperand() {
    }

    public String getName() {
        return "init";
    }

    /** Getter for property constant.
     * @return Value of property constant.
     *
     */
    public boolean isConstant() {
        return this.constant;
    }

    /** Setter for property constant.
     * @param constant New value of property constant.
     *
     */
    public void setConstant(boolean constant) {
        this.constant = constant;
    }

    /** Getter for property returnType.
     * @return Value of property returnType.
     *
     */
    public Type getReturnType() {
        return this.returnType;
    }

    /** Setter for property returnType.
     * @param returnType New value of property returnType.
     *
     */
    public void setReturnType(FunctionType returnType) {
        this.returnType = returnType;
    }

    /** Getter for property type.
     * @return Value of property type.
     *
     */
    public Type getType() {
        return this.type;
    }

    /** Setter for property type.
     * @param type New value of property type.
     *
     */
    public void setType(FunctionType type) {
        this.type = type;
    }

    /** Getter for property readOnly.
     * @return Value of property readOnly.
     *
     */
    public boolean isReadOnly() {
        return this.readOnly;
    }

    /** Setter for property readOnly.
     * @param readOnly New value of property readOnly.
     *
     */
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }
}
