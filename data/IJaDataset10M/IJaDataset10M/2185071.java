package tcpmon.gui;

import java.util.ResourceBundle;
import javax.swing.table.AbstractTableModel;

/**
 * @author Chris Boesch
 * 
 * Created on 17.11.2007
 */
public class TableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;

    private static final int MAX_COLS = 6;

    private String[][] data;

    ResourceBundle bundle = ResourceBundle.getBundle("tcpmon.gui.Resources");

    public TableModel() {
        data = new String[0][0];
    }

    public void setData(String[][] data) {
        this.data = data;
    }

    @Override
    public int getColumnCount() {
        return MAX_COLS - 1;
    }

    @Override
    public String getColumnName(int col) {
        switch(col) {
            case 0:
                return "Process";
            case 1:
                return "Protocol";
            case 2:
                return "Local Address";
            case 3:
                return "Remote Address";
            case 4:
                return "State";
            default:
                return null;
        }
    }

    @Override
    public int getRowCount() {
        if (data == null) return 0; else return data.length;
    }

    @Override
    public Object getValueAt(int row, int col) {
        if (data == null) return null;
        if (col > MAX_COLS - 1) return null;
        if (row < data.length) {
            return data[row][col];
        }
        return null;
    }
}
