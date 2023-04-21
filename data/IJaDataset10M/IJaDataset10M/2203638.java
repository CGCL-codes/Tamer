package org.formaria.oracle.forms.plsql.parser;

import org.formaria.oracle.forms.plsql.visitors.PlSqlNodeVisitor;

public class ASTSelectItem extends SimpleNode {

    private String name = null;

    private boolean hasStar = false;

    private boolean hasName = false;

    public ASTSelectItem(int id) {
        super(id);
    }

    public ASTSelectItem(FormsPlSql p, int id) {
        super(p, id);
    }

    public String getName() {
        return name;
    }

    public void setName(String aName) {
        name = aName;
    }

    public boolean hasStar() {
        return hasStar;
    }

    public void setStar(boolean state) {
        hasStar = state;
    }

    public boolean hasName() {
        return hasName;
    }

    public void setName(boolean state) {
        hasName = state;
    }

    public Object accept(PlSqlNodeVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
