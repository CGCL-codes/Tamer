package org.xblackcat.rojac.gui.component;

import org.xblackcat.rojac.util.LookupDelegate;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;
import java.util.Arrays;
import java.util.Collections;

/**
 * @author xBlackCat Date: 17.06.11
 */
public class ComplexTreeRenderer extends JLightPanel implements TreeCellRenderer {

    /**
     * Last tree the renderer was painted in.
     */
    protected JTree tree;

    /**
     * Is the value currently selected.
     */
    protected boolean selected;

    /**
     * True if has focus.
     */
    protected boolean hasFocus;

    /**
     * True if draws focus border around icon as well.
     */
    protected boolean drawsFocusBorderAroundIcon;

    /**
     * If true, a dashed line is drawn as the focus indicator.
     */
    protected boolean drawDashedFocusIndicator;

    /**
     * Background color of the tree.
     */
    private Color treeBGColor;

    /**
     * Color to draw the focus indicator in, determined from the background. color.
     */
    private Color focusBGColor;

    /**
     * Icon used to show non-leaf nodes that aren't expanded.
     */
    protected transient Icon closedIcon;

    /**
     * Icon used to show leaf nodes.
     */
    protected transient Icon leafIcon;

    /**
     * Icon used to show non-leaf nodes that are expanded.
     */
    protected transient Icon openIcon;

    /**
     * Color to use for the foreground for selected nodes.
     */
    protected Color textSelectionColor;

    /**
     * Color to use for the foreground for non-selected nodes.
     */
    protected Color textNonSelectionColor;

    /**
     * Color to use for the background when a node is selected.
     */
    protected Color backgroundSelectionColor;

    /**
     * Color to use for the background when the node isn't selected.
     */
    protected Color backgroundNonSelectionColor;

    /**
     * Color to use for the focus indicator when the node has focus.
     */
    protected Color borderSelectionColor;

    protected boolean isDropCell;

    protected boolean fillBackground = true;

    private JComponent components;

    public ComplexTreeRenderer(LayoutManager layout) {
        super(layout);
        drawDashedFocusIndicator = LookupDelegate.getBoolean(this, ui, "Tree.drawDashedFocusIndicator", false);
        fillBackground = LookupDelegate.getBoolean(this, ui, "Tree.rendererFillBackground", true);
        drawsFocusBorderAroundIcon = LookupDelegate.getBoolean(this, ui, "Tree.drawsFocusBorderAroundIcon", false);
        setOpaque(true);
        setLeafIcon(LookupDelegate.getIcon(this, ui, "Tree.leafIcon"));
        setClosedIcon(LookupDelegate.getIcon(this, ui, "Tree.closedIcon"));
        setOpenIcon(LookupDelegate.getIcon(this, ui, "Tree.openIcon"));
        setTextSelectionColor(LookupDelegate.getColor(this, ui, "Tree.selectionForeground"));
        setTextNonSelectionColor(LookupDelegate.getColor(this, ui, "Tree.textForeground"));
        setBackgroundSelectionColor(LookupDelegate.getColor(this, ui, "Tree.selectionBackground"));
        setBackgroundNonSelectionColor(LookupDelegate.getColor(this, ui, "Tree.textBackground"));
        setBorderSelectionColor(LookupDelegate.getColor(this, ui, "Tree.selectionBorderColor"));
        Insets margins = LookupDelegate.getInsets(this, ui, "Tree.rendererMargins");
        if (margins == null) {
            margins = new Insets(0, 0, 0, 0);
        }
        setBorder(new MutableBorder(margins));
        setName("Tree.cellRenderer");
        components = new JComponentDelegate(Collections.<JComponent>emptyList());
    }

    protected void setDelegatedComponents(JComponent... components) {
        this.components = new JComponentDelegate(Arrays.asList(components));
    }

    protected JComponent getDelegateComponent() {
        return components;
    }

