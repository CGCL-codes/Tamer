package com.res.cobol.syntaxtree;

/**
 * Grammar production:
 * <PRE>
 * nodeOptional -> [ ( ( &lt;PLUSCHAR_SUBS&gt; | &lt;PLUSCHAR&gt; ) | ( &lt;MINUSCHAR_SUBS&gt; | &lt;MINUSCHAR&gt; ) ) ]
 * basis -> Basis()
 * nodeListOptional -> ( &lt;POW&gt; Basis() )*
 * </PRE>
 */
public class Power extends com.res.cobol.RESNode implements Node {

    private Node parent;

    public NodeOptional nodeOptional;

    public Basis basis;

    public NodeListOptional nodeListOptional;

    public Power(NodeOptional n0, Basis n1, NodeListOptional n2) {
        nodeOptional = n0;
        if (nodeOptional != null) nodeOptional.setParent(this);
        basis = n1;
        if (basis != null) basis.setParent(this);
        nodeListOptional = n2;
        if (nodeListOptional != null) nodeListOptional.setParent(this);
    }

    public Power() {
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
