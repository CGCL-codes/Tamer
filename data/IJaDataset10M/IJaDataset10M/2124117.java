package de.axxeed.ajatt.gui.misc;

import java.awt.event.ActionEvent;
import java.sql.Time;
import java.util.Date;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import org.apache.log4j.Logger;
import com.utils.swing.table.ExcelTable;
import de.axxeed.ajatt.model.TaskTime;

/**
 * TimeViewTable.java
 * Created: 06.03.2009 10:38:19
 * @author Markus J. Luzius
 * 
 */
public class TimeViewTable extends ExcelTable {

    private static Logger log = Logger.getLogger(TimeViewTable.class);

    private static final long serialVersionUID = 6340017731072648979L;

    public TimeViewTable(TaskTimeTableModel dm) {
        super(dm, "task view table");
        setDefaultRenderer(Date.class, new TimeRenderer());
        setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer());
        getColumnModel().getColumn(0).setPreferredWidth(250);
        getColumnModel().getColumn(1).setCellRenderer(new DateRenderer());
        setDefaultEditor(Date.class, new TimeEditor());
        setDefaultEditor(Time.class, new TimeEditor());
    }

    protected JPopupMenu createPopupMenu() {
        JPopupMenu menu = new JPopupMenu();
        menu.add(ACTION_EXCEL);
        menu.add(ACTION_JOIN);
        menu.add(ACTION_REMOVE);
        return menu;
    }

    protected final Action ACTION_JOIN = new AbstractAction("join") {

        private static final long serialVersionUID = -4277205720335143300L;

        public void actionPerformed(ActionEvent e) {
            if (getSelectedRowCount() > 1) {
                int[] index = getSelectedRows();
                String task = null;
                for (int i = 0; i < index.length; i++) {
                    if (task == null) {
                        task = (String) getModel().getValueAt(index[i], 0);
                        log.debug("Joining times for task <" + task + ">");
                    } else if (!task.equals((String) getModel().getValueAt(index[i], 0))) {
                        log.warn("Could not join different tasks!");
                        return;
                    }
                }
                log.debug("Performing join...");
                TaskTime firstRow = ((TaskTimeTableModel) getModel()).getRow(index[0]);
                firstRow.setEndTime(((TaskTimeTableModel) getModel()).getRow(index[index.length - 1]).getEndTime());
                for (int i = 1; i < index.length; i++) {
                    ((TaskTimeTableModel) getModel()).getRow(index[i]).remove();
                }
                ((TaskTimeTableModel) getModel()).removeRows(index[1], index[index.length - 1]);
            }
        }
    };

    protected final Action ACTION_REMOVE = new AbstractAction("remove") {

        private static final long serialVersionUID = 7522764305119710680L;

        public void actionPerformed(ActionEvent e) {
            if (getSelectedRowCount() > 0) {
                int result = JOptionPane.showConfirmDialog(null, "Are you sure?", "Confirm deletion", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (result == JOptionPane.YES_OPTION) {
                    int[] index = getSelectedRows();
                    log.debug("Performing remove...");
                    for (int i = 0; i < index.length; i++) {
                        ((TaskTimeTableModel) getModel()).getRow(index[i]).remove();
                    }
                    ((TaskTimeTableModel) getModel()).removeRows(index[0], index[index.length - 1]);
                }
            }
        }
    };
}
