package org.freeworld.prilib.cell;

import org.freeworld.prilib.table.Table;
import org.freeworld.prilib.table.TableValidationException;

/**
 * <p>Empty wrapper adapter for a TableCellListener, this implementation allows
 * the developer to choose what methods to listen to by overriding them while
 * leaving the rest with stub implementations</p>
 * 
 * @author dchemko
 */
public class TableCellAdapter implements TableCellListener {

    @Override
    public void cellChanged(Table table, int row, String columnName, Object oldValue, Object newValue) {
    }

    @Override
    public void cellChanging(Table table, int row, String columnName, Object oldValue, Object newValue) throws TableValidationException {
    }

    @Override
    public void cellReverted(Table table, int row, String columnName, Object oldValue, Object newValue, TableValidationException exception) {
    }
}
