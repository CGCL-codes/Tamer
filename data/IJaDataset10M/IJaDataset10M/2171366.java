package org.incava.java;

public class ASTExplicitConstructorInvocation extends SimpleNode {

    public ASTExplicitConstructorInvocation(int id) {
        super(id);
    }

    public ASTExplicitConstructorInvocation(JavaParser p, int id) {
        super(p, id);
    }

    /** Accept the visitor. **/
    public Object jjtAccept(JavaParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
