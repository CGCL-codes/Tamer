package at.nullpointer.trayrss.gui.tray;

import javax.swing.table.AbstractTableModel;
import at.nullpointer.trayrss.configuration.ReferenceCollection;
import at.nullpointer.trayrss.configuration.feeds.FeedTable;

public class FeedTableModel extends AbstractTableModel {

    FeedTable table = ReferenceCollection.FEED_TABLE;

    Object[] columnNames = ReferenceCollection.CONFIG_TABLE_HEADER;

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public int getRowCount() {
        return table.getTable().length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return table.getTable()[rowIndex][columnIndex];
    }

    public boolean isCellEditable(int row, int column) {
        return (column != 0);
    }

    public String getColumnName(int column) {
        return (String) columnNames[column];
    }

    public Class getColumnClass(int column) {
        Class erg = null;
        switch(column) {
            case 0:
                erg = Long.class;
                break;
            case 1:
                erg = String.class;
                break;
            case 2:
                erg = String.class;
                break;
            case 3:
                erg = Long.class;
                break;
            case 4:
                erg = Boolean.class;
                break;
            default:
                erg = String.class;
        }
        return erg;
    }

    public void setValueAt(Object value, int row, int column) {
        table.getTable()[row][column] = value;
    }
}
