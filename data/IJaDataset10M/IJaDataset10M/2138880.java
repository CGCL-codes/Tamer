package org.xteam.pascal.parser.ast;

import org.xteam.parser.runtime.Span;

public class While extends Statement {

    public org.xteam.parser.runtime.reflect.AstNodeType getNodeType() {
        return PascalAstPackage.INSTANCE.getWhileType();
    }

    protected Expression condition;

    protected Statement statement;

    public While(Span span, Expression condition, Statement statement) {
        super(span);
        this.condition = condition;
        this.statement = statement;
    }

    public Expression getCondition() {
        return condition;
    }

    public void setCondition(Expression condition) {
        this.condition = condition;
    }

    public Statement getStatement() {
        return statement;
    }

    public void setStatement(Statement statement) {
        this.statement = statement;
    }

    public void visit(IPascalVisitor visitor) {
        visitor.visitWhile(this);
    }
}
