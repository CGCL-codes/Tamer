package org.incava.java;

public class ASTName extends SimpleNode {

    public ASTName(int id) {
        super(id);
    }

    public ASTName(JavaParser p, int id) {
        super(p, id);
    }

    /** Accept the visitor. **/
    public Object jjtAccept(JavaParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
