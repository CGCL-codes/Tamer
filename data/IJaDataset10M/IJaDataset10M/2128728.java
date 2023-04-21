package timemyproject;

import java.awt.Font;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

public class TimeTableView extends JScrollPane {

    private String projectName;

    private int projectID;

    private double projectCredit;

    private double projectTaxes;

    private JTable table;

    private TimeTableModel tModel;

    public TimeTableView(final TimeDatabase db, int projectID, String projectName, double projectCredit, double projectTaxes) {
        this.projectID = projectID;
        this.projectName = projectName;
        this.projectCredit = projectCredit;
        this.projectTaxes = projectTaxes;
        tModel = new TimeTableModel(db, projectID);
        table = new JTable();
        table.setModel(tModel);
        hideTableColumn(0);
        hideTableColumn(1);
        table.getColumnModel().getColumn(2).setMinWidth(75);
        table.getColumnModel().getColumn(2).setMaxWidth(75);
        table.getColumnModel().getColumn(3).setMinWidth(75);
        table.getColumnModel().getColumn(3).setMaxWidth(75);
        table.getColumnModel().getColumn(4).setMinWidth(75);
        table.getColumnModel().getColumn(4).setMaxWidth(75);
        table.getColumnModel().getColumn(5).setMinWidth(200);
        table.getColumnModel().getColumn(5).setMaxWidth(200);
        table.getTableHeader().setReorderingAllowed(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setShowGrid(false);
        table.setDefaultRenderer(Object.class, new TimeTableCellRenderer());
        tModel.addTableModelListener(new TableModelListener() {

            public void tableChanged(TableModelEvent e) {
                if ((e.getSource() instanceof TimeTableModel) && e.getColumn() != -1) {
                    TimeTableModel t = (TimeTableModel) e.getSource();
                    int i = (Integer) t.getValueAt(e.getFirstRow(), 0);
                    TimeObject f = db.getTimeObject(i);
                    f.setDescription(t.getValueAt(e.getFirstRow(), 6).toString());
                    db.editTimeRecord(f);
                }
            }
        });
        this.setViewportView(table);
    }

    private void hideTableColumn(int col) {
        table.getColumnModel().getColumn(col).setWidth(0);
        table.getColumnModel().getColumn(col).setMaxWidth(0);
        table.getColumnModel().getColumn(col).setMinWidth(0);
        table.getColumnModel().getColumn(col).setPreferredWidth(0);
        table.getColumnModel().getColumn(col).setResizable(false);
        table.getTableHeader().resizeAndRepaint();
    }

    public int getSelectedID() {
        int x = table.getSelectedRow();
        if (x != -1) {
            return (Integer) table.getModel().getValueAt(x, 0);
        } else {
            return -1;
        }
    }

    public TimeObject getSelectedElement() {
        TimeTableModel ttv = (TimeTableModel) table.getModel();
        return ttv.getElementAt(table.getSelectedRow());
    }

    public void updateEntry(TimeObject t) {
        tModel.updateEntry(t);
    }

    public void addEntry(TimeObject t) {
        tModel.addEntry(t);
    }

    public void removeEntry(TimeObject t) {
        tModel.removeEntry(t);
    }

    public int getProjectID() {
        return projectID;
    }

    public String getProjectName() {
        return projectName;
    }

    public double getProjectCredit() {
        return projectCredit;
    }

    public double getProjectTaxes() {
        return projectTaxes;
    }
}
