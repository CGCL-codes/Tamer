package com.googlecode.sarasvati.rubric.lang;

import java.util.Date;
import com.googlecode.sarasvati.rubric.env.RubricEnv;
import com.googlecode.sarasvati.rubric.visitor.RubricVisitor;

public class RubricStmtDateSymbol extends AbstractRubricStmt {

    protected String symbol;

    public RubricStmtDateSymbol(final String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(final String symbol) {
        this.symbol = symbol;
    }

    @Override
    public Date eval(final RubricEnv env) {
        return env.evalDateFunction(symbol);
    }

    @Override
    public void traverse(final RubricVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean isEqualTo(final RubricStmt stmt) {
        return stmt.isDateSymbol() && stmt.asDateSymbol().getSymbol().equals(symbol);
    }

    @Override
    public RubricStmtDateSymbol asDateSymbol() {
        return this;
    }

    @Override
    public boolean isDateSymbol() {
        return true;
    }
}
