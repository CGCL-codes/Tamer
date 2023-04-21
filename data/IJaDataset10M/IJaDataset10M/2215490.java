package uk.ac.warwick.dcs.cokefolk.parser.syntaxtree;

/**
 * Grammar production:
 * dyadicIntervalBoolOpName -> DyadicIntervalBoolOpName()
 * intervalExp -> IntervalExp()
 */
public class DyadicIntervalAll implements Node {

    public DyadicIntervalBoolOpName dyadicIntervalBoolOpName;

    public IntervalExp intervalExp;

    public DyadicIntervalAll(DyadicIntervalBoolOpName n0, IntervalExp n1) {
        dyadicIntervalBoolOpName = n0;
        intervalExp = n1;
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
