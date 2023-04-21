package de.fraunhofer.isst.axbench.axlang.syntaxtree;

/**
 * Grammar production:
 * f0 -> [ <OPTIONAL> ]
 * f1 -> <IDENTIFIER>
 * f2 -> <IDENTIFIER>
 * f3 -> ( "," <IDENTIFIER> )*
 * f4 -> [ <BINDINGTIME> ]
 * f5 -> [ Attributes() ]
 * f6 -> ";"
 */
public class AtomicSubcomponent implements Node {

    public NodeOptional f0;

    public NodeToken f1;

    public NodeToken f2;

    public NodeListOptional f3;

    public NodeOptional f4;

    public NodeOptional f5;

    public NodeToken f6;

    public AtomicSubcomponent(NodeOptional n0, NodeToken n1, NodeToken n2, NodeListOptional n3, NodeOptional n4, NodeOptional n5, NodeToken n6) {
        f0 = n0;
        f1 = n1;
        f2 = n2;
        f3 = n3;
        f4 = n4;
        f5 = n5;
        f6 = n6;
    }

    public AtomicSubcomponent(NodeOptional n0, NodeToken n1, NodeToken n2, NodeListOptional n3, NodeOptional n4, NodeOptional n5) {
        f0 = n0;
        f1 = n1;
        f2 = n2;
        f3 = n3;
        f4 = n4;
        f5 = n5;
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
