package org.xito.controlpanel;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 *
 * @author Deane Richan
 */
public class ControlPanel extends JPanel {

    private JTable table;

    /** Creates a new instance of ControlPanel */
    public ControlPanel() {
        setLayout(new BorderLayout());
        table = new JTable(ControlPanelService.getModel(), ControlPanelService.getModel().getColModel());
        table.setShowGrid(false);
        table.getTableHeader().setPreferredSize(new JLabel("dummy").getPreferredSize());
        TableColumn col = table.getColumnModel().getColumn(0);
        col.setWidth(20);
        col.setCellRenderer(new DefaultTableCellRenderer() {

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel comp = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                Action action = (Action) value;
                comp.setIcon((Icon) action.getValue(Action.SMALL_ICON));
                comp.setText((String) action.getValue(Action.NAME));
                return comp;
            }
        });
        table.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    Action a = (Action) table.getValueAt(table.getSelectedRow(), 0);
                    a.actionPerformed(new ActionEvent(table, 0, null));
                }
            }
        });
        JScrollPane sp = new JScrollPane(table);
        sp.getViewport().setBackground(new JTextField().getBackground());
        add(sp);
    }
}
