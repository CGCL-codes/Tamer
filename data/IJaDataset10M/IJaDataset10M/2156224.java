package org.fernwood.jbasic.funcs;

import org.fernwood.jbasic.Status;
import org.fernwood.jbasic.runtime.ArgumentList;
import org.fernwood.jbasic.runtime.JBasicException;
import org.fernwood.jbasic.runtime.SymbolTable;
import org.fernwood.jbasic.value.Value;

/**
  <b>SQRT()</b> JBasic Function
 * <p>
 * <table>
 * <tr><td><b>Description:</b></td><td>Square root.</td></tr>
 * <tr><td><b>Invocation:</b></td><td><code>d2 = SQRT( d1 )</code></td></tr>
 * <tr><td><b>Returns:</b></td><td>Double</td></tr>
 * </table>
 * <p>
 * Calculates the square root of the argument.  The value must be
 * positive or the result is a NaN (not-a-number).
 * @author cole
 *
 */
public class SqrtFunction extends JBasicFunction {

    /**
	 * Runtime execution of the function via _CALLF
	 * 
	 * @param arglist
	 *            the function argument list and count already popped from the
	 *            runtime data stack
	 * @param symbols
	 *            the currently active symbol table
	 * @return a Value containing the function result.
	 * @throws JBasicException  There was an error in the type or count of
	 * function arguments.
	 */
    public Value run(final ArgumentList arglist, final SymbolTable symbols) throws JBasicException {
        arglist.validate(1, 1, new int[] { Value.NUMBER });
        double dv = 0.0;
        try {
            dv = Math.sqrt(arglist.doubleElement(0));
        } catch (Exception e) {
            throw new JBasicException(Status.ARGTYPE, e.toString());
        }
        return new Value(dv);
    }
}
