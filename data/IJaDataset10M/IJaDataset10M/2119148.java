package com.jswiff.swfrecords.actions;

/**
 * <p>
 * Creates a new object, invoking a constructor. You will likely use
 * <code>NewObject</code> instead.
 * </p>
 * 
 * <p>
 * Performed stack operations:<br>
 * <code>pop constrName</code> (name of the constructor function)<br>
 * <code>pop class</code> (class to be instantiated)<br>
 * <code>pop n</code> (number of parameters passed to constructor)<br>
 * <code>pop param1</code> (1st parameter)<br>
 * <code>pop param2</code> (2nd parameter)<br>
 * <code>...</code><br>
 * <code>pop paramn</code> (n-th parameter)<br>
 * <code>push obj</code> (the newly constructed object)<br>
 * </p>
 * 
 * <p>
 * ActionScript equivalents: unknown
 * </p>
 *
 * @since SWF 5
 */
public final class NewMethod extends Action {

    /**
   * Creates a new NewMethod action.
   */
    public NewMethod() {
        code = ActionConstants.NEW_METHOD;
    }

    /**
   * Returns a short description of this action.
   *
   * @return <code>"NewMethod"</code>
   */
    public String toString() {
        return "NewMethod";
    }
}
