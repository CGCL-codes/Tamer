package org.fernwood.jbasic.funcs;

import org.fernwood.jbasic.Status;
import org.fernwood.jbasic.compiler.CompileContext;
import org.fernwood.jbasic.runtime.ByteCode;
import org.fernwood.jbasic.runtime.JBasicException;
import org.fernwood.jbasic.value.Value;

/**
 * <b>NUMBER()</b> JBasic Function
 * <p>
 * <table>
 * <tr><td><b>Description:</b></td><td>Convert argument to a number</td></tr>
 * <tr><td><b>Invocation:</b></td><td><code>d = NUMBER(v)</code></td></tr>
 * <tr><td><b>Returns:</b></td><td>Double</td></tr>
 * </table>
 * <p>
 * This is a synonym for the DOUBLE function.  It converts its argument to a DOUBLE
 * number, regardless of type.  If the argument is a string, then it parses the
 * string to find the representation of a number in that string.  It is an error to
 * pass an array or record as the argument.
 * @author cole
 */
public class NumberFunction extends JBasicFunction {

    /**
	 * Compile the function
	 * @param work the compiler context, including the current byteCode
	 * stream.
	 * @return a Status value indicating if the compilation was successful.
	 * @throws JBasicException an error occurred in the number or type of
	 * function arguments.
	 */
    public Status compile(final CompileContext work) throws JBasicException {
        work.validate(1, 1);
        work.byteCode.add(ByteCode._CVT, Value.DOUBLE);
        return new Status();
    }
}
