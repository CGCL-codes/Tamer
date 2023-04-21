package app.wing;

import java.util.Collection;
import javax.swing.table.AbstractTableModel;
import wing.WingUser;

@SuppressWarnings("serial")
public class UserTableModel extends AbstractTableModel {

    private static final String USER = "入場者一覧";

    private static final String[] header = { USER };

    private static final int[] columnWidth = { 250 };

    private WingUser[] userArray;

    public UserTableModel() {
        userArray = new WingUser[0];
    }

    public WingUser getUser(int index) {
        return userArray[index];
    }

    public void clear() {
        userArray = new WingUser[0];
        fireTableDataChanged();
    }

    public void setUser(Collection<WingUser> userSet) {
        int size = userSet.size();
        userArray = new WingUser[size];
        int i = 0;
        for (WingUser u : userSet) {
            userArray[size - 1 - i] = u;
            i += 1;
        }
        fireTableDataChanged();
    }

    public int[] getColumnWidth() {
        return columnWidth;
    }

    @Override
    public String getColumnName(int column) {
        return header[column];
    }

    public int getColumnCount() {
        return header.length;
    }

    public int getRowCount() {
        return userArray.length;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch(columnIndex) {
            case 0:
                return WingUser.class;
            default:
                return Object.class;
        }
    }

    public Object getValueAt(int row, int column) {
        WingUser user = userArray[row];
        Object o;
        switch(column) {
            case 0:
                o = user;
                break;
            default:
                o = "Unknown";
                break;
        }
        return o;
    }
}
