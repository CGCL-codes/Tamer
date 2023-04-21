package syntaxtree;

/**
 * Grammar production:
 * f0 -> "LB"
 *       | "LBU"
 *       | "LH"
 *       | "LHU"
 *       | "LW"
 */
public class OpLoad implements Node {

    public NodeChoice f0;

    public OpLoad(NodeChoice n0) {
        f0 = n0;
    }

    public void accept(visitor.Visitor v) {
        v.visit(this);
    }

    public <R, A> R accept(visitor.GJVisitor<R, A> v, A argu) {
        return v.visit(this, argu);
    }

    public <R> R accept(visitor.GJNoArguVisitor<R> v) {
        return v.visit(this);
    }

    public <A> void accept(visitor.GJVoidVisitor<A> v, A argu) {
        v.visit(this, argu);
    }
}
