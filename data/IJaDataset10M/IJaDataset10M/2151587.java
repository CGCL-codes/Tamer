package com.res.cobol.syntaxtree;

/**
 * Grammar production:
 * <PRE>
 * nodeChoice -> ( &lt;BEFORE&gt; | &lt;AFTER&gt; )
 * nodeOptional -> [ &lt;ADVANCING&gt; ]
 * nodeChoice1 -> ( &lt;PAGE&gt; | ( Identifier() | IntegerConstant() | FigurativeConstant() ) [ ( &lt;LINE&gt; | &lt;LINES&gt; ) ] | MnemonicName() )
 * </PRE>
 */
public class AdvancingPhrase extends com.res.cobol.RESNode implements Node {

    private Node parent;

    public NodeChoice nodeChoice;

    public NodeOptional nodeOptional;

    public NodeChoice nodeChoice1;

    public AdvancingPhrase(NodeChoice n0, NodeOptional n1, NodeChoice n2) {
        nodeChoice = n0;
        if (nodeChoice != null) nodeChoice.setParent(this);
        nodeOptional = n1;
        if (nodeOptional != null) nodeOptional.setParent(this);
        nodeChoice1 = n2;
        if (nodeChoice1 != null) nodeChoice1.setParent(this);
    }

    public AdvancingPhrase() {
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
