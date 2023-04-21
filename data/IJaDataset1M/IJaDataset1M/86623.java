package org.plantstreamer.tablecellrenderer;

import java.awt.Component;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import org.pushingpixels.substance.api.renderers.SubstanceDefaultTreeCellRenderer;
import org.plantstreamer.treetable.node.AbstractOPCTreeTableNode;
import org.plantstreamer.treetable.node.BranchTreeTableNode;
import org.plantstreamer.treetable.node.CompositeTreeTableNode;
import swingextras.icons.IconManager;

/**
 * A tree cell renderer for OPC items
 * @author Joao Leal
 */
@SuppressWarnings("serial")
public class OPCTreeCellRenderer extends SubstanceDefaultTreeCellRenderer {

    private static final ImageIcon server = IconManager.getIcon("16x16/filesystems/server.png");

    private static final ImageIcon closed = IconManager.getIcon("16x16/filesystems/folder.png");

    private static final ImageIcon open = IconManager.getIcon("16x16/filesystems/folder_open.png");

    private static final ImageIcon opcItemOpen = IconManager.getIcon("16x16/opc/opc_item_open.png");

    private static final ImageIcon opcItemClosed = IconManager.getIcon("16x16/opc/opc_item_closed.png");

    private static final ImageIcon opcPropertyOpen = IconManager.getIcon("16x16/opc/opc_property_open.png");

    private static final ImageIcon opcPropertyClosed = IconManager.getIcon("16x16/opc/opc_property_closed.png");

    private static final ImageIcon opcItem = IconManager.getIcon("16x16/opc/opc_item.png");

    private static final ImageIcon opcProperty = IconManager.getIcon("16x16/opc/opc_property.png");

    private static final ImageIcon compositeItem = IconManager.getIcon("16x16/opc/opc_composite_item.png");

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        AbstractOPCTreeTableNode node = (AbstractOPCTreeTableNode) value;
        Object nodeData = node.getUserObject();
        JLabel label = (JLabel) super.getTreeCellRendererComponent(tree, nodeData != null ? nodeData : "", selected, expanded, leaf, row, hasFocus);
        if (node.getParent() == null) {
            label.setIcon(server);
        } else {
            if (node instanceof BranchTreeTableNode) {
                BranchTreeTableNode branch = (BranchTreeTableNode) node;
                if (expanded) {
                    if (branch.getExpandedNodeIcon() == null) {
                        label.setIcon(open);
                    } else {
                        label.setIcon(branch.getExpandedNodeIcon());
                    }
                } else {
                    if (branch.getColapsedNodeIcon() == null) {
                        label.setIcon(closed);
                    } else {
                        label.setIcon(branch.getColapsedNodeIcon());
                    }
                }
            } else {
                if (node instanceof CompositeTreeTableNode) {
                    label.setIcon(compositeItem);
                } else if (node.isOPCitemProperty()) {
                    if (node.isLeaf()) {
                        label.setIcon(opcProperty);
                    } else {
                        if (expanded) {
                            label.setIcon(opcPropertyOpen);
                        } else {
                            label.setIcon(opcPropertyClosed);
                        }
                    }
                } else {
                    if (node.isLeaf()) {
                        label.setIcon(opcItem);
                    } else {
                        if (expanded) {
                            label.setIcon(opcItemOpen);
                        } else {
                            label.setIcon(opcItemClosed);
                        }
                    }
                }
            }
        }
        if (node.containsSelectedChilds() || node.isSelected()) {
            label.setFont(label.getFont().deriveFont(Font.BOLD));
        } else {
            label.setFont(label.getFont().deriveFont(Font.PLAIN));
        }
        return label;
    }
}
