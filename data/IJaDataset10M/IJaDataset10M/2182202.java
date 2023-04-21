package com.elibera.ccs.panel.question.tbl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;

public class JTableComponentMouseListener implements MouseListener {

    private JTable __table;

    public JTableComponentMouseListener(JTable table) {
        __table = table;
    }

    private void __forwardEventToButton(MouseEvent e) {
        TableColumnModel columnModel = __table.getColumnModel();
        int column = columnModel.getColumnIndexAtX(e.getX());
        int row = e.getY() / __table.getRowHeight();
        Object value;
        if (row >= __table.getRowCount() || row < 0 || column >= __table.getColumnCount() || column < 0) return;
        value = __table.getValueAt(row, column);
        if (value instanceof JButton) {
            JButton button = (JButton) value;
            ActionEvent event = new ActionEvent(button, 1, "cmd");
            ActionListener[] al = button.getActionListeners();
            for (int i = 0; i < al.length; i++) {
                al[i].actionPerformed(event);
            }
            __table.repaint();
        } else if (value instanceof JCheckBox) {
            JCheckBox comp = (JCheckBox) value;
            ActionEvent event = new ActionEvent(comp, 1, "cmd");
            ActionListener[] al = comp.getActionListeners();
            for (int i = 0; i < al.length; i++) {
                al[i].actionPerformed(event);
            }
            __table.repaint();
        } else if (value instanceof JComboBox) {
            JComboBox comp = (JComboBox) value;
            ActionEvent event = new ActionEvent(comp, 1, "cmd");
            ActionListener[] al = comp.getActionListeners();
            for (int i = 0; i < al.length; i++) {
                al[i].actionPerformed(event);
            }
            __table.repaint();
        }
        return;
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
        __forwardEventToButton(e);
    }
}
