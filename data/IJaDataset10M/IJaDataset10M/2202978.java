package org.octaedr.upnp.tools.devicespy.engine;

import javax.swing.table.AbstractTableModel;
import org.octaedr.upnp.tools.devicespy.data.PropertyArray;

public class PropertyTableModel extends AbstractTableModel {

    private static final String[] COLUMN_NAMES = { "Name", "Value" };

    private PropertyArray properties;

    public String getColumnName(int column) {
        return COLUMN_NAMES[column];
    }

    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    public int getRowCount() {
        if (this.properties != null) {
            return this.properties.getSize();
        } else {
            return 0;
        }
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if (this.properties != null) {
            switch(columnIndex) {
                case 0:
                    return this.properties.getKey(rowIndex);
                case 1:
                    return this.properties.getValue(rowIndex);
            }
        }
        return null;
    }

    public void setPropertiesData(final PropertyArray properties) {
        this.properties = properties;
        fireTableDataChanged();
    }
}
