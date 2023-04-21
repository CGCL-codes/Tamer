package ch.unibe.im2.inkanno.gui.tree;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import ch.unibe.im2.inkanno.Selection;

public class TreeListener implements MouseListener, TreeSelectionListener, MouseMotionListener {

    private static TreeListener instance;

    private JPopupMenu popup;

    private TreePath popupSelection;

    public static TreeListener getInstance(JPopupMenu popup) {
        if (instance == null) {
            instance = new TreeListener(popup);
        }
        return instance;
    }

    public static TreeListener getInstance() {
        if (instance == null) {
            throw new NullPointerException("No Popup specified");
        }
        return instance;
    }

    private TreeListener(JPopupMenu popup) {
        this.popup = popup;
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        maybeShowPopup(e);
    }

    public void mouseReleased(MouseEvent e) {
        maybeShowPopup(e);
    }

    private void maybeShowPopup(MouseEvent e) {
        if (e.isPopupTrigger()) {
            JTree tree = (JTree) e.getSource();
            this.popupSelection = tree.getClosestPathForLocation(e.getX(), e.getY());
            popup.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    public void valueChanged(TreeSelectionEvent e) {
        ((JTree) e.getSource()).scrollPathToVisible(e.getPath());
    }

    public void setSelectionContent(Selection s, JTree tree) {
    }

    public TreePath getPopUpSelection() {
        return popupSelection;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        System.err.println(e.getY());
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }
}
