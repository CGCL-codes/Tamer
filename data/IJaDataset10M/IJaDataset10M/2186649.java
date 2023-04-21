package net.sf.opendf.cal.i2.util;

import net.sf.opendf.cal.i2.Evaluator;
import net.sf.opendf.cal.i2.Function;

public abstract class FunctionOf0 implements Function {

    public void apply(int n, Evaluator evaluator) {
        assert n == 0;
        evaluator.replaceWithResult(n, f());
    }

    public abstract Object f();
}
