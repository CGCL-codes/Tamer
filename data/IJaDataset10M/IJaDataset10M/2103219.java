package org.formaria.oracle.forms.plsql.parser;

import org.formaria.oracle.forms.plsql.visitors.PlSqlNodeVisitor;

public class ASTFunctionCall extends SimpleNode {

    private boolean hasDistinct = false;

    private boolean hasAll = false;

    private boolean hasStar = false;

    public ASTFunctionCall(int id) {
        super(id);
    }

    public ASTFunctionCall(FormsPlSql p, int id) {
        super(p, id);
    }

    public boolean hasDistinct() {
        return hasDistinct;
    }

    public void setDistinct(boolean state) {
        hasDistinct = state;
    }

    public boolean hasAll() {
        return hasAll;
    }

    public void setAll(boolean state) {
        hasAll = state;
    }

    public boolean hasStar() {
        return hasStar;
    }

    public void setStar(boolean state) {
        hasStar = state;
    }

    public Object accept(PlSqlNodeVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