    /**
     * Configures the renderer based on the passed in components. The value is set from messaging the tree with
     * <code>convertValueToText</code>, which ultimately invokes <code>toString</code> on <code>value</code>. The
     * foreground color is set based on the selection and the icon is set based on the <code>leaf</code> and
     * <code>expanded</code> parameters.
     */
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        this.tree = tree;
        this.hasFocus = hasFocus;
        this.selected = sel;
        Color fg = null;
        isDropCell = false;
        JTree.DropLocation dropLocation = tree.getDropLocation();
        if (dropLocation != null && dropLocation.getChildIndex() == -1 && tree.getRowForPath(dropLocation.getPath()) == row) {
            Color col = LookupDelegate.getColor(this, ui, "Tree.dropCellForeground");
            if (col != null) {
                fg = col;
            } else {
                fg = getTextSelectionColor();
            }
            isDropCell = true;
        } else if (sel) {
            fg = getTextSelectionColor();
        } else {
            fg = getTextNonSelectionColor();
        }
        setForeground(fg);
        components.setForeground(fg);
        setOpaque(true);
        Icon icon = null;
        if (leaf) {
            icon = getLeafIcon();
        } else if (expanded) {
            icon = getOpenIcon();
        } else {
            icon = getClosedIcon();
        }
        if (!tree.isEnabled()) {
            setEnabled(false);
            components.setEnabled(false);
            LookAndFeel laf = UIManager.getLookAndFeel();
            Icon disabledIcon = laf.getDisabledIcon(tree, icon);
            if (disabledIcon != null) {
                icon = disabledIcon;
            }
            setDisabledIcon(icon);
        } else {
            setEnabled(true);
            components.setEnabled(true);
            setIcon(icon);
        }
        setComponentOrientation(tree.getComponentOrientation());
        Color bColor;
        if (isDropCell) {
            bColor = LookupDelegate.getColor(this, ui, "Tree.dropCellBackground");
            if (bColor == null) {
                bColor = getBackgroundSelectionColor();
            }
        } else if (sel) {
            bColor = getBackgroundSelectionColor();
        } else {
            bColor = getBackgroundNonSelectionColor();
            if (bColor == null) {
                bColor = getBackground();
            }
        }
        setBackground(bColor);
        components.setBackground(bColor);
        return this;
    }

    protected void setIcon(Icon icon) {
    }

    protected void setDisabledIcon(Icon icon) {
    }

    /**
     * Returns the default icon, for the current laf, that is used to represent non-leaf nodes that are expanded.
     */
    public Icon getDefaultOpenIcon() {
        return LookupDelegate.getIcon(this, ui, "Tree.openIcon");
    }

    /**
     * Returns the default icon, for the current laf, that is used to represent non-leaf nodes that are not expanded.
     */
    public Icon getDefaultClosedIcon() {
        return LookupDelegate.getIcon(this, ui, "Tree.closedIcon");
    }

    /**
     * Returns the default icon, for the current laf, that is used to represent leaf nodes.
     */
    public Icon getDefaultLeafIcon() {
        return LookupDelegate.getIcon(this, ui, "Tree.leafIcon");
    }

    /**
     * Sets the icon used to represent non-leaf nodes that are expanded.
     */
    public void setOpenIcon(Icon newIcon) {
        openIcon = newIcon;
    }

    /**
     * Returns the icon used to represent non-leaf nodes that are expanded.
     */
    public Icon getOpenIcon() {
        return openIcon;
    }

    /**
     * Sets the icon used to represent non-leaf nodes that are not expanded.
     */
    public void setClosedIcon(Icon newIcon) {
        closedIcon = newIcon;
    }

    /**
     * Returns the icon used to represent non-leaf nodes that are not expanded.
     */
    public Icon getClosedIcon() {
        return closedIcon;
    }

    /**
     * Sets the icon used to represent leaf nodes.
     */
    public void setLeafIcon(Icon newIcon) {
        leafIcon = newIcon;
    }

    /**
     * Returns the icon used to represent leaf nodes.
     */
    public Icon getLeafIcon() {
        return leafIcon;
    }

    /**
     * Sets the color the text is drawn with when the node is selected.
     */
    public void setTextSelectionColor(Color newColor) {
        textSelectionColor = newColor;
    }

    /**
     * Returns the color the text is drawn with when the node is selected.
     */
    public Color getTextSelectionColor() {
        return textSelectionColor;
    }

    /**
     * Sets the color the text is drawn with when the node isn't selected.
     */
    public void setTextNonSelectionColor(Color newColor) {
        textNonSelectionColor = newColor;
    }

    /**
     * Returns the color the text is drawn with when the node isn't selected.
     */
    public Color getTextNonSelectionColor() {
        return textNonSelectionColor;
    }

    /**
     * Sets the color to use for the background if node is selected.
     */
    public void setBackgroundSelectionColor(Color newColor) {
        backgroundSelectionColor = newColor;
    }

    /**
     * Returns the color to use for the background if node is selected.
     */
    public Color getBackgroundSelectionColor() {
        return backgroundSelectionColor;
    }

    /**
     * Sets the background color to be used for non selected nodes.
     */
    public void setBackgroundNonSelectionColor(Color newColor) {
        backgroundNonSelectionColor = newColor;
    }

    /**
     * Returns the background color to be used for non selected nodes.
     */
    public Color getBackgroundNonSelectionColor() {
        return backgroundNonSelectionColor;
    }

    /**
     * Sets the color to use for the border.
     */
    public void setBorderSelectionColor(Color newColor) {
        borderSelectionColor = newColor;
    }

    /**
     * Returns the color the border is drawn.
     */
    public Color getBorderSelectionColor() {
        return borderSelectionColor;
    }

    /**
     * Subclassed to map <code>FontUIResource</code>s to null. If <code>font</code> is null, or a
     * <code>FontUIResource</code>, this has the effect of letting the font of the JTree show through. On the other
     * hand, if <code>font</code> is non-null, and not a <code>FontUIResource</code>, the font becomes
     * <code>font</code>.
     */
    public void setFont(Font font) {
        if (font instanceof FontUIResource) {
            font = null;
        }
        super.setFont(font);
    }

    /**
     * Gets the font of this component.
     *
     * @return this component's font; if a font has not been set for this component, the font of its parent is returned
     */
    public Font getFont() {
        Font font = super.getFont();
        if (font == null && tree != null) {
            font = tree.getFont();
        }
        return font;
    }

    /**
     * Subclassed to map <code>ColorUIResource</code>s to null. If <code>color</code> is null, or a
     * <code>ColorUIResource</code>, this has the effect of letting the background color of the JTree show through. On
     * the other hand, if <code>color</code> is non-null, and not a <code>ColorUIResource</code>, the background becomes
     * <code>color</code>.
     */
    public void setBackground(Color color) {
        super.setBackground(color);
    }

    /**
     * Overrides <code>JComponent.getPreferredSize</code> to return slightly wider preferred size value.
     */
    public Dimension getPreferredSize() {
        Dimension retDimension = super.getPreferredSize();
        if (retDimension != null) {
            retDimension = new Dimension(retDimension.width + 3, retDimension.height);
        }
        return retDimension;
    }

    protected class MutableBorder extends EmptyBorder {

        public MutableBorder(Insets margins) {
            super(margins.top + 1, margins.left + 1, margins.bottom + 1, margins.right + 1);
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            if (!selected) {
                return;
            }
            Color bsColor = getBorderSelectionColor();
            Color notColor = getBackgroundSelectionColor();
            if (bsColor != null && (selected || !drawDashedFocusIndicator)) {
                g.setColor(bsColor);
                g.drawRect(x, y, width - 1, height - 1);
            }
            if (drawDashedFocusIndicator && notColor != null) {
                if (treeBGColor != notColor) {
                    treeBGColor = notColor;
                    focusBGColor = new Color(~notColor.getRGB());
                }
                g.setColor(focusBGColor);
                BasicGraphicsUtils.drawDashedRect(g, x, y, width, height);
            }
        }
    }
}
