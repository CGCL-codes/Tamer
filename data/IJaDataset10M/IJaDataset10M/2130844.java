package org.butu.gui.widgets.table;

import org.butu.gui.widgets.IColumnInfo;
import org.butu.mapped.IMappedTableRow;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.ISelection;

public class CursorTableSelection implements ISelection, IAdaptable {

    private IMappedTableRow selection;

    private IColumnInfo column;

    public CursorTableSelection(IMappedTableRow selection, IColumnInfo column) {
        this.column = column;
        this.selection = selection;
    }

    public IMappedTableRow getSelection() {
        return selection;
    }

    public void setSelection(IMappedTableRow selection) {
        this.selection = selection;
    }

    public IColumnInfo getColumn() {
        return column;
    }

    public void setColumn(IColumnInfo column) {
        this.column = column;
    }

    public boolean isEmpty() {
        return selection == null;
    }

    @SuppressWarnings("unchecked")
    public Object getAdapter(Class adapter) {
        if (adapter.isInstance(this)) {
            return this;
        } else if (adapter.isInstance(column)) {
            return column;
        } else if (adapter.isInstance(selection)) {
            return selection;
        } else if (selection != null) {
            return Platform.getAdapterManager().getAdapter(selection, adapter);
        }
        return null;
    }
}
