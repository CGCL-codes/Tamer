package com.quickrss.view.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import com.quickrss.controller.Main;
import com.quickrss.model.RSSFeed;
import com.quickrss.model.RSSTag;

public class DynamicTreeCellRenderer extends JPanel implements TreeCellRenderer {

    private JLabel lblTreeNodeName;

    private JLabel lblRSSItemsCount;

    public DynamicTreeCellRenderer() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        lblTreeNodeName = new JLabel() {

            /**
			 * paint is subclassed to draw the background correctly. JLabel currently
			 * does not allow backgrounds other than white, and it will also fill behind
			 * the icon. Something that isn't desirable.
			 */
            public void paint(Graphics g) {
                Color bColor;
                Icon currentI = getIcon();
                if (selected) bColor = SelectedBackgroundColor; else if (getParent() != null) bColor = getParent().getBackground(); else bColor = getBackground();
                g.setColor(bColor);
                if (currentI != null && getText() != null) {
                    int offset = (currentI.getIconWidth() + getIconTextGap());
                    if (getComponentOrientation().isLeftToRight()) {
                        g.fillRect(offset, 0, getWidth() - 1 - offset, getHeight() - 1);
                    } else {
                        g.fillRect(0, 0, getWidth() - 1 - offset, getHeight() - 1);
                    }
                } else g.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
                super.paint(g);
            }
        };
        add(lblTreeNodeName);
        lblRSSItemsCount = new JLabel();
        add(lblRSSItemsCount);
    }

    /** Icon to use when the item is collapsed. */
    protected static ImageIcon collapsedIcon;

    /** Icon to use when the item is expanded. */
    protected static ImageIcon expandedIcon;

    /** Color to use for the background when selected. */
    protected static final Color SelectedBackgroundColor = new Color(221, 221, 221);

    static {
        try {
            collapsedIcon = new ImageIcon(DynamicTreeCellRenderer.class.getResource("/resources/images/collapsed.gif"));
            expandedIcon = new ImageIcon(DynamicTreeCellRenderer.class.getResource("/resources/images/expanded.gif"));
        } catch (Exception e) {
            System.out.println("Couldn't load images: " + e);
        }
    }

    /** Whether or not the item that was last configured is selected. */
    protected boolean selected;

    /**
	 * This is messaged from JTree whenever it needs to get the size of the
	 * component or it wants to draw it. This attempts to set the font based on
	 * value, which will be a TreeNode.
	 */
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        String stringValue = tree.convertValueToText(value, selected, expanded, leaf, row, hasFocus);
        lblTreeNodeName.setText(stringValue);
        setToolTipText(stringValue);
        if (expanded) lblTreeNodeName.setIcon(expandedIcon); else if (!leaf) lblTreeNodeName.setIcon(collapsedIcon); else lblTreeNodeName.setIcon(null);
        if (value instanceof DynamicTreeNode) {
            DynamicTreeNodeData userObject = (DynamicTreeNodeData) ((DefaultMutableTreeNode) value).getUserObject();
            if (userObject.getDataObject() instanceof RSSTag) {
                RSSTag tag = (RSSTag) userObject.getDataObject();
                if (expanded) {
                    Main.getInstance().loadTag(tag);
                }
                userObject.setItemCountLoaded(tag.getItemCountLoaded());
            } else if (userObject.getDataObject() instanceof RSSFeed) {
                RSSFeed feed = (RSSFeed) userObject.getDataObject();
                if (expanded) {
                    Main.getInstance().loadFeed(feed);
                }
                userObject.setItemCountLoaded(feed.getItemCountLoaded());
            } else {
                userObject.setItemCountLoaded(0);
            }
            lblRSSItemsCount.setText("(" + userObject.getItemCountLoaded() + ")");
        }
        this.selected = selected;
        return this;
    }
}
