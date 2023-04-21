package clump.language.belang.statement.impl;

import clump.language.belang.expression.IExpression;
import clump.language.belang.statement.IStatement;
import opala.lexing.ILocation;

public class SetAttribute extends AbstractStatement implements IStatement {

    private final IExpression accessor;

    private final String name;

    private final IExpression value;

    public SetAttribute(ILocation location, IExpression accessor, String name, IExpression value) {
        super(location);
        this.accessor = accessor;
        this.name = name;
        this.value = value;
    }

    public IExpression getAccessor() {
        return accessor;
    }

    public String getName() {
        return name;
    }

    public IExpression getValue() {
        return value;
    }
}
