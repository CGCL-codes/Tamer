package com.ecmdeveloper.plugin.search.model;

import com.ecmdeveloper.plugin.search.model.constants.QueryContainerType;

/**
 * 
 * @author ricardo.belfor
 *
 */
public abstract class QueryContainer extends QueryDiagram {

    public QueryContainer(Query query) {
        super(query);
    }

    private static final long serialVersionUID = 1L;

    public String toSQL() {
        if (getChildren().size() == 0) {
            return "";
        }
        StringBuffer sql = new StringBuffer();
        String prefix = getOperationPrefix();
        if (prefix != null) {
            sql.append(prefix);
            sql.append(" ");
        }
        String concat = "(";
        for (QueryElement queryElement : getChildren()) {
            String childSql = queryElement.toSQL();
            if (!childSql.isEmpty()) {
                sql.append(concat);
                sql.append(childSql);
                concat = " " + getConcatOperation() + " ";
            }
        }
        if (sql.length() != 0) {
            sql.append(")");
        }
        return sql.toString();
    }

    protected abstract String getOperationPrefix();

    protected abstract String getConcatOperation();

    public abstract QueryContainerType getType();
}
