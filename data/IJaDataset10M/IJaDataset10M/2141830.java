package de.axbench.axlang.text.syntaxtree;

/**
 * Grammar production:
 * f0 -> [ <OPTIONAL> ]
 * f1 -> <DATA_TYPE>
 * f2 -> <IDENTIFIER>
 * f3 -> "("
 * f4 -> [ <DATA_TYPE> <IDENTIFIER> ( "," <DATA_TYPE> <IDENTIFIER> )* ]
 * f5 -> ")"
 * f6 -> ";"
 */
public class Operation implements Node {

    public NodeOptional f0;

    public NodeToken f1;

    public NodeToken f2;

    public NodeToken f3;

    public NodeOptional f4;

    public NodeToken f5;

    public NodeToken f6;

    public Operation(NodeOptional n0, NodeToken n1, NodeToken n2, NodeToken n3, NodeOptional n4, NodeToken n5, NodeToken n6) {
        f0 = n0;
        f1 = n1;
        f2 = n2;
        f3 = n3;
        f4 = n4;
        f5 = n5;
        f6 = n6;
    }

    public Operation(NodeOptional n0, NodeToken n1, NodeToken n2, NodeOptional n3) {
        f0 = n0;
        f1 = n1;
        f2 = n2;
        f3 = new NodeToken("(");
        f4 = n3;
        f5 = new NodeToken(")");
        f6 = new NodeToken(";");
    }

    public void accept(de.axbench.axlang.text.visitor.Visitor v) {
        v.visit(this);
    }

    public <R, A> R accept(de.axbench.axlang.text.visitor.GJVisitor<R, A> v, A argu) {
        return v.visit(this, argu);
    }

    public <R> R accept(de.axbench.axlang.text.visitor.GJNoArguVisitor<R> v) {
        return v.visit(this);
    }

    public <A> void accept(de.axbench.axlang.text.visitor.GJVoidVisitor<A> v, A argu) {
        v.visit(this, argu);
    }
}
