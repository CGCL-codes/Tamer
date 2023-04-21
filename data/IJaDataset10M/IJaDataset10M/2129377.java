package de.mguennewig.pobjects;

import de.mguennewig.pobjects.metadata.Column;

/**
 * Class description goes here.
 *
 * @author Michael G�nnewig
 */
public class ColumnReference extends Object {

    private final TableExprContext ctx;

    private final TableRef table;

    private final Column column;

    /**
   * @throws NullPointerException if no table or column is passed.
   */
    public ColumnReference(final TableExprContext ctx, final TableRef table, final Column column) {
        super();
        if (table == null) throw new NullPointerException("table");
        if (column == null) throw new NullPointerException("column");
        this.ctx = ctx;
        this.table = table;
        this.column = column;
    }

    /** Creates a new column reference without table expression context.
   *
   * @throws NullPointerException if any parameter is {@code null}.
   */
    public ColumnReference(final TableRef table, final Column column) {
        this(null, table, column);
    }

    /** Creates a new column reference for a table expression.
   *
   * @throws NullPointerException if any parameter is {@code null}.
   */
    public ColumnReference(final TableExprContext ctx, final Column column) {
        this(ctx, ctx.getTableReference(column), column);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof ColumnReference)) return false;
        final ColumnReference other = (ColumnReference) obj;
        return (this.column.equals(other.column) && this.table.equals(other.table));
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return 31 * (31 + column.hashCode()) + table.hashCode();
    }

    public final TableExprContext getTableExprContext() {
        return this.ctx;
    }

    public final TableRef getTable() {
        return table;
    }

    public final Column getColumn() {
        return column;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return table.getAlias() + "." + getColumn().getSchemaName();
    }
}
