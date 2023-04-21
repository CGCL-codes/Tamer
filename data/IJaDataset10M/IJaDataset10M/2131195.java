package com.res.cobol.syntaxtree;

/**
 * Grammar production:
 * <PRE>
 * sQLMultiplicativeExpressions -> SQLMultiplicativeExpressions()
 * nodeList -> ( ( ( &lt;PLUSCHAR_SUBS&gt; | &lt;PLUSCHAR&gt; ) | ( &lt;MINUSCHAR_SUBS&gt; | &lt;MINUSCHAR&gt; ) | &lt;CONCAT&gt; ) SQLMultiplicativeExpressions() )+
 * </PRE>
 */
public class SQLAdditiveExpression extends com.res.cobol.RESNode implements Node {

    private Node parent;

    public SQLMultiplicativeExpressions sQLMultiplicativeExpressions;

    public NodeList nodeList;

    public SQLAdditiveExpression(SQLMultiplicativeExpressions n0, NodeList n1) {
        sQLMultiplicativeExpressions = n0;
        if (sQLMultiplicativeExpressions != null) sQLMultiplicativeExpressions.setParent(this);
        nodeList = n1;
        if (nodeList != null) nodeList.setParent(this);
    }

    public SQLAdditiveExpression() {
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
