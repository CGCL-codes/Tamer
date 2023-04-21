package org.jumpmind.symmetric.ddl.alteration;

import org.jumpmind.symmetric.ddl.model.Column;

/**
 * Represents a change to a column of a table.
 * 
 * @version $Revision: $
 */
public interface ColumnChange extends TableChange {

    /**
     * Returns the affected column from the original model.
     * 
     * @return The affected column
     */
    public Column getChangedColumn();
}
