package datadog.sql.statements;

public abstract class SqlStatement {

    private String sql;

    public SqlStatement(String sql) {
        this.sql = sql;
    }

    public String toString() {
        return sql;
    }
}
