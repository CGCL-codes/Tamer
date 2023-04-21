package kellinwood.meshi.form;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import java.text.*;
import java.util.*;

/**
 *
 * @author ken
 */
public class TimeFieldTableCellRenderer extends DefaultTableCellRenderer {

    DateFormat format;

    /** Creates a new instance of TimeFieldTableCellRenderer */
    public TimeFieldTableCellRenderer(TimeField timeField) {
        format = timeField.getFormat();
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value != null) value = format.format((Date) value);
        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }
}
