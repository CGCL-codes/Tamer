package verilogx.syntaxtree;

/**
 * Grammar production:
 * f0 -> <OUTPUT_SYMBOL>
 *       | <DASH>
 */
public class next_state implements Node {

    public NodeChoice f0;

    public next_state(NodeChoice n0) {
        f0 = n0;
    }

    public void accept(verilogx.visitor.Visitor v) {
        v.visit(this);
    }

    public Object accept(verilogx.visitor.ObjectVisitor v, Object argu) {
        return v.visit(this, argu);
    }
}
