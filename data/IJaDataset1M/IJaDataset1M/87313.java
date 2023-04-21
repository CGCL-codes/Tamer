package prisms.osql;

/** Represents a request for data from the database */
public class Query {

    private final Table theTable;

    private Column<?>[] theColumns;

    private WhereExpression theFilter;

    private prisms.util.Sorter<Column<?>> theSorter;

    private boolean isDistinct;

    private int theOffset;

    private int theLimit;

    /**
	 * Creates a simple query
	 * 
	 * @param table The table structure to query data from
	 */
    public Query(Table table) {
        theTable = table;
    }

    /**
	 * Creates a query
	 * 
	 * @param table The table structure to query data from
	 * @param columns The columns to retrieve the values of. May be null to get the data from all
	 *        columns in the table structure.
	 * @param where The filter to determine which rows to retrieve
	 * @param sorter The sorter to determine the order for rows should appear in the results
	 */
    public Query(Table table, Column<?>[] columns, WhereExpression where, prisms.util.Sorter<Column<?>> sorter) {
        this(table);
        theColumns = columns;
        theFilter = where;
        theSorter = sorter;
    }

    /** @return The table structure to query data from */
    public Table getTable() {
        return theTable;
    }

    /**
	 * @return The columns to retrieve the values of, or null if this query retrieves all columns
	 *         from its table structure
	 */
    public Column<?>[] getColumns() {
        return theColumns;
    }

    /** @return The filter that determines which rows to retrieve for this query */
    public WhereExpression getFilter() {
        return theFilter;
    }

    /** @return The sorter that determines the order for rows in the results of this query */
    public prisms.util.Sorter<Column<?>> getSorter() {
        return theSorter;
    }

    /** @return Whether this query searches for distinct records */
    public boolean isDistinct() {
        return isDistinct;
    }

    /** @param distinct Whether this query should search for distinct records */
    public void setDistinct(boolean distinct) {
        isDistinct = distinct;
    }

    /**
	 * @return The index (starting from 0) of the first row matching the rest of the query to
	 *         retrieve
	 */
    public int getOffset() {
        return theOffset;
    }

    /** @return The number of rows matching the rest of the query to retrieve */
    public int getLimit() {
        return theLimit;
    }

    /**
	 * @param offset The index (starting from 0) of the first row matching the rest of the query to
	 *        retrieve
	 * @param limit The number of rows matching the rest of the query to retrieve
	 */
    public void setRowIndexes(int offset, int limit) {
        theOffset = offset;
        theLimit = limit;
    }

    /** @return Whether this query can be written as SQL without the need for a prepared statement */
    public boolean isStringable() {
        if (theFilter != null && !theFilter.isStringable()) return false;
        return true;
    }

    ;
}
