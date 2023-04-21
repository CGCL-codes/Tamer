package uk.ac.warwick.dcs.cokefolk.parser.syntaxtree;

/**
 * Grammar production:
 * nodeToken -> <UNION>
 * relationExp -> RelationExp()
 */
public class DyadicUnion implements Node {

    public NodeToken nodeToken;

    public RelationExp relationExp;

    public DyadicUnion(NodeToken n0, RelationExp n1) {
        nodeToken = n0;
        relationExp = n1;
    }

    public DyadicUnion(RelationExp n0) {
        nodeToken = new NodeToken("UNION");
        relationExp = n0;
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
