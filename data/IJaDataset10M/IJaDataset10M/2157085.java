package org.formaria.oracle.forms.plsql.parser;

import org.formaria.oracle.forms.plsql.visitors.PlSqlNodeVisitor;

public class ASTPlSqlLikeClause extends SimpleNode {

    public ASTPlSqlLikeClause(int id) {
        super(id);
    }

    public ASTPlSqlLikeClause(FormsPlSql p, int id) {
        super(p, id);
    }

    public Object accept(PlSqlNodeVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
