package gnu.fishingcat;

import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import java.text.SimpleDateFormat;
import java.awt.Dimension;

/**
 * Given an MModel as a tablemodel, provides a table
 * displaying them.
 *
 *
 * Created: Thu May 27 11:13:39 2004
 *
 * @author <a href="mailto:cbierner@localhost">Constance Bierner</a>
 * @version 1.0
 */
public class MTable extends JTable {

    JPopupMenu popup = new JPopupMenu();

    public MTable() {
        setRowSelectionAllowed(true);
        setShowVerticalLines(false);
        setIntercellSpacing(new Dimension(0, 0));
        AbstractAction view_action = new AbstractAction("View") {

            public void actionPerformed(ActionEvent e) {
                try {
                    view();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        AbstractAction edit_action = new AbstractAction("Edit") {

            public void actionPerformed(ActionEvent e) {
                try {
                    edit();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        AbstractAction delete_action = new AbstractAction("Delete") {

            public void actionPerformed(ActionEvent e) {
                try {
                    delete();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        popup.add(new JMenuItem(view_action));
        popup.add(new JMenuItem(edit_action));
        popup.add(new JMenuItem(delete_action));
        addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                try {
                    if (e.getClickCount() == 2 && !e.isPopupTrigger()) {
                        view();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    popup.show(MTable.this, e.getX(), e.getY());
                }
            }

            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    popup.show(MTable.this, e.getX(), e.getY());
                }
            }
        });
    }

    public void view() throws Exception {
        int row = getSelectedRow();
        if (row == -1) return;
        Object obj = getModel().getValueAt(row, 0);
        getMModel().viewItem(obj);
    }

    public void edit() throws Exception {
        int row = getSelectedRow();
        if (row == -1) return;
        Object obj = getModel().getValueAt(row, 0);
        getMModel().editItem(obj);
    }

    public void delete() throws Exception {
        int row = getSelectedRow();
        if (row == -1) return;
        Object obj = getModel().getValueAt(row, 0);
        getMModel().deleteItem(obj);
        revalidate();
    }

    public void setMModel(MModel model) {
        TableSorter sorter = new TableSorter(model);
        setModel(sorter);
        sorter.setTableHeader(getTableHeader());
        if (getColumnModel().getColumnCount() > 0) {
            removeColumn(getColumnModel().getColumn(0));
        }
        model.setColumnWidths(MTable.this);
    }

    public MModel getMModel() {
        TableSorter sorter = (TableSorter) getModel();
        MModel model = (MModel) sorter.getTableModel();
        return model;
    }

    public TableCellRenderer getCellRenderer(int row, int column) {
        TableSorter sorter = (TableSorter) getModel();
        MModel model = (MModel) sorter.getTableModel();
        return model.getCellRenderer(row, column);
    }
}
