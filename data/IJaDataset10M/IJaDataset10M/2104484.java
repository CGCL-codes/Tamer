package org.xnap.commons.gui.action;

public abstract class AbstractSingleSelectionAction<T> extends AbstractXNapAction implements SelectionAction {

    protected T item;

    public AbstractSingleSelectionAction() {
    }

    public T getItem() {
        return item;
    }

    public void setSelectedItems(Object[] items) {
        if (items != null) {
            item = selectItem(items[0]);
        }
        setEnabled(item != null);
    }

    public abstract T selectItem(Object selectedItem);
}
