package jstudio.gui.generic;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTable;
import jstudio.db.DatabaseObject;

public class PopupListener<Context extends DatabaseObject> extends MouseAdapter {

    private JTable table;

    private ContextualMenu<Context> popup;

    public PopupListener(JTable table, ContextualMenu<Context> popup) {
        this.table = table;
        this.popup = popup;
    }

    public void mousePressed(MouseEvent e) {
        maybeShowPopup(e);
    }

    public void mouseReleased(MouseEvent e) {
        maybeShowPopup(e);
    }

    private void maybeShowPopup(MouseEvent e) {
        if (e.isPopupTrigger()) {
            int row = table.rowAtPoint(e.getPoint());
            if (row >= 0) {
                table.setRowSelectionInterval(row, row);
                int mrow = table.convertRowIndexToModel(row);
                @SuppressWarnings("unchecked") Context c = (Context) table.getModel().getValueAt(mrow, 0);
                popup.setContext(c);
                popup.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }
}
