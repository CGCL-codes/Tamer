package com.googlecode.beauti4j.core.web.gwt.client.data.search;

/**
 * Superclass of binary logical expressions
 */
public class LogicalExpression implements Criterion {

    private Criterion lhs;

    private Criterion rhs;

    private String op;

    public LogicalExpression() {
    }

    public LogicalExpression(Criterion lhs, Criterion rhs, String op) {
        this.lhs = lhs;
        this.rhs = rhs;
        this.op = op;
    }

    public String getOp() {
        return op;
    }

    public Criterion getLhs() {
        return lhs;
    }

    public Criterion getRhs() {
        return rhs;
    }
}
