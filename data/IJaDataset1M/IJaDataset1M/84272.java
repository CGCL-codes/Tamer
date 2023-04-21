package pspdash.data.compiler;

import pspdash.data.SimpleData;
import java.util.List;

public class Iff extends AbstractFunction {

    /** Perform a procedure call.
     *
     * This method <b>must</b> be thread-safe.
     */
    public Object call(List arguments, ExpressionContext context) {
        SimpleData test = getArg(arguments, 0);
        SimpleData t = getArg(arguments, 1);
        SimpleData f = getArg(arguments, 2);
        if (test != null && test.test()) return t; else return f;
    }
}
