package org.vikamine.swing.subgroup.editors.tuningtable;

import java.awt.Component;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

public class TuningTableCellRenderer extends DefaultTableCellRenderer {

    private static final long serialVersionUID = -6328668153503543391L;

    public static final int SCROLL_TYPE = 0;

    public static final int LOCKED_TYPE = 1;

    int type;

    public TuningTableCellRenderer() {
        super();
        setHorizontalAlignment(SwingConstants.CENTER);
    }

    public TuningTableCellRenderer(int type) {
        this.type = type;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (type == LOCKED_TYPE && column == 0) {
            JButton normalButton = new JButton();
            normalButton.setMargin(new Insets(0, 0, 0, 0));
            normalButton.setHorizontalAlignment(SwingConstants.CENTER);
            Border border = UIManager.getBorder("TableHeader.cellBorder");
            normalButton.setBorder(border);
            JTableHeader header = table.getTableHeader();
            normalButton.setForeground(header.getForeground());
            normalButton.setBackground(header.getBackground());
            normalButton.setFont(header.getFont());
            normalButton.getModel().setPressed(true);
            normalButton.getModel().setArmed(true);
            normalButton.setText((value == null) ? "" : value.toString());
            return normalButton;
        }
        setHorizontalAlignment(SwingConstants.CENTER);
        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }
}
