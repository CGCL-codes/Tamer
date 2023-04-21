package ch.simas.nbtostring.util;

import ch.simas.nbtostring.builder.ToStringBuilderType;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JList;

/**
 * Renderer for comboboxes presenting a ToStringBuilderType.
 * @author cperv
 */
public class BuilderTypeComboBoxRenderer extends DefaultListCellRenderer {

    private static final long serialVersionUID = 2128421996753797686L;

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JComponent ret = (JComponent) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof ToStringBuilderType) {
            setText(((ToStringBuilderType) value).getClassAsString());
        } else {
            setText("Unknown");
        }
        return ret;
    }
}
