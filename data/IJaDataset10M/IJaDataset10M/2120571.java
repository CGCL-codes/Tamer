package verilogx.syntaxtree;

/**
 * Grammar production:
 * f0 -> "initial"
 * f1 -> <IDENTIFIER>
 * f2 -> "="
 * f3 -> <INIT_VAL>
 */
public class udp_initial_statement implements Node {

    public NodeToken f0;

    public NodeToken f1;

    public NodeToken f2;

    public NodeToken f3;

    public udp_initial_statement(NodeToken n0, NodeToken n1, NodeToken n2, NodeToken n3) {
        f0 = n0;
        f1 = n1;
        f2 = n2;
        f3 = n3;
    }

    public udp_initial_statement(NodeToken n0, NodeToken n1) {
        f0 = new NodeToken("initial");
        f1 = n0;
        f2 = new NodeToken("=");
        f3 = n1;
    }

    public void accept(verilogx.visitor.Visitor v) {
        v.visit(this);
    }

    public Object accept(verilogx.visitor.ObjectVisitor v, Object argu) {
        return v.visit(this, argu);
    }
}
