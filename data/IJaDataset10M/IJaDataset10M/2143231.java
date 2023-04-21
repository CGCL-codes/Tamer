package com.res.cobol.syntaxtree;

/**
 * Grammar production:
 * <PRE>
 * nodeToken -> &lt;REPLACING&gt;
 * nodeList -> ( &lt;CHARACTERS&gt; &lt;BY&gt; ( Identifier() | Literal() ) ( BeforeAfterPhrase() )* | ( &lt;ALL&gt; | &lt;LEADING&gt; | &lt;FIRST&gt; ) ( ( Identifier() | Literal() ) &lt;BY&gt; ( Identifier() | Literal() ) ( BeforeAfterPhrase() )* )+ )+
 * </PRE>
 */
public class ReplacingPhrase extends com.res.cobol.RESNode implements Node {

    private Node parent;

    public NodeToken nodeToken;

    public NodeList nodeList;

    public ReplacingPhrase(NodeToken n0, NodeList n1) {
        nodeToken = n0;
        if (nodeToken != null) nodeToken.setParent(this);
        nodeList = n1;
        if (nodeList != null) nodeList.setParent(this);
    }

    public ReplacingPhrase() {
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
