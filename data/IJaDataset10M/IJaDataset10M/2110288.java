package org.xteam.pascal.parser.ast;

import org.xteam.parser.runtime.Span;

public class IdentifierExpression extends Expression {

    public org.xteam.parser.runtime.reflect.AstNodeType getNodeType() {
        return PascalAstPackage.INSTANCE.getIdentifierExpressionType();
    }

    protected String value;

    public IdentifierExpression(Span span, String value) {
        super(span);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void visit(IPascalVisitor visitor) {
        visitor.visitIdentifierExpression(this);
    }
}
