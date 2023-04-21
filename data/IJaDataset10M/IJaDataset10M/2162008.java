package org.formaria.oracle.forms.plsql.parser;

import org.formaria.oracle.forms.plsql.visitors.PlSqlNodeVisitor;

public class ASTFunctionBody extends SimpleNode {

    public ASTFunctionBody(int id) {
        super(id);
    }

    public ASTFunctionBody(FormsPlSql p, int id) {
        super(p, id);
    }

    public Object accept(PlSqlNodeVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
