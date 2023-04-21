package com.res.cobol.syntaxtree;

/**
 * Grammar production:
 * <PRE>
 * nodeListOptional -> ( Sentence() )*
 * nodeListOptional1 -> ( Paragraph() )*
 * </PRE>
 */
public class Paragraphs extends com.res.cobol.RESNode implements Node {

    private Node parent;

    public NodeListOptional nodeListOptional;

    public NodeListOptional nodeListOptional1;

    public Paragraphs(NodeListOptional n0, NodeListOptional n1) {
        nodeListOptional = n0;
        if (nodeListOptional != null) nodeListOptional.setParent(this);
        nodeListOptional1 = n1;
        if (nodeListOptional1 != null) nodeListOptional1.setParent(this);
    }

    public Paragraphs() {
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
