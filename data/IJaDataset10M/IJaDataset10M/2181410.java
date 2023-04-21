package com.vertigrated.db;

import com.vertigrated.text.StringUtil;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * This object represents basic useful information about a database table
 */
public class Table implements Comparable<Table> {

    private final String schema;

    private final String tableName;

    private final List<Column> columns;

    Table(final String schema, final String tableName, final List<Column> columns) {
        this.schema = StringUtil.nullToEmptyString(schema);
        this.tableName = tableName;
        this.columns = Collections.unmodifiableList(columns);
    }

    public String getTableName() {
        return tableName;
    }

    public String getSchema() {
        return schema;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public int compareTo(final Table t) {
        return this.tableName.compareTo(t.tableName);
    }

    public String toString() {
        final StringBuilder sb = new StringBuilder(1024);
        sb.append("{").append("table").append(",");
        sb.append(this.tableName).append(",[");
        final Iterator<Column> iter = this.columns.iterator();
        while (iter.hasNext()) {
            sb.append(iter.next());
            if (iter.hasNext()) {
                sb.append(",");
            }
        }
        sb.append("]}");
        return sb.toString();
    }
}
