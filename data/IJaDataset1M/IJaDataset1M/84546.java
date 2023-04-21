package org.codemon.insertion.c.parser.syntaxtree;

/**
 * Grammar production:
 * typeSpecifier -> TypeSpecifier()
 * nodeOptional -> [ TypeSpecifierCycle() ]
 * nodeOptional1 -> [ TypeQualifierCycle() ]
 * nodeOptional2 -> [ StorageClassSpecifierCycle() ]
 */
public class TypeSpecifierCycle implements Node {

    private Node parent;

    public TypeSpecifier typeSpecifier;

    public NodeOptional nodeOptional;

    public NodeOptional nodeOptional1;

    public NodeOptional nodeOptional2;

    public TypeSpecifierCycle(TypeSpecifier n0, NodeOptional n1, NodeOptional n2, NodeOptional n3) {
        typeSpecifier = n0;
        if (typeSpecifier != null) typeSpecifier.setParent(this);
        nodeOptional = n1;
        if (nodeOptional != null) nodeOptional.setParent(this);
        nodeOptional1 = n2;
        if (nodeOptional1 != null) nodeOptional1.setParent(this);
        nodeOptional2 = n3;
        if (nodeOptional2 != null) nodeOptional2.setParent(this);
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
