package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.sql.Date;
import java.sql.SQLException;
import javax.swing.DefaultCellEditor;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableRowSorter;
import state.ProgramState;
import storage.DB;
import general.Ban;
import general.CustomRowColorCellRenderer;

/**
 * @author Pat
 *
 *	This class further modularizes the GUI and keeps all the local database table code in this class
 */
@SuppressWarnings("serial")
public class LocalBansTable extends JTable {

    public DefaultTableModel tableModel = new DefaultTableModel();

    public TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<DefaultTableModel>(tableModel);

    public String preEditValue;

    public LocalBansTable localBansTable = this;

    DB database;

    ProgramState programState = ProgramState.instance();

    /**
	 * Constructor
	 */
    public LocalBansTable() {
        database = DB.instance();
        this.setModel(tableModel);
        this.setRowSorter(sorter);
        this.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        this.setDefaultRenderer(Object.class, new CustomRowColorCellRenderer());
        this.setIntercellSpacing(new Dimension(1, 1));
        this.setShowHorizontalLines(false);
        this.setShowVerticalLines(true);
        this.setGridColor(Color.lightGray);
        tableModel.addColumn("Type");
        tableModel.addColumn("Banned Player");
        tableModel.addColumn("Ban Reason");
        tableModel.addColumn("Ban Date");
        tableModel.addColumn("Last Update");
        addListeners();
    }

    /**
	 * This method determines whether or not a cell is editable
	 * @param row  --Row to be checked (pointless)
	 * @param column  --Column to be checked, starts at 1
	 * @return boolean  --True if the cell is editable, false otherwise
	 */
    public boolean isCellEditable(int row, int column) {
        if (column == 1 || column == 2) {
            return true;
        } else {
            return false;
        }
    }

    /**
	 * This method determines the cell editor for a particular cell and also sets the preEditValue so that the previous data can be recovered
	 * @param row  --Row in the table
	 * @param column  --Column in the table
	 * @return TableCellEditor  --The TableCellEditor used by this cell
	 */
    public TableCellEditor getCellEditor(int row, int column) {
        preEditValue = (String) tableModel.getValueAt(row, column);
        return new DefaultCellEditor(new JTextField());
    }

    /**
	 * Adds a new row to the localDB
	 * 
	 * @param type  --Type of ban 0 = info, 1 = ban
	 * @param player  --The name of the player
	 * @param reason  --The reason for the ban
	 * @param date  --The date this ban was created
	 * @param date2  --The date the ban was last updated (if this is new, today)
	 */
    public void addBanRow(final String type, final String player, final String reason, final String date, final String date2) {
        Runnable ud = new Runnable() {

            public void run() {
                tableModel.addRow(new Object[] { type, player, reason, date, date2 });
            }
        };
        SwingUtilities.invokeLater(ud);
    }

    /**
	 * Updates the row in the table model specified by the row variable
	 * 
	 * @param row  --Row to be updated
	 * @param type  --New ban type
	 * @param player  --New playername
	 * @param reason  --New ban reason
	 * @param date  --Should be the same as the original
	 * @param update  --New last update date
	 * @throws ArrayIndexOutOfBoundsException  --If the row is not in the current table model
	 */
    public void updateBanRow(final int row, final String type, final String player, final String reason, final Date date, final Date update) throws ArrayIndexOutOfBoundsException {
        Runnable ud = new Runnable() {

            public void run() {
                try {
                    tableModel.setValueAt(type, row, 0);
                    tableModel.setValueAt(player, row, 1);
                    tableModel.setValueAt(reason, row, 2);
                    tableModel.setValueAt(date, row, 3);
                    tableModel.setValueAt(update, row, 4);
                } catch (ArrayIndexOutOfBoundsException e) {
                }
            }
        };
        SwingUtilities.invokeLater(ud);
    }

    /**
	 * This method adds the listeners to their objects.  This just keeps the listeners in one place and keeps the code clean.
	 * MUST BE EXECUTED IN THE CONSTRUCTOR
	 */
    private void addListeners() {
        tableModel.addTableModelListener(new TableModelListener() {

            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    int row = e.getFirstRow();
                    String columnName = tableModel.getColumnName(e.getColumn());
                    if (columnName.equalsIgnoreCase("Banned Player")) {
                        if (!Ban.isValidName((String) tableModel.getValueAt(row, e.getColumn()))) {
                            System.err.println("Can't change to invalid battle.net name");
                            JOptionPane.showMessageDialog(null, "Invalid battle.net name to change to");
                            if (Ban.isValidName(preEditValue)) tableModel.setValueAt(preEditValue, row, e.getColumn()); else {
                                tableModel.setValueAt(Ban.makeValid(preEditValue), row, e.getColumn());
                            }
                            return;
                        }
                        try {
                            database.editPerson(database.getPID(preEditValue), (String) tableModel.getValueAt(row, e.getColumn()));
                        } catch (SQLException ef) {
                            JOptionPane.showMessageDialog(null, "Could not change the name of the banned player");
                            ef.printStackTrace();
                        }
                    } else if (columnName.equalsIgnoreCase("Ban Reason")) {
                        int idiotID = -1;
                        int banID = -1;
                        try {
                            idiotID = database.getPID((String) tableModel.getValueAt(row, 1));
                            banID = database.getBanID(idiotID, programState.getBnetName(), 1);
                        } catch (SQLException efg) {
                            efg.printStackTrace();
                        }
                        database.editBan(banID, (String) tableModel.getValueAt(row, e.getColumn()));
                    }
                }
            }
        });
    }

    /**
	 * A renderer for the table
	 *
	 */
    public class MyHeaderRenderer extends DefaultTableCellRenderer {

        public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focused, int row, int column) {
            super.getTableCellRendererComponent(table, value, selected, focused, row, column);
            setBorder(UIManager.getBorder("TableHeader.cellBorder"));
            return this;
        }
    }

    /**
	 *  Sets a new default renderer that contains the new color information and updates the UI
	 */
    public void refreshTables() {
        localBansTable.setDefaultRenderer(Object.class, new CustomRowColorCellRenderer());
        localBansTable.updateUI();
    }

    /**
	 * finds the row with a player given the players name
	 * @param playerName --name of the player's row to delete
	 * @param type -- "Ban" or "Info"
	 */
    public int findPlayer(String playerName, String type) {
        for (int i = 0; i < tableModel.getRowCount() && i != -1; i++) {
            if (((String) (tableModel.getValueAt(i, 0))).equals(type) && ((String) (tableModel.getValueAt(i, 1))).equalsIgnoreCase(playerName)) {
                return i;
            }
        }
        return -1;
    }
}
