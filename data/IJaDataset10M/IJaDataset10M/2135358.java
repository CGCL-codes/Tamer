package KFramework30.Widgets.DataBrowser.TableCellRenderers;

import KFramework30.Widgets.DataBrowser.KTableCellRendererBaseClass;
import javax.swing.*;
import java.awt.*;
import KFramework30.Base.*;
import KFramework30.Widgets.KDataBrowserBaseClass;
import KFramework30.Widgets.DataBrowser.tableModelClass;
import com.toedter.calendar.JDateChooser;

public class CalendarCellRendererClass extends KTableCellRendererBaseClass {

    int columnType;

    Font columnFont;

    int columnAligment;

    public CalendarCellRendererClass(tableModelClass tableModelParam, KLogClass logParam) throws KExceptionClass {
        super(tableModelParam, logParam);
        columnType = KDataBrowserBaseClass.BROWSER_COLUMN_TYPE_DATE;
    }

    @Override
    public int getColumnType() {
        return (columnType);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        try {
            renderer = new JDateChooser(KMetaUtilsClass.stringToDate(KMetaUtilsClass.KDEFAULT_LONG_DATE_TIME_FORMAT, (String) value));
        } catch (KExceptionClass ex) {
            renderer = new JDateChooser();
        }
        ((JDateChooser) renderer).setFont(getColumnFont());
        return (super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column));
    }

    public void setDataType(int columnTypeParam) throws KExceptionClass {
        switch(columnTypeParam) {
            case KDataBrowserBaseClass.BROWSER_COLUMN_TYPE_DATE:
                break;
            default:
                throw new KExceptionClass("Could not set renderer data type. Type specified is invalid.", null);
        }
        columnType = columnTypeParam;
    }

    public int getColumnAligment() {
        return columnAligment;
    }

    public void setColumnAligment(int columnAligment) {
        this.columnAligment = columnAligment;
    }

    public Font getColumnFont() {
        return columnFont;
    }

    public void setColumnFont(Font columnFont) {
        this.columnFont = columnFont;
    }
}
