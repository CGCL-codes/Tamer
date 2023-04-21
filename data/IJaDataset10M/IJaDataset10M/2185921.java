package fr.itris.glips.rtdaeditor.dbeditor;

import java.awt.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.border.*;
import fr.itris.glips.svgeditor.resources.*;

/**
 * @author ITRIS, Jordi SUC
 */
public class InsertedWidgetDataBaseCellRenderer implements TreeCellRenderer {

    /**
     * the colors
     */
    private Color textSelectionColor, textNonSelectionColor, backgroundSelectionColor, backgroundNonSelectionColor, borderSelectionColor;

    /**
     * the label that will be used for the rendering
     */
    private JLabel labelCmp = null;

    /**
     * whether the cell is selected or not
     */
    protected boolean selected = false;

    /**
     * whether the cell has focus or not
     */
    protected boolean hasFocus = false;

    /**
     * the constructor of the class
     * @param tree the tree that will use this renderer
     */
    public InsertedWidgetDataBaseCellRenderer(JTree tree) {
        textSelectionColor = UIManager.getColor("Tree.selectionForeground");
        textNonSelectionColor = UIManager.getColor("Tree.textForeground");
        backgroundSelectionColor = UIManager.getColor("Tree.selectionBackground");
        backgroundNonSelectionColor = UIManager.getColor("Tree.textBackground");
        borderSelectionColor = UIManager.getColor("Tree.selectionBorderColor");
        labelCmp = new JLabel() {

            @Override
            protected void paintComponent(Graphics g) {
                int paintedX = getIcon().getIconWidth() + getIconTextGap();
                if (selected) {
                    g.setColor(backgroundSelectionColor);
                } else {
                    g.setColor(backgroundNonSelectionColor);
                }
                g.fillRect(paintedX, 0, getWidth() - paintedX - 1, getHeight() - 1);
                super.paintComponent(g);
                if (hasFocus) {
                    g.setColor(borderSelectionColor);
                    g.drawRect(paintedX, 0, getWidth() - paintedX - 1, getHeight() - 1);
                }
            }
        };
        labelCmp.setBorder(new EmptyBorder(2, 2, 2, 2));
        labelCmp.setComponentOrientation(tree.getComponentOrientation());
    }

    /**
     * @see javax.swing.tree.TreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int, boolean)
     */
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFcs) {
        if (value != null && value instanceof InsertedWidgetDataBaseEditorTreeNode) {
            this.selected = sel;
            this.hasFocus = hasFcs;
            InsertedWidgetDataBaseEditorTreeNode node = (InsertedWidgetDataBaseEditorTreeNode) value;
            ImageIcon currentIcon = getIcon(node);
            labelCmp.setIcon(currentIcon);
            labelCmp.setText(value.toString());
            labelCmp.setToolTipText(node.getToolTipText());
        }
        return labelCmp;
    }

    /**
     * returning the icon corresponding to the given node
     * @param node a node
     * @return the icon corresponding to the given node
     */
    protected ImageIcon getIcon(InsertedWidgetDataBaseEditorTreeNode node) {
        ImageIcon icon = null;
        if (node != null) {
            icon = ResourcesManager.getIcon(node.getIconName(), false);
        }
        return icon;
    }

    /**
     * @return Returns the backgroundNonSelectionColor.
     */
    public Color getBackgroundNonSelectionColor() {
        return backgroundNonSelectionColor;
    }

    /**
     * @return Returns the backgroundSelectionColor.
     */
    public Color getBackgroundSelectionColor() {
        return backgroundSelectionColor;
    }

    /**
     * @return Returns the borderSelectionColor.
     */
    public Color getBorderSelectionColor() {
        return borderSelectionColor;
    }

    /**
     * @return Returns the textNonSelectionColor.
     */
    public Color getTextNonSelectionColor() {
        return textNonSelectionColor;
    }

    /**
     * @return Returns the textSelectionColor.
     */
    public Color getTextSelectionColor() {
        return textSelectionColor;
    }
}
