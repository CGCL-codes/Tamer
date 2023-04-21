package kpython;

public class ASTListDisplay extends KPythonNode {

    public ASTListDisplay(int id) {
        super(id);
    }

    public ASTListDisplay(Parser p, int id) {
        super(p, id);
    }

    /** Accept the visitor. **/
    public Object jjtAccept(ParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
