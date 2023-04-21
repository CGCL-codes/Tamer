package org.crazydays.gameplan.map.swing;

import java.util.LinkedList;
import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;
import org.crazydays.gameplan.map.Geometry;
import org.crazydays.gameplan.map.GridType;

/**
 * GridTypeModel
 */
public class GridTypeModel implements ComboBoxModel {

    /** geometry */
    protected Geometry geometry;

    /** listDataListeners */
    protected List<ListDataListener> listDataListeners = new LinkedList<ListDataListener>();

    /**
     * GeometryModel constructor.
     * 
     * @param geometry Geometry
     */
    public GridTypeModel(Geometry geometry) {
        this.geometry = geometry;
    }

    /**
     * Get geometry grid option count.
     * 
     * @return Size
     * @see javax.swing.ListModel#getSize()
     */
    @Override
    public int getSize() {
        return GridType.TYPE_NAMES.length;
    }

    /**
     * Get geometry grid option.
     * 
     * @param index Index
     * @return String
     * @see javax.swing.ListModel#getElementAt(int)
     */
    @Override
    public Object getElementAt(int index) {
        return GridType.TYPE_NAMES[index];
    }

    /**
     * Get selected geometry grid type.
     * 
     * @return GridType
     * @see javax.swing.ComboBoxModel#getSelectedItem()
     */
    @Override
    public Object getSelectedItem() {
        return GridType.TYPE_NAMES[geometry.getType()];
    }

    /**
     * Set selected geometry grid type.
     * 
     * @param item GridType
     * @see javax.swing.ComboBoxModel#setSelectedItem(java.lang.Object)
     */
    @Override
    public void setSelectedItem(Object item) {
        for (int i = 0; i < GridType.TYPE_NAMES.length; i++) {
            if (item.equals(GridType.TYPE_NAMES[i])) {
                geometry.setType(i);
                return;
            }
        }
    }

    /**
     * Add list data listener.
     * 
     * @param listener Listener
     * @see javax.swing.ListModel#addListDataListener(ListDataListener)
     */
    @Override
    public void addListDataListener(ListDataListener listener) {
        listDataListeners.add(listener);
    }

    /**
     * Remove list data listener.
     * 
     * @param listener Listener
     * @see javax.swing.ListModel#removeListDataListener(ListDataListener)
     */
    @Override
    public void removeListDataListener(ListDataListener listener) {
        listDataListeners.remove(listener);
    }
}
