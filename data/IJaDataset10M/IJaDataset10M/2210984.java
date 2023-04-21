package org.codemon.insertion.c.parser.syntaxtree;

/**
 * Grammar production:
 * declarationSpecifiers -> DeclarationSpecifiers()
 * nodeOptional -> [ InitDeclaratorList() ]
 * nodeToken -> ";"
 */
public class Declaration implements Node {

    private Node parent;

    public DeclarationSpecifiers declarationSpecifiers;

    public NodeOptional nodeOptional;

    public NodeToken nodeToken;

    public Declaration(DeclarationSpecifiers n0, NodeOptional n1, NodeToken n2) {
        declarationSpecifiers = n0;
        if (declarationSpecifiers != null) declarationSpecifiers.setParent(this);
        nodeOptional = n1;
        if (nodeOptional != null) nodeOptional.setParent(this);
        nodeToken = n2;
        if (nodeToken != null) nodeToken.setParent(this);
    }

    public Declaration(DeclarationSpecifiers n0, NodeOptional n1) {
        declarationSpecifiers = n0;
        if (declarationSpecifiers != null) declarationSpecifiers.setParent(this);
        nodeOptional = n1;
        if (nodeOptional != null) nodeOptional.setParent(this);
        nodeToken = new NodeToken(";");
        if (nodeToken != null) nodeToken.setParent(this);
    }

    public void accept(org.codemon.insertion.c.parser.visitor.Visitor v) {
        v.visit(this);
    }

    public <R, A> R accept(org.codemon.insertion.c.parser.visitor.GJVisitor<R, A> v, A argu) {
        return v.visit(this, argu);
    }

    public <R> R accept(org.codemon.insertion.c.parser.visitor.GJNoArguVisitor<R> v) {
        return v.visit(this);
    }

    public <A> void accept(org.codemon.insertion.c.parser.visitor.GJVoidVisitor<A> v, A argu) {
        v.visit(this, argu);
    }

    public void setParent(Node n) {
        parent = n;
    }

    public Node getParent() {
        return parent;
    }
}
