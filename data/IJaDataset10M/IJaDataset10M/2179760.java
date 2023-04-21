package com.infoether.pmd.ast;

public class ASTSwitchLabel extends SimpleNode {

    public ASTSwitchLabel(int id) {
        super(id);
    }

    public ASTSwitchLabel(JavaParser p, int id) {
        super(p, id);
    }

    /** Accept the visitor. **/
    public Object jjtAccept(JavaParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
