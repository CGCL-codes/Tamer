package org.incava.java;

public class ASTFormalParameters extends SimpleNode {

    public ASTFormalParameters(int id) {
        super(id);
    }

    public ASTFormalParameters(JavaParser p, int id) {
        super(p, id);
    }

    /** Accept the visitor. **/
    public Object jjtAccept(JavaParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
