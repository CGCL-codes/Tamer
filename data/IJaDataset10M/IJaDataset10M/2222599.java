package org.gvsig.gui.beans.swing.cellrenderers;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.LayoutManager;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import org.jfree.layout.CenterLayout;

/**
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 */
public class BooleanTableCellRenderer extends DefaultTableCellRenderer {

    private static final long serialVersionUID = 2121615214282741840L;

    private JCheckBox chk;

    private boolean isBordered;

    private MatteBorder selectedBorder;

    private MatteBorder unselectedBorder;

    public BooleanTableCellRenderer(boolean bordered) {
        this.isBordered = bordered;
        setOpaque(true);
    }

    public JCheckBox getCheck() {
        return chk;
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value == null) return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (isBordered) {
            if (isSelected) {
                if (selectedBorder == null) {
                    selectedBorder = BorderFactory.createMatteBorder(2, 5, 2, 5, table.getSelectionBackground());
                }
                setBorder(selectedBorder);
            } else {
                if (unselectedBorder == null) {
                    unselectedBorder = BorderFactory.createMatteBorder(2, 5, 2, 5, table.getBackground());
                }
                setBorder(unselectedBorder);
            }
        }
        try {
            Boolean v = (Boolean) value;
            JPanel content = new JPanel(new CenterLayout());
            content.setBackground(table.getBackground());
            chk = new JCheckBox("", v.booleanValue());
            chk.setBackground(table.getBackground());
            content.add(chk, BorderLayout.CENTER);
            return content;
        } catch (ClassCastException ccEx) {
            throw new RuntimeException("Trying to use a Boolean cell renderer with a non-Boolean datatype");
        }
    }
}
