package org.arch4j.ui.table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;

/**
 * A table where each row is a single object.
 * 
 * For debugging, the System property "component.tooltip.hint" can
 * be set to enable a tooltip to display the name of the class
 * used to display the table.
 *
 * @author  awick
 */
public class ObjectTable extends JTable {

    private boolean isSorted = false;

    private Comparator comparator = null;

    /**
     * Default constructor.
     */
    public ObjectTable() {
        if (System.getProperty("component.tooltip.hint") != null) {
            setToolTipText(this.getClass().getName());
        }
    }

    /** 
     * Creates a new instance of ObjectTable 
     *
     * @param anObjectList       The objects that represent each row.
     * @param aPropertiesList    The properties that are used to set/get data for each column.
     * @param aColumnHeadingList The strings to use for the column headers.
     * @param aRowHeadingList    The row headings list.  NOTE: Not currently used...
     */
    public ObjectTable(Collection anObjectList, Collection aPropertiesList, Collection aColumnHeadingList, Collection aRowHeadingList) {
        ObjectTableModel theModel = new ObjectTableModel(anObjectList, aPropertiesList, aColumnHeadingList, aRowHeadingList);
        setColumnSelectionAllowed(false);
        setRowSelectionAllowed(true);
        setModel(theModel);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        if (System.getProperty("component.tooltip.hint") != null) {
            setToolTipText(this.getClass().getName());
        }
    }

    /** 
     * Creates a new instance of ObjectTable 
     *
     * @param anObjectList       The objects that represent each row.
     * @param aRowBuilder    	  The row builder that gets data to be displayed for each row.
     * @param aColumnHeadingList The strings to use for the column headers.
     * @param aRowHeadingList    The row headings list.  NOTE: Not currently used...
     */
    public ObjectTable(Collection anObjectList, ObjectRowBuilder aRowBuilder, Collection aColumnHeadingList, Collection aRowHeadingList) {
        ObjectTableModel theModel = new ObjectTableModel(anObjectList, aRowBuilder, aColumnHeadingList, aRowHeadingList);
        setColumnSelectionAllowed(false);
        setRowSelectionAllowed(true);
        setModel(theModel);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        if (System.getProperty("component.tooltip.hint") != null) {
            setToolTipText(this.getClass().getName());
        }
    }

    /**
     * Add a row to the table for each of the given objects.
     *
     * @param objects The objects to add.
     */
    public void addObjects(Collection objects) {
        for (Iterator iter = objects.iterator(); iter.hasNext(); ) {
            addObject(iter.next());
        }
    }

    /**
     * Add a row to the table for the given object.
     *
     * @param anObject The object to add.
     */
    public void addObject(Object anObject) {
        getTableModel().addObject(anObject);
    }

    /**
     * Remove a row from the table for the given object.
     *
     * @param anObject The object to remove.
     */
    public void removeObject(Object anObject) {
        getTableModel().removeObject(anObject);
    }

    /**
     * Remove all of the objects from the table.
     */
    public void removeAllObjects() {
        getTableModel().setObjects(new ArrayList());
    }

    /**
     * Set the objects that represent rows in the table.
     *
     * @param anObjectList The objects for each row.
     */
    public void setObjects(Collection anObjectList) {
        if (isSorted) {
            Set sortedSet = new TreeSet(comparator);
            sortedSet.addAll(anObjectList);
            getTableModel().setObjects(sortedSet);
        } else {
            getTableModel().setObjects(anObjectList);
        }
    }

    /**
	 * Sets the selected object in the table.
	 * 
	 * @param anObject The object to select.
	 */
    public void setSelectedObject(Object anObject) {
        int index = getIndex(anObject);
        if (index == -1) {
            return;
        }
        setRowSelectionInterval(index, index);
    }

    /**
	 * Get the index of the object.
	 * 
	 * @param anObject the Object to get the index of.
	 * 
	 * @return The index of the object. <b>-1</b> if not found.
	 */
    public int getIndex(Object anObject) {
        return getTableModel().getIndex(anObject);
    }

