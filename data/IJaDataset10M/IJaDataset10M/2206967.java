package edu.psu.its.lionshare.gui.chat.renderer;

import edu.psu.its.lionshare.database.ChatHost;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.border.LineBorder;

public class ConnectionComboBoxRenderer extends DefaultListCellRenderer {

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel c = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof ChatHost) {
            c.setText(((ChatHost) value).getName());
        }
        c.setForeground(Color.black);
        c.setBackground(Color.white);
        return c;
    }
}
