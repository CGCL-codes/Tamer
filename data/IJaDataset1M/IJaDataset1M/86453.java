package de.renier.jkeepass.renderer;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * JLabelTableRenderer
 *
 * @author <a href="mailto:software@renier.de">Renier Roth</a>
 */
public class JLabelTableRenderer extends DefaultTableCellRenderer {

    private static final long serialVersionUID = -6280341937090695521L;

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        JLabel label = (JLabel) value;
        this.setText(label.getText());
        if (label.getIcon() != null) {
            this.setIcon(label.getIcon());
        }
        return this;
    }
}
