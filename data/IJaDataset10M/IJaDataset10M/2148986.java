package es.aeat.eett.infoRubik.deploy;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.dnd.DragSource;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Action;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import swingUtil.Tamemasa.tree.CheckNode;

/**
 * @author f00992
 *
 * Esta Clase gestiona los eventos del raton originados en jTreeDisponibles
 *
 */
class CheckNodeMouseTrigger extends MouseAdapter {

    private JTree jTreeDisponibles;

    private JTree jTreeElegidas;

    private Action actionAniadir;

    private boolean moviendo;

    CheckNodeMouseTrigger(MasterDeployPanel panel) {
        jTreeDisponibles = panel.getJTreeDisponibles();
        jTreeElegidas = panel.getJTreeElegidas();
        actionAniadir = panel.getActionAniadir();
    }

    public void mouseClicked(MouseEvent e) {
        if ((e.getButton() == MouseEvent.BUTTON1)) {
            if (e.getSource() instanceof JTree) {
                JTree tree = (JTree) e.getSource();
                int x = e.getX();
                int y = e.getY();
                int row = tree.getRowForLocation(x, y);
                TreePath path = tree.getPathForRow(row);
                if (path != null) {
                    CheckNode node = (CheckNode) path.getLastPathComponent();
                    boolean isSelected = !(node.isSelected());
                    node.setSelected(isSelected);
                    ((DefaultTreeModel) tree.getModel()).nodeChanged(node);
                    if (row == 0) {
                        tree.revalidate();
                        tree.repaint();
                    }
                }
            }
        }
    }

    public void mousePressed(MouseEvent e) {
        if ((e.getButton() == MouseEvent.BUTTON1) && (e.getClickCount() < 2)) {
            if (e.getSource() instanceof JTree) {
                JTree tree = (JTree) e.getSource();
                int x = e.getX();
                int y = e.getY();
                int row = tree.getRowForLocation(x, y);
                TreePath path = tree.getPathForRow(row);
                if (path != null) {
                    CheckNode lastItem = (CheckNode) path.getLastPathComponent();
                    actionAniadir.setEnabled((lastItem.isLeaf() || lastItem.getLevel() != 2) ? false : true);
                    if (actionAniadir.isEnabled()) {
                        moviendo = true;
                        ((JTree) e.getSource()).setCursor(DragSource.DefaultMoveDrop);
                    }
                }
            }
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (moviendo && (e.getButton() == MouseEvent.BUTTON1) && (e.getClickCount() < 2)) {
            if (e.getSource() instanceof JTree) {
                JTree tree = (JTree) e.getSource();
                TreePath selPath = jTreeDisponibles.getSelectionPath();
                if (moviendo && selPath != null) {
                    if (soltoInTreeElegidas(e) && actionAniadir.isEnabled()) {
                        actionAniadir.actionPerformed(null);
                    }
                }
                tree.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                moviendo = false;
            }
        }
    }

    private boolean soltoInTreeElegidas(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        Point pDisponibles = jTreeDisponibles.getLocationOnScreen();
        Point pElegidas = jTreeElegidas.getLocationOnScreen();
        x = (int) (pDisponibles.getX() + x);
        y = (int) (pDisponibles.getY() + y);
        x = (int) (x - pElegidas.getX());
        y = (int) (y - pElegidas.getY());
        if (jTreeElegidas.contains(x, y)) return true; else return false;
    }
}
