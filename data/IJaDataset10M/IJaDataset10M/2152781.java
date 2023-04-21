package edge.querybuilder.generic;

/**
 * Basic insert query with limited features (can't insert into
 * subquery result views, can't use subqueries to acquire data
 * set to insert).
 * <p/>
 * Compatible with most databases.
 */
public interface InsertQuery extends Query {

    /**
     * Set table to insert into.
     */
    public void setTable(TableSimple table);

    /**
     * Add value to insert for column.     
     */
    public void addValue(ColumnSimple column, Object value);
}
