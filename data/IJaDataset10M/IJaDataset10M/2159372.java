package mainview;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 * Cell renderer used to display icons and tooltips for the JXTable
 * in Mainview
 */
public class MainviewTableCellRenderer implements TableCellRenderer {

    private ImageIcon icon;

    private JLabel label;

    private String toolTip;

    public MainviewTableCellRenderer(ImageIcon icon, String toolTip) {
        this.icon = icon;
        this.label = new JLabel(icon);
        this.toolTip = toolTip;
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        label.setToolTipText(toolTip);
        return label;
    }
}
