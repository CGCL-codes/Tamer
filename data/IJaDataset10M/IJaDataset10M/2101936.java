package com.infoether.pmd.ast;

public class ASTTypeDeclaration extends SimpleNode {

    public ASTTypeDeclaration(int id) {
        super(id);
    }

    public ASTTypeDeclaration(JavaParser p, int id) {
        super(p, id);
    }

    /** Accept the visitor. **/
    public Object jjtAccept(JavaParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
