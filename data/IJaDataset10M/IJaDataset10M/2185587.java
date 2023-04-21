package com.mycila.testing.plugin.db;

import com.mycila.testing.plugin.db.api.SqlColumn;
import com.mycila.testing.plugin.db.api.SqlColumnHeader;
import com.mycila.testing.plugin.db.api.SqlData;
import com.mycila.testing.plugin.db.api.SqlResults;
import java.util.AbstractList;
import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class SqlColumnImpl implements SqlColumn {

    private final SqlResults results;

    private final SqlColumnHeader columnHeader;

    private final List<SqlData> rows;

    SqlColumnImpl(SqlResults results, SqlColumnHeader columnHeader) {
        this.results = results;
        this.columnHeader = columnHeader;
        this.rows = new AbstractList<SqlData>() {

            @Override
            public SqlData get(int index) {
                return row(index);
            }

            @Override
            public int size() {
                return rowCount();
            }
        };
    }

    public int index() {
        return columnHeader.index();
    }

    public SqlColumnHeader header() {
        return columnHeader;
    }

    public int rowCount() {
        return results.rowCount();
    }

    public SqlData row(int index) {
        return results.data(index, columnHeader.index());
    }

    public List<SqlData> rows() {
        return rows;
    }

    @Override
    public String toString() {
        return columnHeader.name() + " : " + rows;
    }
}
