package com.res.cobol.syntaxtree;

/**
 * Grammar production:
 * <PRE>
 * nodeToken -> &lt;UNSTRING&gt;
 * identifier -> Identifier()
 * nodeOptional -> [ &lt;DELIMITED&gt; [ &lt;BY&gt; ] [ &lt;ALL&gt; ] ( Identifier() | Literal() ) ( &lt;OR&gt; [ &lt;ALL&gt; ] ( Identifier() | Literal() ) )* ]
 * nodeToken1 -> &lt;INTO&gt;
 * nodeList -> ( Identifier() [ &lt;DELIMITER&gt; [ &lt;IN&gt; ] Identifier() ] [ &lt;COUNT&gt; [ &lt;IN&gt; ] Identifier() ] [ &lt;COMMACHAR&gt; ] )+
 * nodeOptional1 -> [ [ &lt;WITH&gt; ] &lt;POINTER&gt; QualifiedDataName() ]
 * nodeOptional2 -> [ &lt;TALLYING&gt; [ &lt;IN&gt; ] QualifiedDataName() ]
 * nodeOptional3 -> [ [ &lt;ON&gt; ] &lt;OVERFLOW&gt; StatementList() ]
 * nodeOptional4 -> [ &lt;NOT&gt; [ &lt;ON&gt; ] &lt;OVERFLOW&gt; StatementList() ]
 * nodeOptional5 -> [ &lt;END_UNSTRING&gt; ]
 * </PRE>
 */
public class UnstringStatement extends com.res.cobol.RESNode implements Node {

    private Node parent;

    public NodeToken nodeToken;

    public Identifier identifier;

    public NodeOptional nodeOptional;

    public NodeToken nodeToken1;

    public NodeList nodeList;

    public NodeOptional nodeOptional1;

    public NodeOptional nodeOptional2;

    public NodeOptional nodeOptional3;

    public NodeOptional nodeOptional4;

    public NodeOptional nodeOptional5;

    public UnstringStatement(NodeToken n0, Identifier n1, NodeOptional n2, NodeToken n3, NodeList n4, NodeOptional n5, NodeOptional n6, NodeOptional n7, NodeOptional n8, NodeOptional n9) {
        nodeToken = n0;
        if (nodeToken != null) nodeToken.setParent(this);
        identifier = n1;
        if (identifier != null) identifier.setParent(this);
        nodeOptional = n2;
        if (nodeOptional != null) nodeOptional.setParent(this);
        nodeToken1 = n3;
        if (nodeToken1 != null) nodeToken1.setParent(this);
        nodeList = n4;
        if (nodeList != null) nodeList.setParent(this);
        nodeOptional1 = n5;
        if (nodeOptional1 != null) nodeOptional1.setParent(this);
        nodeOptional2 = n6;
        if (nodeOptional2 != null) nodeOptional2.setParent(this);
        nodeOptional3 = n7;
        if (nodeOptional3 != null) nodeOptional3.setParent(this);
        nodeOptional4 = n8;
        if (nodeOptional4 != null) nodeOptional4.setParent(this);
        nodeOptional5 = n9;
        if (nodeOptional5 != null) nodeOptional5.setParent(this);
    }

    public UnstringStatement() {
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
