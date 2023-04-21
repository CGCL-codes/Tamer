package org.fest.swing.data;

import javax.swing.JTable;
import org.fest.util.Objects;
import static java.lang.String.valueOf;
import static org.fest.util.Objects.hashCodeFor;
import static org.fest.util.Strings.*;

/**
 * Understands a cell in a <code>{@link JTable}</code>.
 * <p>
 * Example:
 * <pre>
 * // import static org.fest.swing.fixture.TableCellByColumnName.row;
 * JTableCellFixture cell = dialog.table("records").cell({@link TableCellByColumnName#row(int) row}(3).columnName("firstColumn"));
 * </pre>
 * </p>
 * 
 * @author Alex Ruiz
 */
public class TableCellByColumnName {

    public final int row;

    public final Object columnName;

    /**
   * Starting point for the creation of a <code>{@link TableCellByColumnName}</code>.
   * <p>
   * Example:
   * <pre>
   * // import static org.fest.swing.fixture.TableCellByColumnName.row;
   * TableCellByColumnName cell = row(5).columnName("hobbyColumn");
   * </pre>
   * </p>
   * @param row the row index of the table cell to create.
   * @return the created builder.
   */
    public static TableCellBuilder row(int row) {
        return new TableCellBuilder(row);
    }

    /**
   * Understands creation of <code>{@link TableCellByColumnName}</code>s.
   *
   * @author Alex Ruiz
   */
    public static class TableCellBuilder {

        private final int row;

        TableCellBuilder(int row) {
            this.row = row;
        }

        /**
     * Creates a new table cell using the row index specified in <code>{@link TableCellBuilder#row(int)}</code> and the 
     * column index specified as the argument in this method. 
     * @param columnName the name of the column in the table cell to create.
     * @return the created table cell.
     */
        public TableCellByColumnName columnName(Object columnName) {
            return new TableCellByColumnName(row, columnName);
        }
    }

    protected TableCellByColumnName(int row, Object columnName) {
        this.row = row;
        this.columnName = columnName;
    }

    /** ${@inheritDoc} */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + hashCodeFor(columnName);
        result = prime * result + row;
        return result;
    }

    /** ${@inheritDoc} */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof TableCellByColumnName)) return false;
        TableCellByColumnName other = (TableCellByColumnName) obj;
        if (!Objects.areEqual(columnName, other.columnName)) return false;
        return row == other.row;
    }

    @Override
    public String toString() {
        return concat("[row=", valueOf(row), ", columnName=", quote(columnName), "]");
    }
}
