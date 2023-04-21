package com.orientechnologies.tools.oexplorer.menu;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

public class PopupListener extends MouseAdapter {

    public void mousePressed(MouseEvent e) {
        maybeShowPopup(e);
    }

    public void mouseReleased(MouseEvent e) {
        maybeShowPopup(e);
    }

    private void maybeShowPopup(MouseEvent e) {
        if (!e.isPopupTrigger()) return;
        Component comp = e.getComponent();
        if (comp instanceof JTree) handleTreeEvents(e); else if (comp instanceof javax.swing.JTabbedPane) {
            Component tab = ((javax.swing.JTabbedPane) comp).getSelectedComponent();
            if (tab == null) return;
            if (tab.getClass().getName().equals("com.orientechnologies.tools.oexplorer.views.ViewLog")) handleLogEvents(e);
        }
    }

    private void handleLogEvents(MouseEvent e) {
        LogMenu.getInstance().showPopup(null, e.getComponent(), e.getX(), e.getY());
    }

    private void handleTreeEvents(MouseEvent e) {
        JTree treObjects = (JTree) e.getComponent();
        TreePath path = treObjects.getClosestPathForLocation(e.getX(), e.getY());
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) treObjects.getLastSelectedPathComponent();
        if (node == null || !node.equals(selectedNode)) {
            treObjects.setSelectionPath(new TreePath(node.getPath()));
        }
        Object nodeInfo = node.getUserObject();
        ContextMenu ctxMenu = null;
        switch(node.getLevel()) {
            case 0:
                break;
            case 1:
                ctxMenu = DatabaseMenu.getInstance();
                break;
            case 2:
                if (node.getUserObject().toString().equals("Schema")) ctxMenu = SchemaMenu.getInstance();
                break;
            case 3:
                DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();
                if (parent.getUserObject().toString().equals("Schema")) ctxMenu = ClassMenu.getInstance(); else if (parent.getUserObject().toString().equals("Queries")) ctxMenu = QueryMenu.getInstance(); else if (parent.getUserObject().toString().equals("Dictionary")) ctxMenu = DictObjMenu.getInstance(); else if (parent.getUserObject().toString().equals("Directory")) ctxMenu = DirectoryObjMenu.getInstance();
                break;
            default:
                ctxMenu = ObjectMenu.getInstance();
                break;
        }
        if (ctxMenu != null) ctxMenu.showPopup(node.getUserObject(), treObjects, e.getX(), e.getY());
    }
}
