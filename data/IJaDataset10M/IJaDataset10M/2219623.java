package org.gdbms.parser;

public class ASTSQLLiteral extends SimpleNode {

    public ASTSQLLiteral(int id) {
        super(id);
    }

    public ASTSQLLiteral(SQLEngine p, int id) {
        super(p, id);
    }

    /** Accept the visitor. **/
    public Object jjtAccept(SQLEngineVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
