package ru.moskvin;

public class ASTMethodId extends SimpleNode {

    String name;

    public ASTMethodId(int id) {
        super(id);
    }

    public ASTMethodId(NMLParser p, int id) {
        super(p, id);
    }

    /**
     * Accept the visitor. *
     */
    public Object jjtAccept(NMLParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
