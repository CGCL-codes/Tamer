package org.jimcat.gui.albumlist;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

/**
 * Creates the Popupmenu for the TagTree
 * 
 * $Id: TagTreePopupHandler.java 202 2007-04-10 06:14:13Z 07g1t1u1 $
 * 
 * @author Herbert
 */
public class AlbumTreePopupHandler extends MouseAdapter implements KeyListener {

    /**
	 * the tree this hanlder is installed to
	 */
    private JTree tree;

    /**
	 * create a new handler for the given tree
	 * 
	 * @param tree
	 */
    public AlbumTreePopupHandler(JTree tree) {
        this.tree = tree;
    }

    /**
	 * showes a popup
	 * 
	 * @param menu
	 * @param x
	 * @param y
	 */
    private void showPopup(JPopupMenu menu, int x, int y) {
        if (menu != null) {
            menu.show(tree, x, y);
        }
    }

    /**
	 * 
	 * react on a mouse released - prepaiere Popup menu if it was a popup
	 * trigger
	 * 
	 * @see java.awt.event.MouseAdapter#mouseReleased(java.awt.event.MouseEvent)
	 */
    @Override
    public void mouseReleased(MouseEvent e) {
        checkForPopup(e);
    }

    /**
	 * react on a mouse pressed - prepaiere Popup menu if it was a popup trigger
	 * 
	 * @see java.awt.event.MouseAdapter#mousePressed(java.awt.event.MouseEvent)
	 */
    @Override
    public void mousePressed(MouseEvent e) {
        checkForPopup(e);
    }

    /**
	 * 
	 * test if popup is triggered and show it
	 * 
	 * @param e
	 */
    private void checkForPopup(MouseEvent e) {
        if (e.isPopupTrigger()) {
            TreePath path = tree.getClosestPathForLocation(e.getX(), e.getY());
            tree.setSelectionPath(path);
            AlbumTreeNode node = (AlbumTreeNode) path.getLastPathComponent();
            showPopup(node.getPopupMenu(), e.getX(), e.getY());
        }
    }

    /**
	 * To react on the context_menu key
	 * 
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_CONTEXT_MENU) {
            TreePath path = tree.getSelectionPath();
            AlbumTreeNode node = (AlbumTreeNode) path.getLastPathComponent();
            Rectangle rectangle = tree.getPathBounds(path);
            showPopup(node.getPopupMenu(), rectangle.x + 40, rectangle.y + 10);
        }
    }

    /**
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
    @SuppressWarnings("unused")
    public void keyReleased(KeyEvent e) {
    }

    /**
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
    @SuppressWarnings("unused")
    public void keyTyped(KeyEvent e) {
    }
}
