package com.obdobion.algebrain;

import java.util.*;

public class FuncAbs extends Function {

    public FuncAbs() {
        super();
    }

    public FuncAbs(TokVariable var) {
        super(var);
    }

    @SuppressWarnings("unchecked")
    public void resolve(Stack values) throws EquException {
        if (values.size() < 1) throw new EquException("missing operands for " + toString());
        Object op1 = values.pop();
        if (op1 instanceof Double) {
            values.push(new Double(Math.abs(((Double) op1).doubleValue())));
            return;
        }
        throw new EquException("incorrect variable types for " + toString());
    }

    public String toString() {
        return "function(abs)";
    }
}
