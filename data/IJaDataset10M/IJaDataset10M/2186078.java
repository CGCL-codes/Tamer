package com.infoether.pmd.ast;

public class ASTPackageDeclaration extends SimpleNode {

    public ASTPackageDeclaration(int id) {
        super(id);
    }

    public ASTPackageDeclaration(JavaParser p, int id) {
        super(p, id);
    }

    /** Accept the visitor. **/
    public Object jjtAccept(JavaParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
