package de.internnetz.eaf.calendar.gui;

import java.awt.Component;
import java.util.Date;
import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import com.toedter.calendar.JSpinnerDateEditor;
import de.internnetz.eaf.calendar.CalendarTool;
import de.internnetz.eaf.core.gui.coloredrenderer.ColoredString;

public class DateTableCellEditor extends AbstractCellEditor implements TableCellEditor {

    private static final long serialVersionUID = 145345346L;

    private JSpinnerDateEditor editor;

    public DateTableCellEditor(String formatString) {
        editor = new JSpinnerDateEditor();
        editor.setDateFormatString(formatString);
    }

    public Object getCellEditorValue() {
        return editor.getDate();
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        try {
            ColoredString s = (ColoredString) value;
            Date d = CalendarTool.DATE_FORMAT.parse(s.toString());
            editor.setDate(d);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return editor;
    }
}
