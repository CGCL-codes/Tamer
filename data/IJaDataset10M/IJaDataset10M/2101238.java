package org.openware.job.servlet;

import org.openware.job.data.TableRow;
import org.openware.job.data.Query;
import java.io.Serializable;

public class FindByArguments implements Serializable {

    private TableRow tableRow = null;

    private Query query = null;

    private String[] joinTables = null;

    private String whereClause = null;

    private boolean escapeWhereClause = true;

    public FindByArguments(TableRow tableRow, Query query) {
        this.tableRow = tableRow;
        this.query = query;
    }

    TableRow getTableRow() {
        return tableRow;
    }

    Query getQuery() {
        return query;
    }
}
