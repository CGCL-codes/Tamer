package uk.ac.warwick.dcs.cokefolk.parser.syntaxtree;

/**
 * Grammar production:
 * nodeToken -> <EXTEND>
 * tupleExp -> TupleExp()
 * nodeToken1 -> <ADD>
 * nodeToken2 -> "("
 * extendAddCommalist -> ExtendAddCommalist()
 * nodeToken3 -> ")"
 */
public class TupleExtend implements Node {

    public NodeToken nodeToken;

    public TupleExp tupleExp;

    public NodeToken nodeToken1;

    public NodeToken nodeToken2;

    public ExtendAddCommalist extendAddCommalist;

    public NodeToken nodeToken3;

    public TupleExtend(NodeToken n0, TupleExp n1, NodeToken n2, NodeToken n3, ExtendAddCommalist n4, NodeToken n5) {
        nodeToken = n0;
        tupleExp = n1;
        nodeToken1 = n2;
        nodeToken2 = n3;
        extendAddCommalist = n4;
        nodeToken3 = n5;
    }

    public TupleExtend(TupleExp n0, ExtendAddCommalist n1) {
        nodeToken = new NodeToken("EXTEND");
        tupleExp = n0;
        nodeToken1 = new NodeToken("ADD");
        nodeToken2 = new NodeToken("(");
        extendAddCommalist = n1;
        nodeToken3 = new NodeToken(")");
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
