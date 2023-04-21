package org.jumpmind.symmetric.ddl.alteration;

import org.jumpmind.symmetric.ddl.model.Database;
import org.jumpmind.symmetric.ddl.model.Table;

/**
 * Represents the removal of a table from a model.
 * 
 * @version $Revision: $
 */
public class RemoveTableChange extends TableChangeImplBase {

    /**
     * Creates a new change object.
     * 
     * @param table The table
     */
    public RemoveTableChange(Table table) {
        super(table);
    }

    /**
     * {@inheritDoc}
     */
    public void apply(Database database, boolean caseSensitive) {
        Table table = database.findTable(getChangedTable().getName(), caseSensitive);
        database.removeTable(table);
    }
}
