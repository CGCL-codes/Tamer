package net.sf.doolin.gui.table;

import java.util.ArrayList;
import java.util.List;
import net.sf.doolin.gui.core.support.ExtensibleListFactory;
import net.sf.doolin.gui.field.AbstractSupportField;
import net.sf.doolin.gui.field.event.SelectionListener;
import net.sf.doolin.gui.table.support.TableSupport;

/**
 * Table field.
 * 
 * This displays a list of items in a table.
 * 
 * @author Damien Coraboeuf
 * @version $Id: FieldTable.java,v 1.6 2007/08/15 09:05:24 guinnessman Exp $
 */
public class FieldTable extends AbstractSupportField<TableSupport> {

    private ExtensibleListFactory<Column> columnsGenerator;

    private List<Column> columns = new ArrayList<Column>();

    private boolean multipleSelection;

    private List<SelectionListener> selectionListenerList = new ArrayList<SelectionListener>(0);

    /**
	 * Creates the table support.
	 * 
	 * @see #createSupport(Class)
	 * @see TableSupport
	 * @see net.sf.doolin.gui.field.AbstractSupportField#createSupport()
	 */
    @Override
    protected TableSupport createSupport() {
        return createSupport(TableSupport.class);
    }

    /**
	 * Registers the selection listeners.
	 * 
	 * @see net.sf.doolin.gui.field.AbstractSupportField#init()
	 */
    @Override
    protected void init() {
        super.init();
        for (SelectionListener selectionListener : selectionListenerList) {
            getSupport().addSelectionListener(selectionListener);
        }
    }

    public Object getFieldData(Object formData) {
        return getItems();
    }

    public void setFieldData(Object formData, Object fieldData) {
        @SuppressWarnings("unchecked") List items = (List) fieldData;
        setItems(items);
    }

    /**
	 * Returns the list of column definitions
	 * 
	 * @return List of column definitions
	 */
    public List<Column> getColumns() {
        return columns;
    }

    /**
	 * Sets the list of column definitions.
	 * 
	 * @param columns
	 *            List of column definitions.
	 */
    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    /**
	 * Gets the list of items in the table
	 * 
	 * @see TableSupport#getItems()
	 * @return List of items
	 */
    public List getItems() {
        return getSupport().getItems();
    }

    /**
	 * Sets the list of items in the table
	 * 
	 * @see TableSupport#setItems(List)
	 * @param items
	 *            List of items
	 */
    public void setItems(List items) {
        getSupport().setItems(items);
    }

    /**
	 * Adds an item at the end of the table
	 * 
	 * @see TableSupport#addItem(Object)
	 * @param item
	 *            Item to add
	 */
    public void addItem(Object item) {
        getSupport().addItem(item);
    }

    /**
	 * Deletes an item from the table
	 * 
	 * @see TableSupport#deleteItem(Object)
	 * @param item
	 *            Item to delete
	 */
    public void deleteItem(Object item) {
        getSupport().deleteItem(item);
    }

    /**
	 * Updates an item in the table
	 * 
	 * @see TableSupport#updateItem(Object)
	 * @param item
	 *            Item to update
	 */
    public void updateItem(Object item) {
        getSupport().updateItem(item);
    }

    /**
	 * Gets the current selected item
	 * 
	 * @return Selected item or <code>null</code> if none is selected.
	 * @see TableSupport#getSelectedItem()
	 */
    public Object getSelectedItem() {
        return getSupport().getSelectedItem();
    }

    /**
	 * Reset the columns.
	 * 
	 * @see TableSupport#resetColumns()
	 */
    public void resetColumns() {
        getSupport().resetColumns();
    }

    /**
	 * Returns the <code>multipleSelection</code> property.
	 * 
	 * @return <code>multipleSelection</code> property.
	 */
    public boolean isMultipleSelection() {
        return multipleSelection;
    }

    /**
	 * Sets the <code>multipleSelection</code> property.
	 * 
	 * @param multipleSelection
	 *            <code>multipleSelection</code> property.
	 */
    public void setMultipleSelection(boolean multipleSelection) {
        this.multipleSelection = multipleSelection;
    }

    /**
	 * Returns the <code>columnsGenerator</code> property.
	 * 
	 * @return <code>columnsGenerator</code> property.
	 */
    public ExtensibleListFactory<Column> getColumnsGenerator() {
        return columnsGenerator;
    }

    /**
	 * Sets the <code>columnsGenerator</code> property.
	 * 
	 * @param columnsGenerator
	 *            <code>columnsGenerator</code> property.
	 */
    public void setColumnsGenerator(ExtensibleListFactory<Column> columnsGenerator) {
        this.columnsGenerator = columnsGenerator;
    }

    /**
	 * Selects an item in the table
	 * 
	 * @param object
	 *            Object to be selected
	 */
    public void setSelectedItem(Object object) {
        getSupport().setSelectedItem(object);
    }

    /**
	 * Adds a selection listener
	 * 
	 * @param selectionListener
	 *            Selection listener
	 */
    public void addSelectionListener(SelectionListener selectionListener) {
        selectionListenerList.add(selectionListener);
    }

    /**
	 * Returns the selectionListenerList
	 * 
	 * @return selectionListenerList
	 */
    public List<SelectionListener> getSelectionListenerList() {
        return selectionListenerList;
    }

    /**
	 * Sets the selectionListenerList
	 * 
	 * @param selectionListenerList
	 */
    public void setSelectionListenerList(List<SelectionListener> selectionListenerList) {
        this.selectionListenerList = selectionListenerList;
    }
}
