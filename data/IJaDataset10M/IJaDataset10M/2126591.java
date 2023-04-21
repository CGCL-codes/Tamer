package org.sourceforge.zlang.model.parser;

public class ASTUnmodifiedClassDeclaration extends SimpleNode {

    public ASTUnmodifiedClassDeclaration(int id) {
        super(id);
    }

    public ASTUnmodifiedClassDeclaration(JavaParser p, int id) {
        super(p, id);
    }

    /** Accept the visitor. **/
    public Object jjtAccept(JavaParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
