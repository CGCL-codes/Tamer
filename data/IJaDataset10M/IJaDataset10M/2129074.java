package com.res.cobol.syntaxtree;

/**
 * Grammar production:
 * <PRE>
 * nodeToken -> &lt;S_BIND&gt;
 * nodeOptional -> [ [ &lt;K_INDICATOR&gt; ] &lt;S_BIND&gt; ]
 * </PRE>
 */
public class IndicatorBind extends com.res.cobol.RESNode implements Node {

    private Node parent;

    public NodeToken nodeToken;

    public NodeOptional nodeOptional;

    public IndicatorBind(NodeToken n0, NodeOptional n1) {
        nodeToken = n0;
        if (nodeToken != null) nodeToken.setParent(this);
        nodeOptional = n1;
        if (nodeOptional != null) nodeOptional.setParent(this);
    }

    public IndicatorBind() {
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
