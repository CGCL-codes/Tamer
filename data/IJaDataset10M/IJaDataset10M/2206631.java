package org.vikamine.gui.util;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import org.vikamine.kernel.data.Attribute;
import org.vikamine.kernel.subgroup.selectors.SGNominalSelector;
import org.vikamine.kernel.subgroup.target.SGTarget;

public class DMAttributeTargetAndSGSelectorRenderer extends DefaultListCellRenderer {

    private static final long serialVersionUID = -4793970349246442650L;

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value instanceof SGNominalSelector) {
            SGNominalSelector selector = (SGNominalSelector) value;
            value = selector.getDescription();
        } else if (value instanceof SGTarget) {
            SGTarget target = (SGTarget) value;
            value = target.getDescription();
        } else {
            Attribute attr = (Attribute) value;
            value = attr.getDescription();
        }
        return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    }
}
