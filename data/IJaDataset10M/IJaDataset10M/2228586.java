package systeminformationmonitor.swing.model;

import java.util.List;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;
import systeminformationmonitor.system.object.ProcessObject;

/**
 * Process table model is the Swing table model for the process table.
 * @author Leo Xu
 */
public class ProcessTableModel extends AbstractTableModel {

    private Vector<ProcessObject> rowData;

    private String columnNames[] = { "Process", "User Name", "CPU", "Memory", "Description" };

    private static ProcessTableModel instance = new ProcessTableModel();

    private ProcessTableModel() {
        rowData = new Vector<ProcessObject>();
    }

    public static ProcessTableModel getInstance() {
        return instance;
    }

    public void setRowData(List<ProcessObject> pList) {
        synchronized (String.class) {
            rowData = new Vector(pList);
        }
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public int getRowCount() {
        return rowData.size();
    }

    @Override
    public Object getValueAt(int row, int column) {
        ProcessObject obj = rowData.get(row);
        switch(column) {
            case 0:
                return obj.getProcessName();
            case 1:
                return obj.getprocessCredentialName();
            case 2:
                return obj.getProcessCPUPercentage();
            case 3:
                return obj.getRssSize();
            case 4:
                return obj.getProcessDescription();
            default:
                return "";
        }
    }

    @Override
    public Class getColumnClass(int column) {
        synchronized (String.class) {
            if (rowData.size() == 0) {
                return String.class;
            }
            return (getValueAt(0, column).getClass());
        }
    }
}
