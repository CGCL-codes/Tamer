package com.res.cobol.syntaxtree;

/**
 * Grammar production:
 * <PRE>
 * nodeOptional -> [ &lt;K_NOT&gt; ]
 * nodeToken -> &lt;K_IN&gt;
 * nodeToken1 -> &lt;LPARENCHAR&gt;
 * plSqlExpressionList -> PlSqlExpressionList()
 * nodeToken2 -> &lt;RPARENCHAR&gt;
 * </PRE>
 */
public class PlSqlInClause extends com.res.cobol.RESNode implements Node {

    private Node parent;

    public NodeOptional nodeOptional;

    public NodeToken nodeToken;

    public NodeToken nodeToken1;

    public PlSqlExpressionList plSqlExpressionList;

    public NodeToken nodeToken2;

    public PlSqlInClause(NodeOptional n0, NodeToken n1, NodeToken n2, PlSqlExpressionList n3, NodeToken n4) {
        nodeOptional = n0;
        if (nodeOptional != null) nodeOptional.setParent(this);
        nodeToken = n1;
        if (nodeToken != null) nodeToken.setParent(this);
        nodeToken1 = n2;
        if (nodeToken1 != null) nodeToken1.setParent(this);
        plSqlExpressionList = n3;
        if (plSqlExpressionList != null) plSqlExpressionList.setParent(this);
        nodeToken2 = n4;
        if (nodeToken2 != null) nodeToken2.setParent(this);
    }

    public PlSqlInClause() {
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
