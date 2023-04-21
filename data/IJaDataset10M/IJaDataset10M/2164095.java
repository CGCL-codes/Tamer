package net.sf.doolin.gui.table.action;

import java.util.List;
import net.sf.doolin.gui.core.action.AbstractAction;
import net.sf.doolin.gui.field.Field;
import net.sf.doolin.gui.field.FieldAction;
import net.sf.doolin.gui.table.FieldTable;

public abstract class AbstractTableAction<T> extends AbstractAction implements FieldAction {

    private FieldTable fieldTable;

    public void setField(Field field) {
        fieldTable = (FieldTable) field;
    }

    protected FieldTable getFieldTable() {
        return fieldTable;
    }

    @SuppressWarnings("unchecked")
    protected List<T> getItems() {
        return fieldTable.getItems();
    }

    protected void setItems(List<T> items) {
        fieldTable.setItems(items);
    }

    protected void addItem(T item) {
        fieldTable.addItem(item);
    }

    protected void deleteItem(T item) {
        fieldTable.deleteItem(item);
    }

    protected void updateItem(T item) {
        fieldTable.updateItem(item);
    }

    @SuppressWarnings("unchecked")
    protected T getSelectedItem() {
        return (T) fieldTable.getSelectedItem();
    }
}
