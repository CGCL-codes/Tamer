package com.res.cobol.syntaxtree;

/**
 * Grammar production:
 * <PRE>
 * nodeToken -> &lt;DISPLAY&gt;
 * nodeList -> ( ( Identifier() | Literal() ) [ &lt;COMMACHAR&gt; ] )+
 * nodeOptional -> [ &lt;UPON&gt; ( MnemonicName() | EnvironmentName() ) ]
 * nodeOptional1 -> [ [ &lt;WITH&gt; ] &lt;NO&gt; &lt;ADVANCING&gt; ]
 * </PRE>
 */
public class DisplayStatement extends com.res.cobol.RESNode implements Node {

    private Node parent;

    public NodeToken nodeToken;

    public NodeList nodeList;

    public NodeOptional nodeOptional;

    public NodeOptional nodeOptional1;

    public DisplayStatement(NodeToken n0, NodeList n1, NodeOptional n2, NodeOptional n3) {
        nodeToken = n0;
        if (nodeToken != null) nodeToken.setParent(this);
        nodeList = n1;
        if (nodeList != null) nodeList.setParent(this);
        nodeOptional = n2;
        if (nodeOptional != null) nodeOptional.setParent(this);
        nodeOptional1 = n3;
        if (nodeOptional1 != null) nodeOptional1.setParent(this);
    }

    public DisplayStatement() {
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
