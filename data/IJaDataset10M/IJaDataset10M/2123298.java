package databaseVersionControl.domain.db.sqlBuilder;

import databaseVersionControl.domain.dialect.Dialect;

public class DropTable implements SqlBuilder {

    private String _tableName;

    public DropTable(String tableName) {
        this.setTableName(tableName);
    }

    private void setTableName(String tableName) {
        if (tableName == null || tableName.trim().isEmpty()) throw new IllegalArgumentException("Invalid table name");
        _tableName = tableName.trim().toUpperCase();
    }

    @Override
    public String buildSql(Dialect dialect) {
        return String.format("DROP TABLE %s", _tableName);
    }
}
