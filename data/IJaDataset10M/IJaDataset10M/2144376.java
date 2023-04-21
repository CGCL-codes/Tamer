package verilogx.syntaxtree;

/**
 * Grammar production:
 * f0 -> path_delay_expression()
 * f1 -> ( <COMMA> path_delay_expression() )*
 */
public class list_of_path_delay_expressions implements Node {

    public path_delay_expression f0;

    public NodeListOptional f1;

    public list_of_path_delay_expressions(path_delay_expression n0, NodeListOptional n1) {
        f0 = n0;
        f1 = n1;
    }

    public void accept(verilogx.visitor.Visitor v) {
        v.visit(this);
    }

    public Object accept(verilogx.visitor.ObjectVisitor v, Object argu) {
        return v.visit(this, argu);
    }
}
