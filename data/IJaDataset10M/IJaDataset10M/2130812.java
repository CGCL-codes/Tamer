package com.res.cobol.syntaxtree;

/**
 * Grammar production:
 * <PRE>
 * nodeList -> ( Statement() )+
 * nodeToken -> &lt;DOT&gt;
 * </PRE>
 */
public class Sentence extends com.res.cobol.RESNode implements Node {

    private Node parent;

    public NodeList nodeList;

    public NodeToken nodeToken;

    public Sentence(NodeList n0, NodeToken n1) {
        nodeList = n0;
        if (nodeList != null) nodeList.setParent(this);
        nodeToken = n1;
        if (nodeToken != null) nodeToken.setParent(this);
    }

    public Sentence() {
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
