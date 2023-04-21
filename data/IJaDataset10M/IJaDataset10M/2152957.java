package cw.boardingschoolmanagement.gui.renderer;

import cw.boardingschoolmanagement.app.CalendarUtil;
import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author ManuelG
 */
public class DateTimeTableCellRenderer extends DefaultTableCellRenderer {

    private JLabel cell;

    private boolean dateOnly;

    private String dateFormat;

    private boolean useDateFormat = false;

    public DateTimeTableCellRenderer() {
        this(false);
    }

    public DateTimeTableCellRenderer(boolean dateOnly) {
        this.dateOnly = dateOnly;
    }

    public DateTimeTableCellRenderer(String dateFormat) {
        this.useDateFormat = true;
        this.dateFormat = dateFormat;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        cell = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (value instanceof Date) {
            if (useDateFormat) {
                SimpleDateFormat format = new SimpleDateFormat(dateFormat);
                cell.setText(format.format(value));
            } else {
                GregorianCalendar gc = new GregorianCalendar();
                gc.setTime((Date) value);
                StringBuilder builder = new StringBuilder();
                builder.append(gc.get(Calendar.DAY_OF_MONTH));
                builder.append(".");
                builder.append(CalendarUtil.getMonth(gc.get(Calendar.MONTH) + 1));
                builder.append(".");
                builder.append(gc.get(Calendar.YEAR));
                if (!dateOnly) {
                    builder.append(" ");
                    builder.append(CalendarUtil.getHour(gc.get(Calendar.HOUR_OF_DAY)));
                    builder.append(":");
                    builder.append(CalendarUtil.getMinute(gc.get(Calendar.MINUTE)));
                    builder.append(":");
                    builder.append(CalendarUtil.getSecond(gc.get(Calendar.SECOND)));
                }
                cell.setText(builder.toString());
            }
        } else {
            cell.setText("");
        }
        return cell;
    }
}
