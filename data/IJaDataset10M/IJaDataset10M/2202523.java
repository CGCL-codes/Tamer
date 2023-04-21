package uk.org.dbmm.model;

import java.util.Vector;
import java.util.Enumeration;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

/**
 * @author tpartrid
 *
 */
public class CommandRoster extends AbstractTableModel {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private Vector<RosterLine> lines = new Vector<RosterLine>();

    private int command;

    /**
	 * 
	 */
    public CommandRoster(int command, Roster roster) {
        super();
        this.command = command;
        addTableModelListener((TableModelListener) roster.getRosterStats());
    }

    public int getColumnCount() {
        return 9;
    }

    public int getRowCount() {
        return lines.size();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        return ((RosterLine) lines.elementAt(rowIndex)).getColumn(columnIndex);
    }

    public String getColumnName(int columnIndex) {
        return RosterLine.getColumnName(columnIndex);
    }

    public void addLine(ListItem item) {
        boolean lineFound = false;
        Enumeration<RosterLine> _e = lines.elements();
        while (_e.hasMoreElements()) {
            RosterLine _l = _e.nextElement();
            if (_l.isSame(item)) {
                _l.increment();
                lineFound = true;
                break;
            }
        }
        if (lineFound == false) {
            RosterLine newLine = new RosterLine(item);
            lines.add(newLine);
        }
        fireTableDataChanged();
    }

    public void addLine(RosterLine line) {
        lines.add(line);
        fireTableDataChanged();
    }

    public void removeRow(int rowIndex) {
        lines.remove(rowIndex);
    }

    public RosterLine getLine(int row) {
        return (RosterLine) lines.elementAt(row);
    }

    public void clear() {
        lines.clear();
    }

    public int getCommandId() {
        return command;
    }
}
