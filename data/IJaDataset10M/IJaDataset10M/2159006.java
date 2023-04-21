package x.sql2.queries;

import x.sql2.SQLColumn;
import x.sql2.interfaces.SQLStatement;

public class SQLStatementInsert extends SQLStatement {

    public SQLStatementInsert(String tableName) {
        super(tableName);
    }

    @Override
    public String generateQuery() {
        StringBuilder cols = new StringBuilder();
        StringBuilder values = new StringBuilder();
        StringBuilder ret = new StringBuilder();
        for (SQLColumn c : getColumns()) {
            if (c.isPrimaryKey() || !c.isInsertable()) {
                continue;
            }
            if (cols.length() > 0) {
                cols.append(",");
                values.append(",");
            }
            if (ret.length() > 0) {
                ret.append(",");
            }
            cols.append(c.getColumnName());
            values.append(c.getFormatedColumnName());
            if (c.isReturning()) {
                if (ret.length() == 0) {
                    ret.append(" ").append(getReturning());
                }
                ret.append(c.getColumnName());
            }
        }
        return "INSERT INTO " + getTableName() + " (" + cols + ") VALUES (" + values + ")" + ret + ";";
    }
}
