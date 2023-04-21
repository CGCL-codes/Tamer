package com.res.cobol.syntaxtree;

/**
 * Grammar production:
 * <PRE>
 * nodeChoice -> ( TableReference() | &lt;LPARENCHAR&gt; FromItemExpression() &lt;RPARENCHAR&gt; )
 * nodeChoice1 -> ( JoinerExpression() [ &lt;K_AS&gt; AsObjectName() ] | [ AsObjectName() ] )
 * </PRE>
 */
public class FromItem extends com.res.cobol.RESNode implements Node {

    private Node parent;

    public NodeChoice nodeChoice;

    public NodeChoice nodeChoice1;

    public FromItem(NodeChoice n0, NodeChoice n1) {
        nodeChoice = n0;
        if (nodeChoice != null) nodeChoice.setParent(this);
        nodeChoice1 = n1;
        if (nodeChoice1 != null) nodeChoice1.setParent(this);
    }

    public FromItem() {
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
