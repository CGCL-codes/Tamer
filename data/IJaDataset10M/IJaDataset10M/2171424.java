package net.firefly.client.gui.swing.table.renderer;

import java.awt.Color;
import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import net.firefly.client.model.data.ColumnZero;

public class ColumnZeroRenderer extends DefaultTableCellRenderer {

    private static final long serialVersionUID = 898992358057884830L;

    protected static Border noFocusBorder = new EmptyBorder(0, 5, 0, 5);

    private static final Border SAFE_NO_FOCUS_BORDER = new EmptyBorder(0, 5, 0, 5);

    private Color unselectedForeground;

    private Color unselectedBackground;

    protected int alignment;

    protected boolean alternateRowColor;

    public ColumnZeroRenderer(int alignment, boolean alternateRowColor) {
        super();
        this.alignment = alignment;
        this.alternateRowColor = alternateRowColor;
    }

    private static Border getNoFocusBorder() {
        if (System.getSecurityManager() != null) {
            return SAFE_NO_FOCUS_BORDER;
        } else {
            return noFocusBorder;
        }
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) {
            super.setForeground(table.getSelectionForeground());
            super.setBackground(table.getSelectionBackground());
        } else {
            super.setForeground((unselectedForeground != null) ? unselectedForeground : table.getForeground());
            if (alternateRowColor) {
                if (row % 2 == 0) {
                    super.setBackground((unselectedBackground != null) ? unselectedBackground : table.getBackground());
                } else {
                    Color c = new Color(245, 245, 245);
                    super.setBackground(c);
                }
            } else {
                super.setBackground((unselectedBackground != null) ? unselectedBackground : table.getBackground());
            }
        }
        setFont(table.getFont());
        setBorder(getNoFocusBorder());
        setIcon((Icon) ((ColumnZero) value).getImageIcon());
        setHorizontalAlignment(alignment);
        return this;
    }
}
