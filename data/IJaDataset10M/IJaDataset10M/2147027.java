package org.gdbms.parser;

public class ASTSQLTableList extends SimpleNode {

    public ASTSQLTableList(int id) {
        super(id);
    }

    public ASTSQLTableList(SQLEngine p, int id) {
        super(p, id);
    }

    /** Accept the visitor. **/
    public Object jjtAccept(SQLEngineVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
