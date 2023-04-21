package com.jswiff.swfrecords.actions;

/**
 * <p>
 * Causes the execution to immediately return to the calling function.
 * </p>
 * 
 * <p>
 * Performed stack operations: none
 * </p>
 * 
 * <p>
 * Stack precondition:  the function result must be pushed to stack prior to
 * this action's invocation, as <code>CallMethod</code> and
 * <code>CallFunction</code> implicitly pop the function result off the stack.
 * If the function has no result, use <code>undefined</code> as result.
 * </p>
 * 
 * <p>
 * ActionScript equivalent: <code>return</code> statement
 * </p>
 *
 * @since SWF 5
 */
public final class Return extends Action {

    /**
   * Creates a new Return action.
   */
    public Return() {
        code = ActionConstants.RETURN;
    }

    /**
   * Returns a short description of this action.
   *
   * @return <code>"Return"</code>
   */
    public String toString() {
        return "Return";
    }
}
