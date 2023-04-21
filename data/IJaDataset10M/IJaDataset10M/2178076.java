package org.jumpmind.symmetric.core.db.alter;

import org.jumpmind.symmetric.core.model.Column;
import org.jumpmind.symmetric.core.model.Database;
import org.jumpmind.symmetric.core.model.Table;

/**
 * Represents the change of the data type of a column.
 * 
 * @version $Revision: $
 */
public class ColumnDataTypeChange extends TableChangeImplBase implements ColumnChange {

    /** The column. */
    private Column _column;

    /** The JDBC type code of the new type. */
    private int _newTypeCode;

    /**
     * Creates a new change object.
     * 
     * @param table
     *            The table of the column
     * @param column
     *            The column
     * @param newTypeCode
     *            The JDBC type code of the new type
     */
    public ColumnDataTypeChange(Table table, Column column, int newTypeCode) {
        super(table);
        _column = column;
        _newTypeCode = newTypeCode;
    }

    /**
     * Returns the column.
     * 
     * @return The column
     */
    public Column getChangedColumn() {
        return _column;
    }

    /**
     * Returns the JDBC type code of the new type.
     * 
     * @return The type code
     */
    public int getNewTypeCode() {
        return _newTypeCode;
    }

    /**
     * {@inheritDoc}
     */
    public void apply(Database database, boolean caseSensitive) {
        Table table = database.findTable(getChangedTable().getTableName(), caseSensitive);
        Column column = table.findColumn(_column.getName(), caseSensitive);
        column.setTypeCode(_newTypeCode);
    }
}
