package verilogx.syntaxtree;

/**
 * Grammar production:
 * f0 -> <UNSIGNED_NUMBER>
 *       | <REAL_NUMBER>
 *       | <INIT_VAL>
 */
public class number implements Node {

    public NodeChoice f0;

    public number(NodeChoice n0) {
        f0 = n0;
    }

    public void accept(verilogx.visitor.Visitor v) {
        v.visit(this);
    }

    public Object accept(verilogx.visitor.ObjectVisitor v, Object argu) {
        return v.visit(this, argu);
    }
}
