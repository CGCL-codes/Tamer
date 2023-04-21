package eu.keep.uphec.mainwindow.language;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

@SuppressWarnings("serial")
class CustomListCellRenderer extends JLabel implements ListCellRenderer {

    private static final Dimension preferredSize = new Dimension(100, 20);

    protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        renderer.setPreferredSize(preferredSize);
        list.setSelectedIndex(0);
        if (index != -1) {
            renderer.setFocusable(false);
            renderer.setEnabled(false);
        }
        return renderer;
    }
}
