package com.res.cobol.syntaxtree;

/**
 * Grammar production:
 * <PRE>
 * nodeToken -> &lt;INDEXED&gt;
 * </PRE>
 */
public class IndexedOrganizationClause extends com.res.cobol.RESNode implements Node {

    private Node parent;

    public NodeToken nodeToken;

    public IndexedOrganizationClause(NodeToken n0) {
        nodeToken = n0;
        if (nodeToken != null) nodeToken.setParent(this);
    }

    public IndexedOrganizationClause() {
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
