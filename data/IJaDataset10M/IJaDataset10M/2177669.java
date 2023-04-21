package uk.ac.warwick.dcs.cokefolk.parser.syntaxtree;

/**
 * Grammar production:
 * nodeToken -> <CONSTRAINT>
 * boolExp -> BoolExp()
 */
public class PossrepConstraintDef implements Node {

    public NodeToken nodeToken;

    public BoolExp boolExp;

    public PossrepConstraintDef(NodeToken n0, BoolExp n1) {
        nodeToken = n0;
        boolExp = n1;
    }

    public PossrepConstraintDef(BoolExp n0) {
        nodeToken = new NodeToken("CONSTRAINT");
        boolExp = n0;
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
