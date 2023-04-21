package de.fraunhofer.isst.axbench.axlang.syntaxtree;

/**
 * Grammar production:
 * f0 -> [ "\'" <IDENTIFIER> "\'" ]
 * f1 -> [ ( <IDENTIFIER> | <THIS> ) "." ]
 * f2 -> <IDENTIFIER>
 * f3 -> "--"
 * f4 -> ( <IDENTIFIER> | <THIS> )
 * f5 -> "."
 * f6 -> <IDENTIFIER>
 * f7 -> ";"
 */
public class HWConnection implements Node {

    public NodeOptional f0;

    public NodeOptional f1;

    public NodeToken f2;

    public NodeToken f3;

    public NodeChoice f4;

    public NodeToken f5;

    public NodeToken f6;

    public NodeToken f7;

    public HWConnection(NodeOptional n0, NodeOptional n1, NodeToken n2, NodeToken n3, NodeChoice n4, NodeToken n5, NodeToken n6, NodeToken n7) {
        f0 = n0;
        f1 = n1;
        f2 = n2;
        f3 = n3;
        f4 = n4;
        f5 = n5;
        f6 = n6;
        f7 = n7;
    }

    public HWConnection(NodeOptional n0, NodeOptional n1, NodeToken n2, NodeChoice n3, NodeToken n4) {
        f0 = n0;
        f1 = n1;
        f2 = n2;
        f3 = new NodeToken("--");
        f4 = n3;
        f5 = new NodeToken(".");
        f6 = n4;
        f7 = new NodeToken(";");
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
