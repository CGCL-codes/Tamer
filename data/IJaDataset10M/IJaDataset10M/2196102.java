package deduced.viewer.table;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.table.AbstractTableModel;
import assertion.AssertUtility;
import deduced.ChangeType;
import deduced.Property;
import deduced.PropertyChangeEvent;
import deduced.PropertyCollection;
import deduced.PropertyInstance;
import deduced.PropertyList;
import deduced.PropertyListener;

public class PropertyTableModel extends AbstractTableModel implements PropertyListener {

    private List _columnPropertyKeys = new ArrayList();

    private PropertyList _displayedList = new PropertyList();

    private List _rowList = new ArrayList();

    private Map _collectionRowMap = new IdentityHashMap();

    public PropertyTableModel() {
        _displayedList.addListener(this);
    }

    public void propertyChanged(PropertyChangeEvent event) {
        if (event.getCollection() == _displayedList) {
            displayListChanged(event);
        } else {
            subItemChanged(event);
        }
    }

    public void setDisplayedInstances(List instanceList) {
        getColumnPropertyKeys().clear();
        getColumnPropertyKeys().addAll(instanceList);
        fireTableStructureChanged();
    }

    /**
     * displayListChanged
     * 
     * @param event
     */
    private void displayListChanged(PropertyChangeEvent event) {
        ChangeType type = event.getType();
        PropertyCollection newSubCollection = (PropertyCollection) event.getNewValue();
        PropertyCollection oldSubCollection = (PropertyCollection) event.getOldValue();
        if (type == ChangeType.ADD) {
            if (newSubCollection != null) {
                newSubCollection.addListener(this);
            }
            int rowKey = event.getCollection().getSize();
            CollectionTableRow newRow = new CollectionTableRow();
            newRow.setCollection(newSubCollection);
            newRow.setSelected(false);
            _rowList.add(newRow);
            _collectionRowMap.put(newSubCollection, newRow);
            fireTableRowsInserted(rowKey, rowKey);
        } else if (type == ChangeType.REMOVE) {
            if (oldSubCollection != null) {
                oldSubCollection.removeListener(this);
            }
            CollectionTableRow removeRow = (CollectionTableRow) _collectionRowMap.remove(oldSubCollection);
            int removeKey = _rowList.indexOf(removeRow);
            _rowList.remove(removeKey);
            fireTableRowsDeleted(removeKey, removeKey);
        } else if (type == ChangeType.UPDATE) {
            int row = 0;
            int column = 0;
            CollectionTableRow removeRow = (CollectionTableRow) _collectionRowMap.get(oldSubCollection);
            row = _rowList.indexOf(removeRow);
            fireTableCellUpdated(row, column);
        }
    }

    /**
     * subItemChanged
     * 
     * @param event
     */
    private void subItemChanged(PropertyChangeEvent event) {
        if (!getColumnPropertyKeys().contains(event.getInstance())) {
            return;
        }
    }

    public int getColumnCount() {
        return getColumnPropertyKeys().size();
    }

    public int getRowCount() {
        if (_displayedList == null) {
            return 0;
        }
        return _displayedList.getSize();
    }

    public String getColumnName(int col) {
        return ((PropertyInstance) getColumnPropertyKeys().get(col)).getName();
    }

    public Object getValueAt(int row, int col) {
        if (_displayedList == null || row < 0 || row > _displayedList.getSize()) {
            AssertUtility.fail();
            return null;
        }
        if (col < 0 || col > getColumnPropertyKeys().size()) {
            AssertUtility.fail();
            return null;
        }
        PropertyCollection rowProperty = (PropertyCollection) _displayedList.getProperty(new Integer(row)).getValue();
        Property returnedProperty = rowProperty.getProperty(getColumnPropertyKeys().get(col));
        return returnedProperty.getValue();
    }

    public Class getColumnClass(int c) {
        Object value = getValueAt(0, c);
        if (value == null) {
            return Object.class;
        }
        return value.getClass();
    }

    public boolean isCellEditable(int row, int col) {
        return true;
    }

    public void setValueAt(Object value, int row, int col) {
    }

    /**
     * @return
     */
    public PropertyList getDisplayedList() {
        return _displayedList;
    }

    /**
     * @param columnPropertyKeys
     *            The columnPropertyKeys to set.
     */
    public void setColumnPropertyKeys(List columnPropertyKeys) {
        _columnPropertyKeys = columnPropertyKeys;
    }

    /**
     * @return Returns the columnPropertyKeys.
     */
    public List getColumnPropertyKeys() {
        return _columnPropertyKeys;
    }
}
