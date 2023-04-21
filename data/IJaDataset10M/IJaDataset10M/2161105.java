package com.res.cobol.syntaxtree;

/**
 * Grammar production:
 * <PRE>
 * nodeToken -> &lt;INITIALIZE&gt;
 * nodeList -> ( Identifier() [ &lt;COMMACHAR&gt; ] )+
 * nodeOptional -> [ &lt;REPLACING&gt; ( ( &lt;ALPHABETIC&gt; | &lt;ALPHANUMERIC&gt; | &lt;NUMERIC&gt; | &lt;ALPHANUMERIC_EDITED&gt; | &lt;NUMERIC_EDITED&gt; | &lt;DBCS&gt; | &lt;EGCS&gt; ) [ &lt;DATA&gt; ] &lt;BY&gt; ( Identifier() | Literal() [ &lt;COMMACHAR&gt; ] ) )+ ]
 * </PRE>
 */
public class InitializeStatement extends com.res.cobol.RESNode implements Node {

    private Node parent;

    public NodeToken nodeToken;

    public NodeList nodeList;

    public NodeOptional nodeOptional;

    public InitializeStatement(NodeToken n0, NodeList n1, NodeOptional n2) {
        nodeToken = n0;
        if (nodeToken != null) nodeToken.setParent(this);
        nodeList = n1;
        if (nodeList != null) nodeList.setParent(this);
        nodeOptional = n2;
        if (nodeOptional != null) nodeOptional.setParent(this);
    }

    public InitializeStatement() {
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
