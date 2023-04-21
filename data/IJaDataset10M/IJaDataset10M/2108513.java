package de.fraunhofer.isst.axbench.axlang.syntaxtree;

/**
 * Grammar production:
 * f0 -> <DO>
 * f1 -> Block()
 * f2 -> <WHILE>
 * f3 -> "("
 * f4 -> Expression()
 * f5 -> ")"
 * f6 -> ";"
 */
public class DoStatement implements Node {

    public NodeToken f0;

    public Block f1;

    public NodeToken f2;

    public NodeToken f3;

    public Expression f4;

    public NodeToken f5;

    public NodeToken f6;

    public DoStatement(NodeToken n0, Block n1, NodeToken n2, NodeToken n3, Expression n4, NodeToken n5, NodeToken n6) {
        f0 = n0;
        f1 = n1;
        f2 = n2;
        f3 = n3;
        f4 = n4;
        f5 = n5;
        f6 = n6;
    }

    public DoStatement(Block n0, Expression n1) {
        f0 = new NodeToken("do");
        f1 = n0;
        f2 = new NodeToken("while");
        f3 = new NodeToken("(");
        f4 = n1;
        f5 = new NodeToken(")");
        f6 = new NodeToken(";");
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
