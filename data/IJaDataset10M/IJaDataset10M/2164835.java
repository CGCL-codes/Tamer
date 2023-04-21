package net.kano.joustsim.app.forms;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import java.awt.Component;

public class FilterListCellRenderer implements ListCellRenderer {

    private final ListCellRenderer orig;

    public FilterListCellRenderer(ListCellRenderer orig) {
        if (orig == null) {
            this.orig = new DefaultListCellRenderer();
        } else {
            this.orig = orig;
        }
    }

    public final ListCellRenderer getOriginalRenderer() {
        return orig;
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        return orig.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    }
}
