package com.tomjudge.gui.tree;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

/**
 *
 * @author tj
 */
public class DoubleClickListener extends MouseAdapter {

    private JTree tree;

    /**
     * Create a new double click listener for the tree.
     * @param tree The tree this listener will be listening on.
     */
    public DoubleClickListener(JTree tree) {
        this.tree = tree;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int button = e.getButton();
        int clicks = e.getClickCount();
        if (button == MouseEvent.BUTTON1 && clicks == 2) {
            TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
            if (selPath != null) {
                Object n = selPath.getPathComponent(selPath.getPathCount() - 1);
                if (n instanceof IconNode) {
                    ((IconNode) n).doubleClicked();
                }
            }
        }
    }
}
