package org.owasp.orizon.mirage.java.parser;

public class PrimaryExpression extends Expression {

    public PrimaryExpression(int id) {
        super(id);
    }

    public PrimaryExpression() {
        super(JavaConstants.PRIMARYEXPRESSION);
    }
}
