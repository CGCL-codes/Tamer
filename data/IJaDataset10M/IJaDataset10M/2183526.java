package edge.querybuilder.generic;

/**
 * Simple (non-subquery result) column with owner table.
 * It can be a physical column, pseudo column or
 * column from table subquery expression.
 */
public interface ColumnSimple extends Column {

    public Table getTable();
}
