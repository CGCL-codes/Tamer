package verilogx.syntaxtree;

/**
 * Grammar production:
 * f0 -> level_input_list()
 * f1 -> <COLON>
 * f2 -> <OUTPUT_SYMBOL>
 * f3 -> <SEMICOLON>
 */
public class combinational_entry implements Node {

    public level_input_list f0;

    public NodeToken f1;

    public NodeToken f2;

    public NodeToken f3;

    public combinational_entry(level_input_list n0, NodeToken n1, NodeToken n2, NodeToken n3) {
        f0 = n0;
        f1 = n1;
        f2 = n2;
        f3 = n3;
    }

    public combinational_entry(level_input_list n0, NodeToken n1) {
        f0 = n0;
        f1 = new NodeToken(":");
        f2 = n1;
        f3 = new NodeToken(";");
    }

    public void accept(verilogx.visitor.Visitor v) {
        v.visit(this);
    }

    public Object accept(verilogx.visitor.ObjectVisitor v, Object argu) {
        return v.visit(this, argu);
    }
}
