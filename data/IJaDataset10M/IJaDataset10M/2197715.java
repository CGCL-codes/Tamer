package org.javagroup.tools.processmanager;

import java.awt.*;
import java.util.*;
import org.javagroup.process.*;
import org.javagroup.util.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.TableModelEvent;

public class ProcessDataModel extends AbstractTableModel implements ProcessEventListener {

    protected Vector _processTableEntries;

    private static String __NAME = "Process name (including argument list)";

    /** column numbers */
    public static final int C_PID = 0, C_NAME = 1;

    public int numColumns = 2;

    public ProcessDataModel() {
        _processTableEntries = new Vector();
        ProcessManager manager = ProcessManagerHolder.getProcessManager();
        Enumeration processes = manager.getProcesses();
        while (processes.hasMoreElements()) createProcessTableEntry((JProcess) processes.nextElement());
        manager.addProcessEventListener(this);
    }

    protected void createProcessTableEntry(JProcess process) {
        ProcessTableEntry entry = new ProcessTableEntry(process);
        _processTableEntries.addElement(entry);
    }

    public int getColumnCount() {
        return numColumns;
    }

    public int getRowCount() {
        return _processTableEntries.size();
    }

    public Object getValueAt(int rowIndex, int colIndex) {
        if (rowIndex >= getRowCount()) return "";
        ProcessTableEntry entry = (ProcessTableEntry) _processTableEntries.elementAt(rowIndex);
        if (colIndex == C_PID) return String.valueOf(entry._pid);
        if (colIndex == C_NAME) return entry._name;
        return "NO DATA";
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex == C_PID) return true; else return false;
    }

    public void setValueAt(Object aValue, int rowIndex, int colIndex) {
        if (rowIndex > getRowCount()) return;
        ProcessTableEntry entry = (ProcessTableEntry) _processTableEntries.elementAt(rowIndex);
        ProcessManagerHolder.getProcessManager().kill(entry._pid);
        fireTableDataChanged();
    }

    /** This method _is_ in TableDataModel.	 */
    public String getColumnName(int columnIndex) {
        switch(columnIndex) {
            case C_PID:
                return "PID";
            case C_NAME:
                return __NAME;
            default:
                return "Undefined";
        }
    }

    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    public void processCreated(JProcess process) {
        createProcessTableEntry(process);
        fireTableDataChanged();
    }

    public void processDestroyed(JProcess process) {
        _processTableEntries.removeElement(new ProcessTableEntry(process));
        fireTableDataChanged();
    }
}

final class ProcessTableEntry {

    protected String _name;

    protected long _pid;

    public ProcessTableEntry(JProcess process) {
        _name = process.getName();
        _pid = process.getPid();
    }

    public boolean equals(Object o) {
        if (o instanceof ProcessTableEntry) {
            ProcessTableEntry entry = (ProcessTableEntry) o;
            return _pid == entry._pid;
        }
        return super.equals(o);
    }

    public int hashCode() {
        return (int) _pid;
    }
}
