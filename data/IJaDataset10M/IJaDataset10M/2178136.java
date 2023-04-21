package net.sf.refactorit.standalone;

import net.sf.refactorit.jsp.JspUtil;
import net.sf.refactorit.ui.tree.JClassTree;
import net.sf.refactorit.ui.tree.SourceNode;
import net.sf.refactorit.ui.tree.TypeNode;
import net.sf.refactorit.ui.tree.UITreeNode;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.tree.TreePath;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class BrowserTreeMouseListener extends MouseAdapter {

    private JClassTree tree;

    private JBrowserPanel panel;

    public BrowserTreeMouseListener(JClassTree tree, JBrowserPanel panel) {
        this.tree = tree;
        this.panel = panel;
    }

    public void mousePressed(MouseEvent e) {
        int row = tree.getRowForLocation(e.getX(), e.getY());
        if (row < 0) {
            return;
        }
        TreePath path = tree.getPathForLocation(e.getX(), e.getY());
        if (path == null) {
            return;
        }
        Object object = path.getLastPathComponent();
        if (object instanceof SourceNode) {
            SourceNode node = (SourceNode) object;
            int line = 1;
            if (node.getStart() == null) {
                path = path.getParentPath();
                if (path.getLastPathComponent() instanceof SourceNode) {
                    SourceNode newNode = (SourceNode) path.getLastPathComponent();
                    if (newNode.getStart() != null) {
                        line = newNode.getStart().getLine();
                    }
                }
            } else {
                line = node.getStart().getLine();
            }
            panel.show(node.getCompilationUnit(), line, (node instanceof TypeNode.Member));
        }
        if (e.isPopupTrigger()) {
            popup(e);
            return;
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger()) {
            popup(e);
            return;
        }
    }

    private void popup(MouseEvent e) {
        TreePath[] paths = tree.getSelectionPaths();
        if (paths == null || paths.length == 0) {
            return;
        }
        Point point = SwingUtilities.convertPoint(tree, e.getPoint(), SwingUtilities.getWindowAncestor(panel));
        JPopupMenu menu = null;
        int size = paths.length;
        Object[] bins = new Object[size];
        if (paths.length == 1) {
            TreePath path = tree.getPathForLocation(e.getX(), e.getY());
            if (path == null) {
                return;
            }
            tree.setSelectionPath(path);
            Object node = path.getLastPathComponent();
            bins[0] = ((UITreeNode) node).getBin();
        } else {
            for (int i = 0; i < size; i++) {
                bins[i] = ((UITreeNode) paths[i].getLastPathComponent()).getBin();
            }
        }
        boolean isJsp = JspUtil.containsJSPNodes(bins);
        if (isJsp) {
            menu = null;
        } else {
            menu = panel.popupManager.getPopupMenu(bins, point);
        }
        if (menu != null) {
            int y = e.getY();
            int popupH = (int) menu.getPreferredSize().getHeight() + 30;
            int availablePopupY = panel.getHeight() - point.y;
            if (availablePopupY < popupH) {
                int availableScreenH = (int) panel.getToolkit().getScreenSize().getHeight() - (panel.getLocationOnScreen().y + panel.getHeight());
                if (availablePopupY + availableScreenH < popupH) {
                    int offset = popupH - (availablePopupY + availableScreenH);
                    y -= offset;
                }
            }
            menu.show(e.getComponent(), e.getX(), y);
            menu.requestFocus();
        }
    }

    public void filterJSPActions(JPopupMenu param0) {
    }
}
