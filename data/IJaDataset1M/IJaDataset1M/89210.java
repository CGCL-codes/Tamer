package com.googlecode.maratische.google.gui;

import java.awt.Component;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

public class FeedTreeCellRenderer extends DefaultTreeCellRenderer {

    private static final long serialVersionUID = -1863261505049105435L;

    private Icon icon = new ImageIcon(getClass().getResource("/feed.png"));

    private Icon iconOfflineSupport = new ImageIcon(getClass().getResource("/feed_disk.png"));

    private Icon iconFolder = new ImageIcon(getClass().getResource("/feed_folder.png"));

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        Component component = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        FeedNode feedNode = (FeedNode) value;
        if (feedNode != null) {
            if (feedNode.isFolder()) {
                setIcon(iconFolder);
            } else if (feedNode.isOfflineSupport()) {
                setIcon(iconOfflineSupport);
            } else {
                setIcon(icon);
            }
        }
        return component;
    }
}
