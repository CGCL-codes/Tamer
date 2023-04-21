package net.sourceforge.exclusive.client.gui.filetransferstatus.cellrenderer;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

public class TextCellRenderer extends JLabel implements TableCellRenderer {

    private static final long serialVersionUID = -8530970633991192447L;

    private Color selectionFGColor = null;

    private Color selectionBGColor = null;

    private Color fgColor = null;

    private Color bgColor = null;

    public TextCellRenderer(int alignment) {
        if (selectionFGColor == null) {
            selectionFGColor = UIManager.getColor("Table.selectionForeground");
            selectionBGColor = UIManager.getColor("Table.selectionBackground");
            fgColor = UIManager.getColor("Table.textForeground");
            bgColor = UIManager.getColor("Table.textBackground");
        }
        this.setHorizontalAlignment(alignment);
        this.setOpaque(true);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) {
            this.setBackground(selectionBGColor);
            this.setForeground(selectionFGColor);
        } else {
            this.setBackground(bgColor);
            this.setForeground(fgColor);
        }
        this.setText((String) value);
        return this;
    }
}
