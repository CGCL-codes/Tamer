package org.brandao.jcptbr.mapping;

/**
 *
 * @author Brandao
 */
public class Check {

    private Table table;

    private Column column;

    private String expression;

    public boolean isColumnLevel() {
        return getColumn() != null;
    }

    public boolean isTableLevel() {
        return getTable() != null;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public Column getColumn() {
        return column;
    }

    public void setColumn(Column column) {
        this.column = column;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }
}
