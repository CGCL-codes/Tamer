package net.sourceforge.picdev.ui;

import net.sourceforge.picdev.annotations.EComponent;
import net.sourceforge.picdev.core.Component;
import javax.swing.*;

/**
 * @author Klaus Friedel
 *         Date: 11.12.2007
 *         Time: 22:13:16
 */
public class ComponentListRenderer extends DefaultListCellRenderer {

    public java.awt.Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        java.awt.Component guiComp = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof Class) {
            String description = ((Class<? extends Component>) value).getAnnotation(EComponent.class).description();
            setText(description);
        } else if (value instanceof Component) {
            Component comp = (Component) value;
            setText(comp.getName());
        }
        return guiComp;
    }
}
