package org.sodeja.il.model;

import org.sodeja.il.runtime.Context;
import org.sodeja.il.sdk.ILObject;
import org.sodeja.il.sdk.ILSymbol;

public class VariableExpression implements Expression {

    public final ILSymbol name;

    public VariableExpression(String name) {
        this.name = new ILSymbol(name);
    }

    @Override
    public ILObject eval(Context ctx) {
        return ctx.get(name);
    }

    @Override
    public String toString() {
        return name.toString();
    }
}
