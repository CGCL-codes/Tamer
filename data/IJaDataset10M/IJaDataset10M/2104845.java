package clubmixer.client.ui.songtable;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Alexander Schindler
 */
public class ButtonCellRenderer implements TableCellRenderer {

    private JButton button = new JButton();

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        button.setText(value.toString());
        Dimension size = new Dimension(16, 16);
        button.setSize(size);
        button.setMaximumSize(size);
        button.setMinimumSize(size);
        button.setPreferredSize(size);
        return button;
    }
}
