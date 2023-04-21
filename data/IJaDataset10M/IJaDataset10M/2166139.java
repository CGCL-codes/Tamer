package com.res.cobol.syntaxtree;

/**
 * Grammar production:
 * <PRE>
 * nodeToken -> &lt;SEGMENT_LIMIT&gt;
 * nodeOptional -> [ &lt;IS&gt; ]
 * integerConstant -> IntegerConstant()
 * </PRE>
 */
public class SegmentLimitClause extends com.res.cobol.RESNode implements Node {

    private Node parent;

    public NodeToken nodeToken;

    public NodeOptional nodeOptional;

    public IntegerConstant integerConstant;

    public SegmentLimitClause(NodeToken n0, NodeOptional n1, IntegerConstant n2) {
        nodeToken = n0;
        if (nodeToken != null) nodeToken.setParent(this);
        nodeOptional = n1;
        if (nodeOptional != null) nodeOptional.setParent(this);
        integerConstant = n2;
        if (integerConstant != null) integerConstant.setParent(this);
    }

    public SegmentLimitClause() {
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
