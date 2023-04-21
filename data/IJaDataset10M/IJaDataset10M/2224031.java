package com.res.cobol.syntaxtree;

/**
 * Grammar production:
 * <PRE>
 * paragraphs -> Paragraphs()
 * nodeListOptional -> ( ProcedureSection() )*
 * </PRE>
 */
public class ProcedureBody extends com.res.cobol.RESNode implements Node {

    private Node parent;

    public Paragraphs paragraphs;

    public NodeListOptional nodeListOptional;

    public ProcedureBody(Paragraphs n0, NodeListOptional n1) {
        paragraphs = n0;
        if (paragraphs != null) paragraphs.setParent(this);
        nodeListOptional = n1;
        if (nodeListOptional != null) nodeListOptional.setParent(this);
    }

    public ProcedureBody() {
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
