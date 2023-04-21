package uk.ac.warwick.dcs.cokefolk.parser.syntaxtree;

/**
 * Grammar production:
 * nodeToken -> <UNGROUP>
 * nodeToken1 -> "("
 * ungroupingCommalist -> UngroupingCommalist()
 * nodeToken2 -> ")"
 */
public class Ungroup implements Node {

    public NodeToken nodeToken;

    public NodeToken nodeToken1;

    public UngroupingCommalist ungroupingCommalist;

    public NodeToken nodeToken2;

    public Ungroup(NodeToken n0, NodeToken n1, UngroupingCommalist n2, NodeToken n3) {
        nodeToken = n0;
        nodeToken1 = n1;
        ungroupingCommalist = n2;
        nodeToken2 = n3;
    }

    public Ungroup(UngroupingCommalist n0) {
        nodeToken = new NodeToken("UNGROUP");
        nodeToken1 = new NodeToken("(");
        ungroupingCommalist = n0;
        nodeToken2 = new NodeToken(")");
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
