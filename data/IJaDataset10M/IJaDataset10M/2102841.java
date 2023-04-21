package ch.intertec.storybook.toolkit.swing.table;

import ch.intertec.storybook.model.Idea;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import net.miginfocom.swing.MigLayout;

public class IdeasStatusTableCellRenderer extends StandardTableCellRenderer {

    private static final long serialVersionUID = -8379642766225630012L;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Idea.Status status = (Idea.Status) value;
        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        JPanel panel = new JPanel(new MigLayout("insets 0 1 0 1"));
        panel.setOpaque(true);
        panel.setBackground(label.getBackground());
        label.setText(status.toString());
        Color color = status.getColor();
        JLabel lbColor = new JLabel();
        lbColor.setBackground(color);
        lbColor.setOpaque(true);
        lbColor.setPreferredSize(new Dimension(20, 10));
        panel.add(lbColor);
        panel.add(label);
        return panel;
    }
}
