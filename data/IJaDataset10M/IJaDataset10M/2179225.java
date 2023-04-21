package org.sourceforge.zlang.model.parser;

public class ASTArgumentList extends SimpleNode {

    public ASTArgumentList(int id) {
        super(id);
    }

    public ASTArgumentList(JavaParser p, int id) {
        super(p, id);
    }

    /** Accept the visitor. **/
    public Object jjtAccept(JavaParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
