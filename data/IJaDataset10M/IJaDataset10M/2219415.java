package clump.language.belang.expression.impl;

import clump.language.belang.expression.IExpression;

public class GetAttribute implements IExpression {

    private final IExpression object;

    private final String name;

    public GetAttribute(IExpression object, String name) {
        this.object = object;
        this.name = name;
    }

    public IExpression getObject() {
        return object;
    }

    public String getName() {
        return name;
    }
}
