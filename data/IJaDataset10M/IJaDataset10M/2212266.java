package kpython;

public class ASTLiteralFloat extends KPythonNode {

    public ASTLiteralFloat(int id) {
        super(id);
    }

    public ASTLiteralFloat(Parser p, int id) {
        super(p, id);
    }

    /** Accept the visitor. **/
    public Object jjtAccept(ParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

    public void setValue(Token t) {
        this.name = t.image;
    }

    public String getValue() {
        return this.name;
    }

    private String name;
}
