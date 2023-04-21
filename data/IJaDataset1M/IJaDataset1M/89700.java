package verilogx.syntaxtree;

/**
 * Grammar production:
 * f0 -> "#" delay_value()
 *       | "#" <LPAREN> delay_value() [ delay_value() <COMMA> delay_value() ] <RPAREN>
 */
public class delay2 implements Node {

    public NodeChoice f0;

    public delay2(NodeChoice n0) {
        f0 = n0;
    }

    public void accept(verilogx.visitor.Visitor v) {
        v.visit(this);
    }

    public Object accept(verilogx.visitor.ObjectVisitor v, Object argu) {
        return v.visit(this, argu);
    }
}
