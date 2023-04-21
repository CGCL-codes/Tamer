package org.incava.java;

public class ASTClassBody extends SimpleNode {

    public ASTClassBody(int id) {
        super(id);
    }

    public ASTClassBody(JavaParser p, int id) {
        super(p, id);
    }

    /** Accept the visitor. **/
    public Object jjtAccept(JavaParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
