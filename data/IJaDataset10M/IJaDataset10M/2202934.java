package verilogx.syntaxtree;

/**
 * Grammar production:
 * f0 -> <LPAREN> "small" <RPAREN>
 *       | <LPAREN> "medium" <RPAREN>
 *       | <LPAREN> "large" <RPAREN>
 */
public class charge_strength implements Node {

    public NodeChoice f0;

    public charge_strength(NodeChoice n0) {
        f0 = n0;
    }

    public void accept(verilogx.visitor.Visitor v) {
        v.visit(this);
    }

    public Object accept(verilogx.visitor.ObjectVisitor v, Object argu) {
        return v.visit(this, argu);
    }
}
