package org.formaria.oracle.forms.plsql.parser;

import org.formaria.oracle.forms.plsql.visitors.PlSqlNodeVisitor;

public class ASTPlSqlExpression extends SimpleNode {

    public ASTPlSqlExpression(int id) {
        super(id);
    }

    public ASTPlSqlExpression(FormsPlSql p, int id) {
        super(p, id);
    }

    public Object accept(PlSqlNodeVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
