package com.vertigrated.db;

import com.vertigrated.text.StringUtil;

/**
 * Basic generalized implementation of DeleteBuiler.
 * This should generate clean ANSI-SQL99 delete statements.
 * Vendor specific implementations can inherit from this and override
 * the getCommand() method to customize or specialize its behavior.
 */
public abstract class AbstractDeleteBuilder extends WhereClause implements DeleteBuilder {

    private final String schema;

    private final String table;

    protected AbstractDeleteBuilder(final String schema, final String table) {
        this.schema = schema;
        this.table = table;
    }

    public String getCommand() {
        final StringBuilder sql = new StringBuilder(1000);
        sql.append("DELETE FROM ");
        if (!StringUtil.isNullOrEmptyString(this.schema)) {
            sql.append(this.schema).append(".");
        }
        sql.append(this.table);
        sql.append(super.toString());
        return sql.toString();
    }

    public String toString() {
        return this.getCommand();
    }
}
