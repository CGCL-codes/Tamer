package uk.ac.warwick.dcs.cokefolk.parser.syntaxtree;

/**
 * Grammar production:
 * nodeChoice -> VarRef()
 *       | UserOpInv()
 *       | THE_OpInv()
 *       | AttributeExtractorInv()
 *       | NadicCommonJoin()
 *       | CommonExtend()
 *       | CommonSubstitute()
 *       | CommonTreat()
 */
public class CommonHead implements Node {

    public NodeChoice nodeChoice;

    public CommonHead(NodeChoice n0) {
        nodeChoice = n0;
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
