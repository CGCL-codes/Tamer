package de.fraunhofer.isst.axbench.axlang.syntaxtree;

/**
 * Grammar production:
 * f0 -> <CARDINALITIES>
 * f1 -> "{"
 * f2 -> ( CardinalityAssignment() )*
 * f3 -> "}"
 */
public class Cardinalities implements Node {

    public NodeToken f0;

    public NodeToken f1;

    public NodeListOptional f2;

    public NodeToken f3;

    public Cardinalities(NodeToken n0, NodeToken n1, NodeListOptional n2, NodeToken n3) {
        f0 = n0;
        f1 = n1;
        f2 = n2;
        f3 = n3;
    }

    public Cardinalities(NodeListOptional n0) {
        f0 = new NodeToken("cardinalities");
        f1 = new NodeToken("{");
        f2 = n0;
        f3 = new NodeToken("}");
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
