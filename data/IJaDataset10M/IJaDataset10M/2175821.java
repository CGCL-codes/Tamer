package net.sf.hipster.gui;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellRenderer;

/**
 * A cell renderer for the text column in {@link HipsterTable}.
 */
@SuppressWarnings("serial")
public class HipsterTableTextCellRenderer extends JTextArea implements TableCellRenderer {

    /**
     * Instantiates a new hipster table text cell renderer.
     */
    public HipsterTableTextCellRenderer() {
        setLineWrap(true);
        setWrapStyleWord(true);
        setAlignmentY(TOP_ALIGNMENT);
        setOpaque(false);
    }

    public Component getTableCellRendererComponent(JTable table, Object obj, boolean isSelected, boolean hasFocus, int row, int column) {
        setText((String) obj);
        setSize(table.getColumnModel().getColumn(column).getWidth(), 0);
        getUI().getRootView(this).setSize(getWidth(), 0f);
        int wantedHeight = (int) getPreferredSize().getHeight() + table.getRowMargin();
        if (wantedHeight != table.getRowHeight(row)) table.setRowHeight(row, wantedHeight);
        return this;
    }
}
