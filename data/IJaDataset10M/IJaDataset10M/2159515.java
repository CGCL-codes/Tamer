package org.freeform.func;

import org.freeform.exp.FreeFormException;

public class TracedFunction implements PureFunction, OptimizableFunction {

    private Function f;

    private Object trace;

    public TracedFunction(Function f, Object trace) {
        this.f = f;
        this.trace = trace;
    }

    public Object call(Object... args) {
        try {
            return f.call(args);
        } catch (FreeFormException e) {
            throw new FreeFormException(trace, e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FreeFormException(trace, e);
        }
    }

    public String toString() {
        return f.toString();
    }

    public Function optimize() {
        if (f instanceof OptimizableFunction) {
            return ((OptimizableFunction) f).optimize();
        }
        return f;
    }
}
