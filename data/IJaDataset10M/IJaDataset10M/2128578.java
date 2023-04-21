package com.res.cobol.syntaxtree;

/**
 * Grammar production:
 * <PRE>
 * nodeSequence -> ( DataName() ( ( &lt;IN&gt; | &lt;OF&gt; ) DataName() )* [ ( &lt;IN&gt; | &lt;OF&gt; ) FileName() ] )
 * </PRE>
 */
public class QualifiedDataName extends com.res.cobol.RESNode implements Node {

    private Node parent;

    public NodeSequence nodeSequence;

    public QualifiedDataName(NodeSequence n0) {
        nodeSequence = n0;
        if (nodeSequence != null) nodeSequence.setParent(this);
    }

    public QualifiedDataName() {
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
