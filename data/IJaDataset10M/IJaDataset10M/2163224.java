package net.sf.opendf.cal.ast;

public abstract class Expression extends ASTNode {

    public abstract void accept(ExpressionVisitor v);
}
