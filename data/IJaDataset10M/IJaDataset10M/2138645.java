package com.res.cobol.syntaxtree;

/**
 * Grammar production:
 * <PRE>
 * nodeToken -> &lt;K_USING&gt;
 * nodeChoice -> ( &lt;S_IDENTIFIER&gt; | &lt;S_BIND&gt; )
 * </PRE>
 */
public class SQLUsingDMLReturn extends com.res.cobol.RESNode implements Node {

    private Node parent;

    public NodeToken nodeToken;

    public NodeChoice nodeChoice;

    public SQLUsingDMLReturn(NodeToken n0, NodeChoice n1) {
        nodeToken = n0;
        if (nodeToken != null) nodeToken.setParent(this);
        nodeChoice = n1;
        if (nodeChoice != null) nodeChoice.setParent(this);
    }

    public SQLUsingDMLReturn() {
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
