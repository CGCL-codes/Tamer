package org.codecover.instrumentation.cobol85.syntaxtree;

/**
 * Grammar production:
 * <PRE>
 * f0 -> ProcedureName()
 * f1 -> [ ( &lt;THROUGH&gt; | &lt;THRU&gt; ) ProcedureName() ]
 * </PRE>
 */
public class PerformProcedureScopeClause implements Node {

    private Node parent;

    public ProcedureName f0;

    public NodeOptional f1;

    public PerformProcedureScopeClause(ProcedureName n0, NodeOptional n1) {
        f0 = n0;
        if (f0 != null) f0.setParent(this);
        f1 = n1;
        if (f1 != null) f1.setParent(this);
    }

    public void accept(org.codecover.instrumentation.cobol85.visitor.Visitor v) {
        v.visit(this);
    }

    public <R, A> R accept(org.codecover.instrumentation.cobol85.visitor.GJVisitor<R, A> v, A argu) {
        return v.visit(this, argu);
    }

    public <R> R accept(org.codecover.instrumentation.cobol85.visitor.GJNoArguVisitor<R> v) {
        return v.visit(this);
    }

    public <A> void accept(org.codecover.instrumentation.cobol85.visitor.GJVoidVisitor<A> v, A argu) {
        v.visit(this, argu);
    }

    public void setParent(Node n) {
        parent = n;
    }

    public Node getParent() {
        return parent;
    }
}
