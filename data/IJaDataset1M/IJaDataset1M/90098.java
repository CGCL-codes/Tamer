package jde.parser.syntaxtree;

/**
 * Grammar production:
 * <PRE>
 * f0 -> VariableDeclaratorId()
 * f1 -> [ "=" VariableInitializer() ]
 * </PRE>
 */
public class VariableDeclarator implements Node {

    public VariableDeclaratorId f0;

    public NodeOptional f1;

    public VariableDeclarator(VariableDeclaratorId n0, NodeOptional n1) {
        f0 = n0;
        f1 = n1;
    }

    public void accept(jde.parser.visitor.Visitor v) {
        v.visit(this);
    }
}
