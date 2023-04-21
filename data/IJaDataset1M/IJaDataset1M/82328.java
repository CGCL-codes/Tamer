package alf2java.papyrus.parser.alfparser;

public class ASTsi extends SimpleNode {

    public ASTsi(int id) {
        super(id);
    }

    public ASTsi(Grammaire p, int id) {
        super(p, id);
    }

    /** Accept the visitor. **/
    public Object jjtAccept(GrammaireVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
