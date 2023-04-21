package com.papyrus.alf2java.parser.alfparser;

public class ASTAssignmentExpressionCompletion extends SimpleNode {

    public ASTAssignmentExpressionCompletion(int id) {
        super(id);
    }

    public ASTAssignmentExpressionCompletion(Grammaire p, int id) {
        super(p, id);
    }

    /** Accept the visitor. **/
    public Object jjtAccept(GrammaireVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
