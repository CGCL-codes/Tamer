package com.res.cobol.syntaxtree;

/**
 * Grammar production:
 * <PRE>
 * nodeOptional -> [ &lt;K_NOT&gt; ]
 * nodeToken -> &lt;K_PRIOR&gt;
 * sQLSimpleExpressions -> SQLSimpleExpressions()
 * </PRE>
 */
public class SQLPriorExpression extends com.res.cobol.RESNode implements Node {

    private Node parent;

    public NodeOptional nodeOptional;

    public NodeToken nodeToken;

    public SQLSimpleExpressions sQLSimpleExpressions;

    public SQLPriorExpression(NodeOptional n0, NodeToken n1, SQLSimpleExpressions n2) {
        nodeOptional = n0;
        if (nodeOptional != null) nodeOptional.setParent(this);
        nodeToken = n1;
        if (nodeToken != null) nodeToken.setParent(this);
        sQLSimpleExpressions = n2;
        if (sQLSimpleExpressions != null) sQLSimpleExpressions.setParent(this);
    }

    public SQLPriorExpression() {
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
