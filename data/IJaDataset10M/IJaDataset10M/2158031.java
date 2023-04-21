package de.axbench.axlang.text.syntaxtree;

/**
 * Grammar production:
 * f0 -> [ <OPTIONAL> ]
 * f1 -> <DIRECTION>
 * f2 -> "<"
 * f3 -> ( <HW_BUSTYPE> | <ANALOG> | <DIGITAL> )
 * f4 -> ">"
 * f5 -> <IDENTIFIER>
 * f6 -> ( "," <IDENTIFIER> )*
 * f7 -> [ Attributes() ]
 * f8 -> ";"
 */
public class HWPort implements Node {

    public NodeOptional f0;

    public NodeToken f1;

    public NodeToken f2;

    public NodeChoice f3;

    public NodeToken f4;

    public NodeToken f5;

    public NodeListOptional f6;

    public NodeOptional f7;

    public NodeToken f8;

    public HWPort(NodeOptional n0, NodeToken n1, NodeToken n2, NodeChoice n3, NodeToken n4, NodeToken n5, NodeListOptional n6, NodeOptional n7, NodeToken n8) {
        f0 = n0;
        f1 = n1;
        f2 = n2;
        f3 = n3;
        f4 = n4;
        f5 = n5;
        f6 = n6;
        f7 = n7;
        f8 = n8;
    }

    public HWPort(NodeOptional n0, NodeToken n1, NodeChoice n2, NodeToken n3, NodeListOptional n4, NodeOptional n5) {
        f0 = n0;
        f1 = n1;
        f2 = new NodeToken("<");
        f3 = n2;
        f4 = new NodeToken(">");
        f5 = n3;
        f6 = n4;
        f7 = n5;
        f8 = new NodeToken(";");
    }

    public void accept(de.axbench.axlang.text.visitor.Visitor v) {
        v.visit(this);
    }

    public <R, A> R accept(de.axbench.axlang.text.visitor.GJVisitor<R, A> v, A argu) {
        return v.visit(this, argu);
    }

    public <R> R accept(de.axbench.axlang.text.visitor.GJNoArguVisitor<R> v) {
        return v.visit(this);
    }

    public <A> void accept(de.axbench.axlang.text.visitor.GJVoidVisitor<A> v, A argu) {
        v.visit(this, argu);
    }
}
