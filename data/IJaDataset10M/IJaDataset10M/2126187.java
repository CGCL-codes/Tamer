package fr.soleil.bensikin.components.snapshot.detail;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Enumeration;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import fr.soleil.bensikin.actions.listeners.SnapshotCompareTableHeaderListener;
import fr.soleil.bensikin.components.renderers.SnapshotCompareRenderer;
import fr.soleil.bensikin.components.renderers.SnapshotCompareTableHeaderRenderer;
import fr.soleil.bensikin.components.renderers.SortedTableHeaderRenderer;
import fr.soleil.bensikin.data.snapshot.Snapshot;
import fr.soleil.bensikin.models.SnapshotCompareTablePrintModel;

/**
 * A JTable used to compare 2 Snapshots, that can take a variable set of
 * columns.
 * 
 * @author CLAISSE
 */
public class SnapshotCompareTable extends JTable {

    private static final long serialVersionUID = -5215843490069466540L;

    private SnapshotCompareTablePrintModel model;

    public SnapshotCompareTablePrintModel getModel() {
        return model;
    }

    /**
     * Builds a SnapshotCompareTable with the specified columns.
     */
    public SnapshotCompareTable() {
        super();
        this.setDefaultRenderer(Object.class, new SnapshotCompareRenderer());
        this.setColumnModel(new PersistentColumnModel());
        this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        this.setRowHeight(this.getRowHeight() + 2);
        this.setName("SnapshotCompareTable");
        getTableHeader().setDefaultRenderer(new SortedTableHeaderRenderer());
        this.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                if (rowAtPoint(e.getPoint()) == 0) {
                    MouseListener[] listeners = getTableHeader().getMouseListeners();
                    if (listeners != null) {
                        for (int i = 0; i < listeners.length; i++) {
                            if (listeners[i] instanceof SnapshotCompareTableHeaderListener) {
                                MouseEvent event = new MouseEvent(getTableHeader(), e.getID(), e.getWhen(), e.getModifiers(), e.getX(), e.getY(), e.getClickCount(), e.isPopupTrigger(), e.getButton());
                                listeners[i].mousePressed(event);
                                getTableHeader().repaint();
                            }
                        }
                    }
                }
                super.mousePressed(e);
            }
        });
        getTableHeader().setDefaultRenderer(new SnapshotCompareTableHeaderRenderer());
    }

    public boolean isCellEditable(int row, int column) {
        return false;
    }

    public boolean isColumnSelected(int column) {
        if (column == 0) {
            return false;
        }
        return super.isColumnSelected(column);
    }

    /**
     * Builds its model from <code>snapshot1</code> and <code>snapshot2</code>
     * 
     * @param snapshot1
     *            The 1st snapshot of the comparison
     * @param snapshot2
     *            The 2nd snapshot of the comparison
     */
    public void build(Snapshot snapshot1, Snapshot snapshot2) {
        model = new SnapshotCompareTablePrintModel(snapshot1, Snapshot.getFirstSnapshotOfComparisonTitle(), snapshot2, Snapshot.getSecondSnapshotOfComparisonTitle());
        model.build();
        JTableHeader header = this.getTableHeader();
        MouseListener[] listeners = header.getMouseListeners();
        if (listeners != null) {
            MouseListener[] copyListeners = new MouseListener[listeners.length];
            System.arraycopy(listeners, 0, copyListeners, 0, listeners.length);
            for (int i = 0; i < copyListeners.length; i++) {
                if (copyListeners[i] instanceof SnapshotCompareTableHeaderListener) {
                    header.removeMouseListener(copyListeners[i]);
                }
            }
        }
        this.setModel(model);
        this.setColumnsSize();
        header.addMouseListener(new SnapshotCompareTableHeaderListener(model));
    }

    /**
     * Presets its columns sizes
     */
    private void setColumnsSize() {
        TableColumnModel columnModel = this.getColumnModel();
        Enumeration<TableColumn> enumer = columnModel.getColumns();
        int i = 0;
        while (enumer.hasMoreElements()) {
            TableColumn nextCol = enumer.nextElement();
            String id = "" + nextCol.getIdentifier();
            this.setColumnId(nextCol, i);
            int size = 120;
            if (id.trim().equals("")) {
                size = 200;
            }
            nextCol.setPreferredWidth(size);
            i++;
        }
    }

    private void setColumnId(TableColumn nextCol, int columnIndex) {
        String colName = this.model.getColumnName(columnIndex);
        nextCol.setIdentifier(colName);
    }
}
