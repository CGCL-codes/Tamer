package net.sf.opendf.cal.i2.util;

import net.sf.opendf.cal.i2.Evaluator;
import net.sf.opendf.cal.i2.Function;

public abstract class FunctionOf3Eval implements Function {

    public void apply(int n, Evaluator evaluator) {
        assert n == 3;
        evaluator.replaceWithResult(n, f(evaluator.getValue(0), evaluator.getValue(1), evaluator.getValue(2), evaluator));
    }

    public abstract Object f(Object a, Object b, Object c, Evaluator evaluator);
}
