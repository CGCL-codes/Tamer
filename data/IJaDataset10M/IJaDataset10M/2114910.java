package gnu.rhuelga.cirl.cirllib;

import java.text.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

class ResourceListNumberCellRenderer extends DefaultTableCellRenderer {

    JTextField text_field;

    NumberFormat number_format;

    static final String TXT_UNKNOWN = "Unknown";

    ResourceListNumberCellRenderer() {
        super();
        number_format = NumberFormat.getInstance();
        text_field = new JTextField();
        text_field.setHorizontalAlignment(JTextField.RIGHT);
        text_field.setEditable(false);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Integer i;
        i = (Integer) value;
        if (i.intValue() != -1) {
            text_field.setText(number_format.format((Integer) value));
        } else {
            text_field.setText(TXT_UNKNOWN);
        }
        return (Component) text_field;
    }
}
