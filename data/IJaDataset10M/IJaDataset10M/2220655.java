package net.sf.refactorit.ui.treetable;

import net.sf.refactorit.ui.treetable.writer.TableFormat;
import net.sf.refactorit.ui.treetable.writer.TableLayout;

/**
 * TreeTableModel for JTreeTable component
 *
 * @author  Anton Safonov
 */
public class BinTreeTableModel extends AbstractTreeTableModel {

    protected static final String NUMBER_PADDING = "  ";

    public BinTreeTableModel(final Object root) {
        super(root);
    }

    /**
   * Returns the number of available column.
   */
    public int getColumnCount() {
        return 1;
    }

    /**
   * Returns the name for column number <code>column</code>.
   */
    public String getColumnName(final int column) {
        return "Target";
    }

    /**
   * To be overriden.
   */
    public boolean isShowing(final int column) {
        return true;
    }

    public Object getChild(final Object node, final int num) {
        return ((ParentTreeTableNode) node).getChildAt(num);
    }

    public int getChildCount(final Object node) {
        return ((ParentTreeTableNode) node).getChildCount();
    }

    /**
   * Returns the value to be displayed for node <code>node</code>,
   * at column number <code>column</code>.
   */
    public Object getValueAt(final Object node, final int column) {
        return node;
    }

    public final void showHiddenRows() {
        BinTreeTableNode rootNode = ((BinTreeTableNode) getRoot());
        rootNode.setShowHiddenChildren(true);
        if (rootNode.getSecondaryText() != null) {
            rootNode.reflectLeafNumberToParentName();
        }
        fireSomethingChanged();
    }

    public final void hideHiddenRows() {
        BinTreeTableNode rootNode = ((BinTreeTableNode) getRoot());
        rootNode.setShowHiddenChildren(false);
        if (rootNode.getSecondaryText() != null) {
            rootNode.reflectLeafNumberToParentName();
        }
        fireSomethingChanged();
    }

    public final void selectAll() {
        ((ParentTreeTableNode) getRoot()).setSelected(true);
        fireSomethingChanged();
    }

    public final void deselectAll() {
        ((ParentTreeTableNode) getRoot()).setSelected(false);
        fireSomethingChanged();
    }

    /**
   * Gives text for entire model, without accounting for selection;
   * for selection, use BinTreeTable.getClipboardText();
   */
    public final String getClipboardText(TableFormat format) {
        return TableLayout.getClipboardText(format, this, null).toString();
    }

    public final int getVisibleColumnsCount() {
        int result = 0;
        for (int i = 0; i < getColumnCount(); i++) {
            if (isShowing(i)) {
                result++;
            }
        }
        return result;
    }
}
