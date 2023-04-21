package kr.ac.ssu.imc.whitehole.report.designer.dialogs;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;

public class QResultsCellRenderer implements TableCellRenderer {

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value instanceof JComboBox) {
            ((JComboBox) value).setBackground(Color.lightGray);
        }
        return (JComponent) value;
    }
}
