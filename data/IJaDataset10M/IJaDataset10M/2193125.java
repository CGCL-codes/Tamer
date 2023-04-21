package alf2java.papyrus.parser.alfparser;

public class ASTEqualExp extends SimpleNode {

    public ASTEqualExp(int id) {
        super(id);
    }

    public ASTEqualExp(Grammaire p, int id) {
        super(p, id);
    }

    /** Accept the visitor. **/
    public Object jjtAccept(GrammaireVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
