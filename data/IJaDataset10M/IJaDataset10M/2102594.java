package com.jswiff.swfrecords.actions;

/**
 * <p>
 * Tests whether a string is less than another.
 * </p>
 * 
 * <p>
 * Performed stack operations:<br>
 * <code>pop b</code><br>
 * <code>pop a</code><br>
 * <code>push [a &lt; b]</code> (1 (<code>true</code> in SWF 5 and higher) if
 * a&lt;b, otherwise 0 (<code>false</code>) )<br>
 * </p>
 * 
 * <p>
 * ActionScript equivalent: <code>lt</code> operator, <code>&lt;</code>
 * operator
 * </p>
 *
 * @since SWF 4
 */
public final class StringLess extends Action {

    /**
   * Creates a new StringLess action.
   */
    public StringLess() {
        code = ActionConstants.STRING_LESS;
    }

    /**
   * Returns a short description of this action.
   *
   * @return <code>"StringLess"</code>
   */
    public String toString() {
        return "StringLess";
    }
}
