package com.fairbait.sybase.model;

import com.fairbait.sybase.model.visitor.*;

public final class SqlTruncateStatement extends AbstractSqlStatement {

    private SqlTableReference tableRef;

    public SqlTruncateStatement(final SqlTableReference tableRef) {
        setTableReference(tableRef);
    }

    public SqlTableReference getTableReference() {
        return tableRef;
    }

    public void setTableReference(final SqlTableReference tableRef) {
        tableRef.setParent(this);
        this.tableRef = tableRef;
    }

    public boolean setSqlText(final String sqlText) {
        final SqlTruncateStatement i = parseTruncateStatement(sqlText);
        if (i == null) return false;
        return copyFrom(i);
    }

    public boolean copyFrom(final ISqlItem item) {
        if (!(item instanceof SqlTruncateStatement)) return false;
        final SqlTruncateStatement from = (SqlTruncateStatement) item;
        tableRef = from.tableRef;
        return true;
    }

    public static SqlTruncateStatement parseTruncateStatement(final String sqlText) {
        return new SqlItemParserVisitor(sqlText).parseTruncateStatement();
    }

    public void accept(final ISqlItemVisitor visitor) {
        visitor.visit(this);
    }

    public SqlItemGroupType getSqlItemGroupType() {
        return ISqlItem.SqlItemGroupType.SQL_STATEMENT;
    }

    public SqlItemType getSqlItemType() {
        return ISqlItem.SqlItemType.SqlTruncateStatement;
    }
}
