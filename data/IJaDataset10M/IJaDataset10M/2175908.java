package net.sf.cybowmodeller.modelcomposer.model;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author SHIMAYOSHI Takao
 * @version $Revision: 10 $
 */
public final class ComponentTypeList extends AbstractTableModel {

    private static final int NUM_COLUMNS = 2;

    private static final int COL_NAME = 0;

    private static final int COL_LOCATION = 1;

    private static final String[] COL_NAMES = new String[] { "name", "location" };

    private ArrayList<ComponentType> components = new ArrayList<ComponentType>();

    private int selected = -1;

    public void addComponentType(final ComponentType componentType) {
        final int index = components.size();
        components.add(componentType);
        fireTableRowsInserted(index, index);
    }

    public void removeComponentType(final int index) {
        components.remove(index);
        fireTableRowsDeleted(index, index);
    }

    public void setSelectedIndex(final int index) {
        selected = index;
    }

    public ComponentType getSelectedType() {
        if (selected < 0) {
            return null;
        } else {
            return components.get(selected);
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public String getColumnName(int column) {
        return COL_NAMES[column];
    }

    public int getColumnCount() {
        return NUM_COLUMNS;
    }

    public int getRowCount() {
        return components.size();
    }

    public Object getValueAt(final int rowIndex, final int columnIndex) {
        switch(columnIndex) {
            case COL_NAME:
                return components.get(rowIndex).getName();
            case COL_LOCATION:
                return components.get(rowIndex).getUri().toString();
            default:
                return null;
        }
    }
}
