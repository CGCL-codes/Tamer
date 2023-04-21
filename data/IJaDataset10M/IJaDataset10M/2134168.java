package com.agilejava.bignumbers.stack;

import java.util.Stack;

/**
 * A StackOp that represents pushing the value held by a variable onto the
 * stack.
 * 
 * @author Wilfred Springer
 */
public class PushVariableOp implements StackOp {

    /**
   * The variable holding the value to be pushed to the stack.
   */
    private String variable;

    /**
   * Constructs a new instance.
   * 
   * @param variable
   *          The variable holding the value to be pushed to the stack.
   */
    public PushVariableOp(String variable) {
        this.variable = variable;
    }

    /**
   * Returns the variable containing the value to be pushed onto the stack.
   * 
   * @return The variable containing the value to be pushed onto the stack.
   */
    public String getVariable() {
        return variable;
    }

    public void apply(BigDecimalStackOps stack) {
        stack.push(variable);
    }

    public String getType() {
        return "pushVariable";
    }
}
