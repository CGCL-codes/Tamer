package x.sql2.queries;

import x.sql2.SQLColumn;
import x.sql2.interfaces.SQLStatement;

public class SQLStatementUpdate extends SQLStatement {

    public SQLStatementUpdate(String tableName) {
        super(tableName);
    }

    @Override
    public String generateQuery() {
        StringBuilder cols = new StringBuilder();
        if (getColumns() != null) {
            for (SQLColumn c : getColumns()) {
                if (!c.isUpdatable()) {
                    continue;
                }
                if (cols.length() > 0) {
                    cols.append(",");
                }
                cols.append(c.format("="));
            }
        }
        StringBuilder query = new StringBuilder();
        query.append("UPDATE ").append(tableName).append(" SET ").append(cols);
        if (getWhere() != null) {
            query.append(" WHERE ").append(getWhere());
        }
        return query.toString();
    }
}
