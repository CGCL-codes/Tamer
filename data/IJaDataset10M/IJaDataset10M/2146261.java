package com.res.cobol.syntaxtree;

/**
 * Grammar production:
 * <PRE>
 * nodeChoice -> ( &lt;VALUE&gt; [ &lt;IS&gt; ] | &lt;VALUES&gt; [ &lt;ARE&gt; ] )
 * nodeList -> ( ( Identifier() | Literal() ) [ &lt;COMMACHAR&gt; ] [ ( &lt;THROUGH&gt; | &lt;THRU&gt; ) Literal() [ &lt;COMMACHAR&gt; ] ] )+
 * </PRE>
 */
public class DataValueClause extends com.res.cobol.RESNode implements Node {

    private Node parent;

    public NodeChoice nodeChoice;

    public NodeList nodeList;

    public DataValueClause(NodeChoice n0, NodeList n1) {
        nodeChoice = n0;
        if (nodeChoice != null) nodeChoice.setParent(this);
        nodeList = n1;
        if (nodeList != null) nodeList.setParent(this);
    }

    public DataValueClause() {
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
