package jaxilstudio.business.collector;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

public class ListCollectorRender extends DefaultListCellRenderer {

    private static final long serialVersionUID = 7212025741558623208L;

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        ListCollectorItem item = (ListCollectorItem) value;
        return super.getListCellRendererComponent(list, item.getLabel(), index, isSelected, cellHasFocus);
    }
}
