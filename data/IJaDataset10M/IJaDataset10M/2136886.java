package clump.language.belang.statement.impl;

import clump.language.belang.expression.IExpression;
import clump.language.belang.statement.IStatement;
import opala.lexing.ILocation;

public class While extends AbstractStatement implements IStatement {

    private final boolean blocFirst;

    private final IExpression condition;

    private final IStatement statement;

    public While(ILocation location, boolean blocFirst, IExpression condition, IStatement statement) {
        super(location);
        this.blocFirst = blocFirst;
        this.condition = condition;
        this.statement = statement;
    }

    public boolean isBlocFirst() {
        return blocFirst;
    }

    public IExpression getCondition() {
        return condition;
    }

    public IStatement getStatement() {
        return statement;
    }
}
