package org.dmonix.timex.list;

import java.awt.*;
import javax.swing.*;

/**
 * Cell renderer for lists containing activity objects.
 * @author Peter Nerg
 * @version 1.0
 */
public class ActivityListCellRenderer extends DefaultListCellRenderer {

    private static final long serialVersionUID = 1L;

    public ActivityListCellRenderer() {
        super();
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        String desc = ((ActivityListObject) value).getDescription();
        if (desc == null || desc.length() == 0) desc = "";
        super.setToolTipText(desc);
        return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    }
}
