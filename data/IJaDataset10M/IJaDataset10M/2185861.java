package org.sourceforge.zlang.model.parser;

public class ASTEmptyStatement extends SimpleNode {

    public ASTEmptyStatement(int id) {
        super(id);
    }

    public ASTEmptyStatement(JavaParser p, int id) {
        super(p, id);
    }

    /** Accept the visitor. **/
    public Object jjtAccept(JavaParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
