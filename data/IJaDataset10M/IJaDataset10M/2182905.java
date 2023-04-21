package org.fernwood.jbasic.funcs;

import org.fernwood.jbasic.Status;
import org.fernwood.jbasic.compiler.CompileContext;
import org.fernwood.jbasic.runtime.ArgumentList;
import org.fernwood.jbasic.runtime.JBasicException;
import org.fernwood.jbasic.runtime.SymbolTable;
import org.fernwood.jbasic.value.Value;

/**
 * <b>ARRAY()</b> JBasic Function
 * <p>
 * <table>
 * <tr><td><b>Description:</b></td><td>Convert to array</td></tr>
 * <tr><td><b>Invocation:</b></td><td><code>a = ARRAY( v1 [, v2...] )</code></td></tr>
 * <tr><td><b>Returns:</b></td><td>Array</td></tr>
 * </table>
 * <p>
 * Collects all the arguments and stores them in an array value, which is the
 * result of the function.  This is semantically equivalent to using an array
 * constant [v1, v2, v3] to define an array.  There can be any number of parameters.
 * <p>
 * Note that this function will "flatten" all arguments, so if one or more parameters
 * are arrays or contain arrays, they will all be stores as individual elements 
 * in the single array that is the result.
 * @author cole
 */
public class ArrayFunction extends JBasicFunction {

    public Status compile(CompileContext work) throws JBasicException {
        if (work.constantArguments) {
            Value[] r = getArguments(work);
            Value n = new Value(Value.ARRAY, null);
            for (Value element : r) n.addElement(element);
            this.deleteArguments(work);
            work.byteCode.add(n);
            return new Status();
        }
        return new Status(Status.UNKFUNC);
    }

    /**
	 * Runtime execution of the function via _CALLF
	 * 
	 * @param arglist
	 *            the function argument list and count already popped from the
	 *            runtime data stack
	 * @param symbols
	 *            the currently active symbol table
	 * @return a Value containing the function result.
	 * @throws JBasicException if a parameter count or type error occurs
	 */
    public Value run(final ArgumentList arglist, final SymbolTable symbols) throws JBasicException {
        int j;
        if (arglist.size() < 1) throw new JBasicException(Status.INSFARGS);
        final Value list = new Value(Value.ARRAY, null);
        for (j = 0; j < arglist.size(); j++) {
            Value v = arglist.element(j);
            list.addElement(v);
        }
        return list;
    }
}
