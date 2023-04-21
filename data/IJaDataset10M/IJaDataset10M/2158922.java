package org.jostraca.tree.path.expr;

import org.jostraca.tree.Node;

public class BooleanExpr extends FunctionExpr {

    private boolean mValue;

    public BooleanExpr(boolean pValue) {
        super();
        mValue = pValue;
    }

    @Override
    public Object eval(Node pNode) {
        return mValue;
    }

    @Override
    protected String functionName() {
        return String.valueOf(mValue);
    }
}
