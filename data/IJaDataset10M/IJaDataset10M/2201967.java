package de.fraunhofer.isst.axbench.axlang.syntaxtree;

/**
 * Grammar production:
 * f0 -> "("
 * f1 -> Expression()
 * f2 -> ")"
 */
public class BracketsExpression implements Node {

    public NodeToken f0;

    public Expression f1;

    public NodeToken f2;

    public BracketsExpression(NodeToken n0, Expression n1, NodeToken n2) {
        f0 = n0;
        f1 = n1;
        f2 = n2;
    }

    public BracketsExpression(Expression n0) {
        f0 = new NodeToken("(");
        f1 = n0;
        f2 = new NodeToken(")");
    }

    public void accept(de.fraunhofer.isst.axbench.axlang.visitor.Visitor v) {
        v.visit(this);
    }

    public <R, A> R accept(de.fraunhofer.isst.axbench.axlang.visitor.GJVisitor<R, A> v, A argu) {
        return v.visit(this, argu);
    }

    public <R> R accept(de.fraunhofer.isst.axbench.axlang.visitor.GJNoArguVisitor<R> v) {
        return v.visit(this);
    }

    public <A> void accept(de.fraunhofer.isst.axbench.axlang.visitor.GJVoidVisitor<A> v, A argu) {
        v.visit(this, argu);
    }
}
