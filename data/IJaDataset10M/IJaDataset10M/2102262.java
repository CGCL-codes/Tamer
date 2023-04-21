package uk.ac.warwick.dcs.cokefolk.parser.syntaxtree;

/**
 * Grammar production:
 * nodeToken -> <AMINUS>
 * integerFactor -> IntegerFactor()
 */
public class NegatedIntegerExp implements Node {

    public NodeToken nodeToken;

    public IntegerFactor integerFactor;

    public NegatedIntegerExp(NodeToken n0, IntegerFactor n1) {
        nodeToken = n0;
        integerFactor = n1;
    }

    public NegatedIntegerExp(IntegerFactor n0) {
        nodeToken = new NodeToken("-");
        integerFactor = n0;
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
