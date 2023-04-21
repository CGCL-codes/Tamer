package bt747.j2se_view.model;

import java.awt.FontMetrics;
import javax.swing.JTable;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import bt747.j2se_view.model.PositionData.UserWayPointListModel;

/**
 * @author Mario
 * 
 */
@SuppressWarnings("serial")
public class FileTableModel extends AbstractTableModel {

    private UserWayPointListModel wpListModel;

    /**
     * The columns currently shown.
     */
    private int[] columns = { DataTypes.FILE_DATE, DataTypes.FILE_TIME, DataTypes.GPS_TIME, DataTypes.FILE_PATH, DataTypes.GEOMETRY, DataTypes.LATITUDE, DataTypes.LONGITUDE, DataTypes.HEIGHT_METERS };

    /**
     * 
     */
    public FileTableModel(final UserWayPointListModel m) {
        wpListModel = m;
        wpListModel.addListDataListener(new WPListDataListener());
    }

    public void removeRows(int[] indexes) {
        final Object[] elements = new Object[indexes.length];
        for (int i = 0; i < indexes.length; i++) {
            elements[i] = wpListModel.getElementAt(indexes[i]);
        }
        wpListModel.remove(elements);
    }

    private final class WPListDataListener implements ListDataListener {

        public void contentsChanged(final ListDataEvent e) {
            fireTableRowsUpdated(e.getIndex0(), e.getIndex1());
        }

        public void intervalAdded(final ListDataEvent e) {
            fireTableRowsInserted(e.getIndex0(), e.getIndex1());
            fireTableDataChanged();
        }

        public void intervalRemoved(final ListDataEvent e) {
            fireTableRowsDeleted(e.getIndex0(), e.getIndex1());
        }
    }

    public void add(final String path) {
        wpListModel.add(path);
    }

    /**
     * 
     */
    public void clear() {
        wpListModel.clear();
    }

    public Class<?> getColumnClass(final int columnIndex) {
        return DataTypes.getDataDisplayClass(columnToDataType(columnIndex));
    }

    public int getColumnCount() {
        return columns.length;
    }

    public String getColumnName(final int columnIndex) {
        return DataTypes.getDataDisplayName(columnToDataType(columnIndex));
    }

    public int getRowCount() {
        return wpListModel.getSize();
    }

    private int columnToDataType(final int column) {
        if (column < columns.length) {
            return columns[column];
        } else {
            return DataTypes.NONE;
        }
    }

    public Object getValueAt(final int rowIndex, final int columnIndex) {
        return PositionData.getData(getElementAt(rowIndex), columnToDataType(columnIndex));
    }

    public final MapWaypoint getElementAt(final int rowIndex) {
        return (MapWaypoint) wpListModel.getElementAt(rowIndex);
    }

    public boolean isCellEditable(final int rowIndex, final int columnIndex) {
        return false;
    }

    public void setValueAt(final Object value, final int rowIndex, final int columnIndex) {
    }

    public final int getPreferredWidth(final FontMetrics fm, final int columnIndex) {
        return DataTypes.defaultDataWidth(columnToDataType(columnIndex), fm);
    }
}
