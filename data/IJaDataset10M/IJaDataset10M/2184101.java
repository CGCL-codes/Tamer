package net.pzc.filechooser;

import java.awt.Color;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JList;

public class MyListCellRender extends DefaultListCellRenderer {

    private static final long serialVersionUID = 1L;

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value != null) {
            if (isSelected) {
                setForeground(Color.white);
                setBackground(new Color(10, 36, 106));
            } else {
                setForeground(Color.black);
                setBackground(null);
            }
            FileItem fi = (FileItem) value;
            setText(fi.getName());
            if (fi.getIcon() != null) {
                setIcon(new ImageIcon(fi.getIcon()));
            }
        }
        return this;
    }
}
