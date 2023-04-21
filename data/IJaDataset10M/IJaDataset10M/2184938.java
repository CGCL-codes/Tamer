package com.res.cobol.syntaxtree;

/**
 * Grammar production:
 * <PRE>
 * nodeChoice -> ( ( &lt;PLUSCHAR&gt; | &lt;PLUSCHAR_SUBS&gt; ) | ( &lt;MINUSCHAR&gt; | &lt;MINUSCHAR_SUBS&gt; ) )
 * sQLPrimaryExpression -> SQLPrimaryExpression()
 * </PRE>
 */
public class SQLUnaryExpression extends com.res.cobol.RESNode implements Node {

    private Node parent;

    public NodeChoice nodeChoice;

    public SQLPrimaryExpression sQLPrimaryExpression;

    public SQLUnaryExpression(NodeChoice n0, SQLPrimaryExpression n1) {
        nodeChoice = n0;
        if (nodeChoice != null) nodeChoice.setParent(this);
        sQLPrimaryExpression = n1;
        if (sQLPrimaryExpression != null) sQLPrimaryExpression.setParent(this);
    }

    public SQLUnaryExpression() {
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
