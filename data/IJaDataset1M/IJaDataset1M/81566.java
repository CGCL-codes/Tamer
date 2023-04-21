package net.sourceforge.sdm.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 * Table header for a sortable table.
 * Supports the 3 SortTypes = NONE, ASCENDING, DECENDING.
 */
public class SortHeaderRenderer extends DefaultTableCellRenderer {

    public static Icon ICON_NONSORTED = new SortArrowIcon(SortTypes.NONE);

    public static Icon ICON_ASCENDING = new SortArrowIcon(SortTypes.ASCENDING);

    public static Icon ICON_DECENDING = new SortArrowIcon(SortTypes.DECENDING);

    public SortHeaderRenderer() {
        setHorizontalTextPosition(LEFT);
        setHorizontalAlignment(CENTER);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
        int index = -1;
        int sortType = SortTypes.NONE;
        if (table instanceof SortTable) {
            SortTable sortTable = (SortTable) table;
            index = sortTable.getSortedColumnIndex();
            sortType = sortTable.getSortType();
        }
        if (table != null) {
            JTableHeader header = table.getTableHeader();
            if (header != null) {
                setForeground(header.getForeground());
                setBackground(header.getBackground());
                setFont(header.getFont());
            }
        }
        setIcon((col == index) ? getSortIcon(sortType) : ICON_NONSORTED);
        setText((value == null) ? "" : value.toString());
        setBorder(UIManager.getBorder("TableHeader.cellBorder"));
        return this;
    }

    private Icon getSortIcon(int sortType) {
        switch(sortType) {
            case SortTypes.ASCENDING:
                return ICON_ASCENDING;
            case SortTypes.DECENDING:
                return ICON_DECENDING;
            default:
                return ICON_NONSORTED;
        }
    }
}
