package edu.udo.scaffoldhunter.gui.dataimport;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import edu.udo.scaffoldhunter.gui.util.CustomComboBoxRenderer;

/**
 * A list cell renderer which renders <code>null</code> as a predefined string.
 * <p>
 * Additionally this renderer sets its tooltiptext to the value text, which is
 * useful when dealing with longer value strings.
 * 
 * @author Henning Garus
 * 
 */
public class CellRendererWithNullValue extends CustomComboBoxRenderer {

    private final String nullString;

    /**
     * Create a new Cell Renderer which will show a specified String for null
     * values.
     * 
     * @param nullString
     *            the string which will be shown when a rendered value is null
     */
    public CellRendererWithNullValue(String nullString) {
        super();
        this.nullString = nullString;
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value == null) {
            value = nullString;
        }
        JLabel c = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        c.setToolTipText(value.toString());
        return c;
    }
}
