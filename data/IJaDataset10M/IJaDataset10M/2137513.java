package net.sf.dewdrop.sqlml.expr;

public class Or extends BooleanExpression {

    private static final String OR_OPERATOR = "or";

    public Or() {
        setOperator(OR_OPERATOR);
    }
}
