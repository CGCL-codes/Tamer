package uk.ac.warwick.dcs.cokefolk.parser.syntaxtree;

/**
 * Grammar production:
 * possrepDef -> PossrepDef()
 * nodeListOptional -> ( PossrepDef() )*
 */
public class PossrepDefList implements Node {

    public PossrepDef possrepDef;

    public NodeListOptional nodeListOptional;

    public PossrepDefList(PossrepDef n0, NodeListOptional n1) {
        possrepDef = n0;
        nodeListOptional = n1;
    }

    public void accept(uk.ac.warwick.dcs.cokefolk.parser.visitor.Visitor v) {
        v.visit(this);
    }

    public <R, A> R accept(uk.ac.warwick.dcs.cokefolk.parser.visitor.GJVisitor<R, A> v, A argu) {
        return v.visit(this, argu);
    }

    public <R> R accept(uk.ac.warwick.dcs.cokefolk.parser.visitor.GJNoArguVisitor<R> v) {
        return v.visit(this);
    }

    public <A> void accept(uk.ac.warwick.dcs.cokefolk.parser.visitor.GJVoidVisitor<A> v, A argu) {
        v.visit(this, argu);
    }
}
