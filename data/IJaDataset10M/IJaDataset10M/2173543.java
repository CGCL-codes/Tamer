package com.infoether.pmd.ast;

public class ASTFieldDeclaration extends SimpleNode {

    public ASTFieldDeclaration(int id) {
        super(id);
    }

    public ASTFieldDeclaration(JavaParser p, int id) {
        super(p, id);
    }

    /** Accept the visitor. **/
    public Object jjtAccept(JavaParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
