package com.jswiff.swfrecords.actions;

/**
 * <p>
 * Tests whether a string is greater than another.
 * </p>
 * 
 * <p>
 * Performed stack operations:<br>
 * <code>pop b</code><br>
 * <code>pop a</code><br>
 * <code>push [a &gt; b]</code> (1 (<code>true</code> in SWF 5 and higher) if
 * a&gt;b, otherwise 0 (<code>false</code>) )<br>
 * </p>
 * 
 * <p>
 * ActionScript equivalent: <code>&gt;</code> operator
 * </p>
 *
 * @since SWF 6
 */
public final class StringGreater extends Action {

    /**
   * Creates a new StringGreater action.
   */
    public StringGreater() {
        code = ActionConstants.STRING_GREATER;
    }

    /**
   * Returns a short description of this action.
   *
   * @return <code>"StringGreater"</code>
   */
    public String toString() {
        return "StringGreater";
    }
}
