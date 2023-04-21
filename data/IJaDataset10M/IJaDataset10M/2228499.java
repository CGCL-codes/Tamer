package org.xnap.commons.gui.util;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.JTree;

/**
 * Provides a mouse event listener that displays a popup menu if the user
 * presses the platform dependent popup button.
 */
public class PopupListener extends MouseAdapter {

    private JPopupMenu popup;

    public PopupListener(JMenu menu) {
        this(menu.getPopupMenu());
    }

    public PopupListener(JPopupMenu popup) {
        this.popup = popup;
        popup.setBorderPainted(true);
    }

    public PopupListener() {
    }

    public void mousePressed(MouseEvent e) {
        showPopup(e);
    }

    public void mouseReleased(MouseEvent e) {
        showPopup(e);
    }

    private void showPopup(MouseEvent e) {
        if (e.isPopupTrigger()) {
            if (!makeSelection(e.getComponent(), e.getPoint(), e.isControlDown())) {
                return;
            }
            e.getComponent().requestFocus();
            showPopup(e.getComponent(), e.getX(), e.getY());
        }
    }

    /**
	 * Shows the popup menu.
	 *
	 * @see GUIHelper#showPopupMenu(JPopupMenu, Component, int, int)
	 */
    protected void showPopup(Component source, int x, int y) {
        JPopupMenu popupMenu = getPopupMenu();
        if (popupMenu != null) {
            GUIHelper.showPopupMenu(popupMenu, source, x, y);
        }
    }

    public JPopupMenu getPopupMenu() {
        return popup;
    }

    private boolean makeSelection(Object parent, Point point, boolean multiple) {
        if (parent instanceof JTable) {
            JTable table = (JTable) parent;
            int row = table.rowAtPoint(point);
            if (row == -1) return false;
            int[] rows = table.getSelectedRows();
            boolean selectRow = true;
            for (int i = 0; i < rows.length; i++) {
                if (rows[i] == row) selectRow = false;
            }
            if (selectRow) {
                table.getSelectionModel().setSelectionInterval(row, row);
            }
        } else if (parent instanceof JTree) {
            JTree tree = (JTree) parent;
            int row = tree.getClosestRowForLocation(point.x, point.y);
            if (row == -1) return false;
            if (multiple) tree.addSelectionRow(row); else tree.setSelectionRow(row);
        }
        return true;
    }
}
