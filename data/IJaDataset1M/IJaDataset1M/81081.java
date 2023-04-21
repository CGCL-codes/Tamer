package com.yeep.basis.swing.widget.list;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

@SuppressWarnings("serial")
public class ListWidget<X> extends JList {

    private ListWidgetModel<X> listModel;

    public ListWidget() {
        super();
        setSelectionModel(false);
    }

    public ListWidget(boolean isMultipleSelect) {
        setSelectionModel(isMultipleSelect);
    }

    public ListWidget(ListWidgetModel<X> listModel, boolean isMultipleSelect) {
        this(isMultipleSelect);
        this.listModel = listModel;
    }

    /**
	 * Set selection mode
	 */
    private void setSelectionModel(boolean isMultipleSelect) {
        DefaultListSelectionModel selectionModel = new DefaultListSelectionModel();
        if (isMultipleSelect) selectionModel.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION); else selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        selectionModel.setLeadAnchorNotificationEnabled(false);
        setSelectionModel(selectionModel);
    }

    /**
	 * Set listModel
	 * @param listModel
	 */
    public void setListModel(ListWidgetModel<X> listModel) {
        super.setModel(listModel);
        this.listModel = listModel;
    }

    /**
	 * Get listModel
	 * @return
	 */
    public ListWidgetModel<X> getListModel() {
        return listModel;
    }

    /**
	 * Get selected item
	 * @return
	 */
    public X getSelected() {
        int selectedIndex = this.getSelectedIndex();
        return selectedIndex == -1 ? null : this.listModel.getEntry(selectedIndex);
    }

    /**
	 * Set selected item
	 */
    public void setSelected(X x) {
        if (x != null) setSelectedIndex(this.listModel.getEntryIndex(x));
    }

    /**
	 * Add a item to widget
	 * @param x
	 */
    public void addItem(X x) {
        this.listModel.addEntry(x);
    }

    /**
	 * Remove the specific item from the list widget
	 * @param x
	 */
    public void removeItem(X x) {
        this.listModel.removeEntry(x);
    }
}
