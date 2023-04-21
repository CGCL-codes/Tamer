package com.res.cobol.syntaxtree;

/**
 * Grammar production:
 * <PRE>
 * nodeToken -> &lt;REWRITE&gt;
 * recordName -> RecordName()
 * nodeOptional -> [ &lt;FROM&gt; Identifier() ]
 * nodeOptional1 -> [ &lt;INVALID&gt; [ &lt;KEY&gt; ] StatementList() ]
 * nodeOptional2 -> [ &lt;NOT&gt; &lt;INVALID&gt; [ &lt;KEY&gt; ] StatementList() ]
 * nodeOptional3 -> [ &lt;END_REWRITE&gt; ]
 * </PRE>
 */
public class RewriteStatement extends com.res.cobol.RESNode implements Node {

    private Node parent;

    public NodeToken nodeToken;

    public RecordName recordName;

    public NodeOptional nodeOptional;

    public NodeOptional nodeOptional1;

    public NodeOptional nodeOptional2;

    public NodeOptional nodeOptional3;

    public RewriteStatement(NodeToken n0, RecordName n1, NodeOptional n2, NodeOptional n3, NodeOptional n4, NodeOptional n5) {
        nodeToken = n0;
        if (nodeToken != null) nodeToken.setParent(this);
        recordName = n1;
        if (recordName != null) recordName.setParent(this);
        nodeOptional = n2;
        if (nodeOptional != null) nodeOptional.setParent(this);
        nodeOptional1 = n3;
        if (nodeOptional1 != null) nodeOptional1.setParent(this);
        nodeOptional2 = n4;
        if (nodeOptional2 != null) nodeOptional2.setParent(this);
        nodeOptional3 = n5;
        if (nodeOptional3 != null) nodeOptional3.setParent(this);
    }

    public RewriteStatement() {
    }

    public void accept(com.res.cobol.visitor.Visitor v) {
        v.visit(this);
    }

    public <R, A> R accept(com.res.cobol.visitor.GJVisitor<R, A> v, A argu) {
        return v.visit(this, argu);
    }

    public <R> R accept(com.res.cobol.visitor.GJNoArguVisitor<R> v) {
        return v.visit(this);
    }

    public <A> void accept(com.res.cobol.visitor.GJVoidVisitor<A> v, A argu) {
        v.visit(this, argu);
    }

    public void setParent(Node n) {
        parent = n;
    }

    public Node getParent() {
        return parent;
    }
}
