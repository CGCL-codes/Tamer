package jchessboard;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;

/**
 * @author cd
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
class GameTable extends javax.swing.JScrollPane {

    private Vector pgnList = new Vector();

    private Vector strList = new Vector();

    private GameTableModel tableModel;

    boolean tableChanged = false;

    private JTable jTable;

    private int currentGameIndex = 0;

    private int networkGameIndex = -1;

    class GameTableModel extends javax.swing.table.AbstractTableModel {

        public int getColumnCount() {
            return 8;
        }

        public int getRowCount() {
            return strList.size();
        }

        public int getGameCount() {
            return strList.size();
        }

        public String getColumnName(int col) {
            if (col == 0) return "#";
            if (col == 1) return "White";
            if (col == 2) return "Black";
            if (col == 3) return "Res.";
            if (col == 4) return "Date";
            if (col == 5) return "Event";
            if (col == 6) return "Site";
            if (col == 7) return "Rnd.";
            return null;
        }

        public Object getValueAt(int row, int col) {
            if (col == 0) {
                if (row == networkGameIndex) return "Net"; else return row + 1 + "";
            }
            PGN.STR str = (PGN.STR) strList.get(row);
            if (col == 1) return str.getTag("White");
            if (col == 2) return str.getTag("Black");
            if (col == 3) {
                if (str.getTag("Result").equals("1/2-1/2")) return "½-½"; else return str.getTag("Result");
            }
            if (col == 4) return str.getTag("Date");
            if (col == 5) return str.getTag("Event");
            if (col == 6) return str.getTag("Site");
            if (col == 7) return str.getTag("Round");
            return null;
        }
    }

    public int addGame(String pgnData, PGN.STR str) {
        pgnList.add(pgnData);
        strList.add(str);
        tableModel.fireTableChanged(new TableModelEvent(tableModel));
        int index = pgnList.size() - 1;
        jTable.setRowSelectionInterval(index, index);
        setColumnWidths();
        tableChanged = true;
        return index;
    }

    public String getPGN(int index) {
        return (String) pgnList.get(index);
    }

    public void setPGN(int index, String pgnData) {
        pgnList.set(index, pgnData);
        tableChanged = true;
    }

    public PGN.STR getSTR(int index) {
        return (PGN.STR) strList.get(index);
    }

    public void setSTR(int index, PGN.STR str) {
        strList.set(index, str);
        tableModel.fireTableChanged(new TableModelEvent(tableModel));
        tableChanged = true;
    }

    public void clear() {
        synchronized (this) {
            pgnList.clear();
            strList.clear();
            tableModel.fireTableChanged(new TableModelEvent(tableModel));
            tableChanged = false;
        }
        setColumnWidths();
    }

    public int getGameCount() {
        return pgnList.size();
    }

    public int getSelectedIndex() {
        return jTable.getSelectedRow();
    }

    public int getSelectionCount() {
        return jTable.getSelectedRowCount();
    }

    public void setSelectedIndex(int index) {
        jTable.setRowSelectionInterval(index, index);
        jTable.scrollRectToVisible(jTable.getCellRect(index, 0, true));
    }

    public void updateRow(int row) {
        tableModel.fireTableChanged(new TableModelEvent(tableModel, row));
    }

    public void removeGame(int index) {
        pgnList.remove(index);
        strList.remove(index);
        tableModel.fireTableChanged(new TableModelEvent(tableModel));
        tableChanged = true;
    }

    public int removeSelectedGames() {
        int removeIndex = getSelectedIndex();
        int count = getSelectionCount();
        int newGameIndex = currentGameIndex;
        for (int n = 0; n < count; n++) {
            removeGame(removeIndex);
            if (newGameIndex >= removeIndex) newGameIndex--;
        }
        if (getGameCount() == 0) {
            addGame("", new PGN.STR());
            return 0;
        } else {
            if (newGameIndex < 0) newGameIndex = 0;
            return newGameIndex;
        }
    }

    public GameTable() {
        super();
        tableModel = new GameTableModel();
        jTable = new JTable(tableModel);
        jTable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        setViewportView(jTable);
        jTable.setFont(new Font("SansSerrif", Font.PLAIN, 10));
        javax.swing.table.TableCellRenderer cellRenderer = new javax.swing.table.DefaultTableCellRenderer() {

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (row == currentGameIndex) setBackground(new Color(200, 200, 200)); else if (row == networkGameIndex) {
                    setBackground(new Color(200, 20, 20));
                    isSelected = false;
                } else setBackground(new Color(255, 255, 255));
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                return this;
            }
        };
        jTable.setDefaultRenderer((new Object()).getClass(), cellRenderer);
        jTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        setColumnWidths();
    }

    private void setColumnWidths() {
        jTable.getColumnModel().getColumn(0).setMaxWidth(30);
        jTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        jTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        jTable.getColumnModel().getColumn(3).setMaxWidth(50);
        jTable.getColumnModel().getColumn(4).setPreferredWidth(80);
        jTable.getColumnModel().getColumn(7).setMaxWidth(40);
    }

    /**
	 * @return
	 */
    public int getCurrentGameIndex() {
        return currentGameIndex;
    }

    /**
	 * @param i
	 */
    public void setCurrentGameIndex(int i) {
        currentGameIndex = i;
    }

    /**
	 * @return The JTable used to display the games.
	 */
    public JTable getJTable() {
        return jTable;
    }

    public void setEnabled(boolean enable) {
        super.setEnabled(enable);
        jTable.setEnabled(enable);
    }

    /**
	 * @return
	 */
    public int getNetworkGameIndex() {
        return networkGameIndex;
    }

    /**
	 * @param i
	 */
    public void setNetworkGameIndex(int i) {
        networkGameIndex = i;
    }
}
