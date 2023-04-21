package org.plazmaforge.studio.reportdesigner.model.table;

import org.plazmaforge.studio.reportdesigner.model.IElement;
import org.plazmaforge.studio.reportdesigner.model.DesignElement;

/**
 * Table Column
 * 
 * @author ohapon
 *
 */
public class TableColumn extends DesignElement implements ITableColumn {

    public int getIndex() {
        return indexOfColumn();
    }

    public ITable getTable() {
        return (ITable) getParent();
    }

    public void setTable(ITable table) {
        setParent(table);
    }

    public void setParent(IElement parent) {
        if (parent != null && !(parent instanceof ITable)) {
            throw new IllegalArgumentException("Parent must be ITable");
        }
        super.setParent(parent);
    }

    public boolean isLast() {
        return getTable().isLastColumn(this);
    }

    protected int indexOfColumn() {
        ITable table = getTable();
        if (table == null) {
            return UNKNOWN_INDEX;
        }
        return getTable().indexOfColumn(this);
    }
}
