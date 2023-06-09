package org.codecover.instrumentation.cobol85.syntaxtree;

/**
 * Grammar production:
 * <PRE>
 * f0 -> &lt;REWRITE&gt;
 * f1 -> RecordName()
 * f2 -> [ &lt;FROM&gt; QualifiedDataName() ]
 * f3 -> [ &lt;INVALID&gt; [ &lt;KEY&gt; ] StatementList() ]
 * f4 -> [ &lt;NOT&gt; &lt;INVALID&gt; [ &lt;KEY&gt; ] StatementList() ]
 * f5 -> [ &lt;END_REWRITE&gt; ]
 * </PRE>
 */
public class RewriteStatement implements Node {

    private Node parent;

    public NodeToken f0;

    public RecordName f1;

    public NodeOptional f2;

    public NodeOptional f3;

    public NodeOptional f4;

    public NodeOptional f5;

    public RewriteStatement(NodeToken n0, RecordName n1, NodeOptional n2, NodeOptional n3, NodeOptional n4, NodeOptional n5) {
        f0 = n0;
        if (f0 != null) f0.setParent(this);
        f1 = n1;
        if (f1 != null) f1.setParent(this);
        f2 = n2;
        if (f2 != null) f2.setParent(this);
        f3 = n3;
        if (f3 != null) f3.setParent(this);
        f4 = n4;
        if (f4 != null) f4.setParent(this);
        f5 = n5;
        if (f5 != null) f5.setParent(this);
    }

    public RewriteStatement(RecordName n0, NodeOptional n1, NodeOptional n2, NodeOptional n3, NodeOptional n4) {
        f0 = new NodeToken("rewrite");
        if (f0 != null) f0.setParent(this);
        f1 = n0;
        if (f1 != null) f1.setParent(this);
        f2 = n1;
        if (f2 != null) f2.setParent(this);
        f3 = n2;
        if (f3 != null) f3.setParent(this);
        f4 = n3;
        if (f4 != null) f4.setParent(this);
        f5 = n4;
        if (f5 != null) f5.setParent(this);
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
