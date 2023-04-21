package clump.language.belang.statement.impl;

import clump.language.belang.statement.IStatement;
import opala.lexing.ILocation;

public class TryCatch extends AbstractStatement implements IStatement {

    private final IStatement statement;

    private final String exceptionName;

    private final IStatement catches;

    public TryCatch(ILocation location, IStatement statement, String exceptionName, IStatement catches) {
        super(location);
        this.statement = statement;
        this.exceptionName = exceptionName;
        this.catches = catches;
    }

    public IStatement getStatement() {
        return statement;
    }

    public String getExceptionName() {
        return exceptionName;
    }

    public IStatement getCatches() {
        return catches;
    }
}
