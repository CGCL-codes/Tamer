package org.owasp.orizon.mirage.java.parser;

public class PreDecrementExpression extends Expression {

    public PreDecrementExpression(int id) {
        super(id);
    }

    public PreDecrementExpression() {
        super(JavaConstants.PREDECREMENTEXPRESSION);
    }
}
