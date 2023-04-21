package com.softaspects.jsf.renderer.table.cell.booleans;

import com.softaspects.jsf.renderer.table.cell.AbstractCellEditor;
import com.softaspects.jsf.component.table.Table;
import javax.faces.context.FacesContext;

public abstract class AbstractBooleanCellEditor extends AbstractCellEditor {

    public Object getValue(FacesContext context, Table table) {
        int rowIndex = getRowIndex();
        int columnIndex = getColumnIndex();
        Object value = table.getValueAt(rowIndex, columnIndex);
        if (value != null && value instanceof Boolean) {
            return value;
        }
        return generateExceptionIfTypeNotCompartable(rowIndex, columnIndex, value);
    }
}
