package com.res.cobol.syntaxtree;

/**
 * Grammar production:
 * <PRE>
 * nodeChoice -> ( &lt;JUSTIFIED&gt; | &lt;JUST&gt; )
 * nodeOptional -> [ &lt;RIGHT&gt; ]
 * </PRE>
 */
public class DataJustifiedClause extends com.res.cobol.RESNode implements Node {

    private Node parent;

    public NodeChoice nodeChoice;

    public NodeOptional nodeOptional;

    public DataJustifiedClause(NodeChoice n0, NodeOptional n1) {
        nodeChoice = n0;
        if (nodeChoice != null) nodeChoice.setParent(this);
        nodeOptional = n1;
        if (nodeOptional != null) nodeOptional.setParent(this);
    }

    public DataJustifiedClause() {
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