    /**
	 * Get the objects in the table.
	 * 
	 * @return A collection of objects being shown in the table
	 */
    public Collection getObjects() {
        if (isSorted) {
            Set sortedSet = new TreeSet(comparator);
            sortedSet.addAll(getTableModel().getObjects());
            return sortedSet;
        }
        return getTableModel().getObjects();
    }

    /**
     * Get the object for the given row index.
     *
     * @param anIndex The index of the row to get the object for.
     *
     * @return The object for the given index.
     */
    public Object getRowObject(int anIndex) {
        return getTableModel().getRowObject(anIndex);
    }

    /**
     * Rebuild columns with new properties and headings.
     * 
     * @param aPropertiesList    The properties that are used to set/get data for each column.
     * @param aColumnHeadingList The strings to use for the column headers.
     */
    public void rebuildColumns(Collection aPropertiesList, Collection aColumnHeadingList) {
        for (int index = getColumnCount() - 1; index >= 0; index--) {
            removeColumnAndData(index);
        }
        getTableModel().rebuildColumns(aPropertiesList, aColumnHeadingList);
    }

    public void removeColumnAndData(int vColIndex) {
        ObjectTableModel model = getTableModel();
        TableColumn col = getColumnModel().getColumn(vColIndex);
        int columnModelIndex = col.getModelIndex();
        Vector data = model.getDataVector();
        Vector colIds = model.getColumnIdentifiers();
        removeColumn(col);
        colIds.removeElementAt(columnModelIndex);
        for (int r = 0; r < data.size(); r++) {
            Vector row = (Vector) data.get(r);
            row.removeElementAt(columnModelIndex);
        }
        model.setDataVector(data, colIds);
        Enumeration enumerator = getColumnModel().getColumns();
        for (; enumerator.hasMoreElements(); ) {
            TableColumn c = (TableColumn) enumerator.nextElement();
            if (c.getModelIndex() >= columnModelIndex) {
                c.setModelIndex(c.getModelIndex() - 1);
            }
        }
        model.fireTableStructureChanged();
    }

    /**
     * Refresh all the data in the table rows.
     */
    public void refreshTableData() {
        getTableModel().refreshTableData();
    }

    /**
     * Refresh the row data for the given object.
     * 
     * @param anObject The object to update the data for.
     */
    public void refreshTableData(Object anObject) {
        getTableModel().refreshTableData(anObject);
    }

    private ObjectTableModel getTableModel() {
        return (ObjectTableModel) getModel();
    }

    public void setSelectionMode(int aMode) {
        super.setSelectionMode(aMode);
        getTableModel().setSelectionMode(aMode);
    }

    public void setComparator(Comparator aComparator) {
        if (aComparator == null) {
            setSorted(false);
        }
        comparator = aComparator;
    }

    /**
     * Enable and disable sorting.  In order for sorting to be enabled a comparator must
     * first be set to compare the objects in the internal collection.
     * 
     * @param sorted
     * @throws IllegalStateException When the comparator is null and sorting is being enabled
     */
    public void setSorted(boolean sorted) {
        if (sorted && comparator == null) {
            throw new IllegalStateException("Unable sort objects without a comparator (comparator is null)");
        }
        isSorted = sorted;
    }

    /**
	 * Indicates if the ObjectTable is to display objects in a sorted order.
	 * 
	 * @return
	 */
    public boolean isSorted() {
        return isSorted;
    }

    /**
     * @return the current selection mode.  Will either be ListSelectionModel.MULTIPLE_INTERVAL_SELECTION
     * or ListSelectionModel.SINGLE_SELECTION.
     */
    public int getSelectionMode() {
        return getTableModel().getSelectionMode();
    }

    public boolean isCellEditable(int arg0, int arg1) {
        if (getRowCount() <= arg0) {
            return super.isCellEditable(arg0, arg1);
        }
        Object dataObject = getValueAt(arg0, arg1);
        if (dataObject instanceof Boolean) {
            return true;
        } else {
            return false;
        }
    }
}
