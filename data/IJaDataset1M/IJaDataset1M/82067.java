package org.yccheok.jstock.gui.table;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import org.yccheok.jstock.gui.JStockOptions;
import org.yccheok.jstock.gui.MainFrame;
import org.yccheok.jstock.gui.portfolio.CommentableContainer;

/**
 *
 * @author yccheok
 */
public class GenericRenderer extends DefaultTableCellRenderer {

    protected Color getBackgroundColor(int row) {
        final JStockOptions jStockOptions = MainFrame.getInstance().getJStockOptions();
        if (row % 2 == 0) {
            return jStockOptions.getFirstRowBackgroundColor();
        }
        return jStockOptions.getSecondRowBackgroundColor();
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        TableModel tableModel = table.getModel();
        if (tableModel instanceof CommentableContainer) {
            CommentableContainer commentableContainer = (CommentableContainer) tableModel;
            int index = table.convertRowIndexToModel(row);
            String comment = commentableContainer.getCommentable(index).getComment();
            if (comment.length() > 0) {
                ((JComponent) c).setToolTipText(org.yccheok.jstock.gui.Utils.toHTML(comment));
            } else {
                ((JComponent) c).setToolTipText(null);
            }
        }
        if (isSelected || hasFocus) {
            return c;
        }
        final JStockOptions jStockOptions = MainFrame.getInstance().getJStockOptions();
        c.setForeground(jStockOptions.getNormalTextForegroundColor());
        c.setBackground(getBackgroundColor(row));
        return c;
    }
}
