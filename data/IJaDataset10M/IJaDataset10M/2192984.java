package surfer.yahoohd.desktop;

import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.ArrayList;

/**
 * created on: 2007-10-17 by tzvetan
 */
public class HDTableModel extends AbstractTableModel {

    private List<StockData> tableData;

    public HDTableModel() {
        tableData = new ArrayList<StockData>();
    }

    public void add(StockData row) {
        tableData.add(row);
    }

    public int getRowCount() {
        return tableData == null ? 0 : tableData.size();
    }

    public int getColumnCount() {
        return StockData.getColumnCount();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex > getRowCount()) {
            return "";
        }
        StockData row = tableData.get(rowIndex);
        switch(columnIndex) {
            case 0:
                return row.getSymbol();
            case 1:
                return row.getName();
            case 2:
                return row.getDate();
            case 3:
                return row.getOpen();
            case 4:
                return row.getHigh();
            case 5:
                return row.getLow();
            case 6:
                return row.getClose();
            case 7:
                return row.getAdjClose();
            case 8:
                return row.getVolume();
            default:
                return "";
        }
    }

    public String getColumnName(int column) {
        return StockData.getColumnName(column);
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
}
