package jgnash.ui.components;

import java.text.DateFormat;
import jgnash.util.DateUtils;

/**
 * @author Craig Cavanaugh
 *
 * $Id: DateTableCellRenderer.java 675 2008-06-17 01:36:01Z ccavanaugh $
 */
public class DateTableCellRenderer extends ColoredTableCellRenderer {

    private static DateFormat formatter = DateUtils.getShortDateFormat();

    public void setValue(Object value) {
        setText((value == null) ? "" : formatter.format(value));
    }
}
