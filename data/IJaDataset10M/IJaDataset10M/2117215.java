package org.formaria.oracle.forms.plsql.parser;

import org.formaria.oracle.forms.plsql.visitors.PlSqlNodeVisitor;

public class ASTOpenStatement extends SimpleNode {

    private String name = null;

    public ASTOpenStatement(int id) {
        super(id);
    }

    public ASTOpenStatement(FormsPlSql p, int id) {
        super(p, id);
    }

    public String getName() {
        return name;
    }

    public void setName(String aName) {
        name = aName;
    }

    public Object accept(PlSqlNodeVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
