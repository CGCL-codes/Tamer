package it.eg.sloth.db.query.pagedQuery;

import it.eg.sloth.db.datasource.DataTable;
import it.eg.sloth.db.query.filteredQuery.FilteredQuery;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

/**
 * 
 * @author Enrico Grillini
 * 
 */
public class PagedQuery extends FilteredQuery implements PagedQueryInterface {

    private static final long serialVersionUID = 1L;

    private String pagedQuery;

    private int start;

    private int end;

    public PagedQuery(String statement) {
        super(statement);
    }

    protected String getFilteredStatement() {
        return pagedQuery;
    }

    public PreparedStatement getPreparedStatement(Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(getFilteredStatement());
        int i = addValues(statement, 1);
        if (start != -1) {
            statement.setObject(i++, new BigDecimal(start), Types.INTEGER);
            statement.setObject(i++, new BigDecimal(end), Types.INTEGER);
        }
        return statement;
    }

    private void prepareCount() {
        this.pagedQuery = "select count(*) rowCount from (" + super.getFilteredStatement() + ")";
        this.start = -1;
    }

    public int getCount() throws SQLException {
        prepareCount();
        return super.selectRow().getBigDecimal("rowCount").intValue();
    }

    public int getCount(String connectionName) throws SQLException {
        prepareCount();
        return super.selectRow(connectionName).getBigDecimal("rowCount").intValue();
    }

    public int getCount(Connection connection) throws SQLException {
        prepareCount();
        return super.selectRow(connection).getBigDecimal("rowCount").intValue();
    }

    private void prepareSelect(int start, int end) {
        this.pagedQuery = "select * from (select rownum rowCount, baseQuery.* from (\n\n" + super.getFilteredStatement() + ") baseQuery ) where rowCount between ? and ?";
        this.start = start;
        this.end = end;
    }

    public DataTable<?> select(int start, int end) throws SQLException {
        prepareSelect(start, end);
        return super.selectTable();
    }

    public DataTable<?> select(String connectionName, int start, int end) throws SQLException {
        prepareSelect(start, end);
        return super.selectTable(connectionName);
    }

    public DataTable<?> select(Connection connection, int start, int end) throws SQLException {
        prepareSelect(start, end);
        return super.selectTable(connection);
    }
}
