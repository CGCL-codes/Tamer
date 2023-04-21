package org.gdbms.parser;

public class ASTSQLUnaryExpr extends SimpleNode {

    public ASTSQLUnaryExpr(int id) {
        super(id);
    }

    public ASTSQLUnaryExpr(SQLEngine p, int id) {
        super(p, id);
    }

    /** Accept the visitor. **/
    public Object jjtAccept(SQLEngineVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
