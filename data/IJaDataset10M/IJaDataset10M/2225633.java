package org.akrogen.tkui.gui.swing.widgets.trees;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import org.akrogen.tkui.core.dom.ITkuiWidgetElement;
import org.akrogen.tkui.core.gui.widgets.AbstractGuiWidget;
import org.akrogen.tkui.core.gui.widgets.IGuiOptions;
import org.akrogen.tkui.core.gui.widgets.IGuiWidget;
import org.akrogen.tkui.core.gui.widgets.graphics.Font;
import org.akrogen.tkui.core.gui.widgets.graphics.RGBValue;
import org.akrogen.tkui.core.gui.widgets.trees.IGuiTree;
import org.akrogen.tkui.core.gui.widgets.trees.IGuiTreeCell;
import org.akrogen.tkui.core.gui.widgets.trees.IGuiTreeRow;
import org.akrogen.tkui.core.gui.widgets.trees.models.GuiTreeRowModel;

/**
 * GUI TreeRow implemented into Swing.
 * 
 * @version 1.0.0
 * @author <a href="mailto:angelo.zerr@gmail.com">Angelo ZERR</a>
 * 
 */
public class SwingGuiTreeRowImpl extends AbstractGuiWidget implements IGuiTreeRow {

    private DefaultMutableTreeNode treeNode;

    private Font font;

    private RGBValue backgroundColor;

    public Object buildWidget(Object parent, IGuiOptions options) {
        DefaultMutableTreeNode root = null;
        if (parent instanceof JTree) {
            JTree tree = (JTree) parent;
            root = (DefaultMutableTreeNode) tree.getModel().getRoot();
        } else {
            if (parent instanceof DefaultMutableTreeNode) {
                root = (DefaultMutableTreeNode) parent;
            }
        }
        if (root != null) {
            treeNode = new DefaultMutableTreeNode(new GuiTreeRowModel(this));
            root.add(treeNode);
        }
        return treeNode;
    }

    public Object getWidget() {
        return treeNode;
    }

    public Object getLayoutData() {
        return null;
    }

    public void setLayoutData(Object layoutData) {
    }

    public boolean hasBeenTreat() {
        return true;
    }

    public void addListener(ITkuiWidgetElement element, int guiEventId, String eventAttrName) {
    }

    public void addCell(IGuiTreeCell cell) {
    }

    public void addRow(IGuiTreeRow row) {
    }

    public IGuiTreeCell getCell(int index) {
        return null;
    }

    public IGuiTreeRow[] getChildren() {
        return null;
    }

    public IGuiWidget getParent() {
        return null;
    }

    public boolean hasChildren() {
        return false;
    }

    public void setParent(IGuiWidget guiWidget) {
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public RGBValue getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(RGBValue backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setExpanded(boolean expanded) {
    }

    public IGuiTree getGuiTree() {
        return null;
    }
}
