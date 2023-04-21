package org.xteam.pascal.parser.ast;

import org.xteam.parser.runtime.Span;

public class Assignment extends Statement {

    public org.xteam.parser.runtime.reflect.AstNodeType getNodeType() {
        return PascalAstPackage.INSTANCE.getAssignmentType();
    }

    protected Access variable;

    protected Expression expression;

    public Assignment(Span span, Access variable, Expression expression) {
        super(span);
        this.variable = variable;
        this.expression = expression;
    }

    public Access getVariable() {
        return variable;
    }

    public void setVariable(Access variable) {
        this.variable = variable;
    }

    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    public void visit(IPascalVisitor visitor) {
        visitor.visitAssignment(this);
    }
}
