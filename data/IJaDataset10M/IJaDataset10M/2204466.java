package com.fairbait.sybase.model;

import com.fairbait.sybase.model.visitor.*;

public final class SqlCommitTransactionStatement extends AbstractSqlStatement {

    private String transactionName;

    public SqlCommitTransactionStatement() {
        setTransactionName("");
    }

    public SqlCommitTransactionStatement(final String transactionName) {
        setTransactionName(transactionName);
    }

    public String getTransactionName() {
        return transactionName;
    }

    public void setTransactionName(final String transactionName) {
        this.transactionName = transactionName;
    }

    public SqlItemGroupType getSqlItemGroupType() {
        return ISqlItem.SqlItemGroupType.SQL_STATEMENT;
    }

    public SqlItemType getSqlItemType() {
        return ISqlItem.SqlItemType.SqlCommitTransactionStatement;
    }

    public boolean setSqlText(final String sqlText) {
        final SqlCommitTransactionStatement i = parseCommitTransactionStatement(sqlText);
        if (i == null) return false;
        return copyFrom(i);
    }

    public boolean copyFrom(final ISqlItem item) {
        if (!(item instanceof SqlCommitTransactionStatement)) return false;
        final SqlCommitTransactionStatement from = (SqlCommitTransactionStatement) item;
        transactionName = from.transactionName;
        return true;
    }

    public static SqlCommitTransactionStatement parseCommitTransactionStatement(final String sqlText) {
        return new SqlItemParserVisitor(sqlText).parseCommitTransactionStatement();
    }

    public void accept(final ISqlItemVisitor visitor) {
        visitor.visit(this);
    }
}
