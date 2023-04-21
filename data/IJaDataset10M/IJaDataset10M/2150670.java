package clump.language.belang.statement.impl;

import clump.language.belang.expression.IExpression;
import clump.language.belang.statement.IStatement;
import clump.language.belang.type.IType;
import opala.lexing.ILocation;

public class NewVariable extends AbstractStatement implements IStatement {

    private final IType specification;

    private final String name;

    private final IExpression value;

    public NewVariable(ILocation location, IType specification, String name, IExpression value) {
        super(location);
        this.specification = specification;
        this.name = name;
        this.value = value;
    }

    public IType getSpecification() {
        return specification;
    }

    public String getName() {
        return name;
    }

    public IExpression getValue() {
        return value;
    }
}
