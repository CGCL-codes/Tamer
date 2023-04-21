package pl.otros.logview.gui.actions.search;

import javax.swing.*;

public class NextRowProviderFactory {

    public static NextRowProvider getNextFilteredTableRow(JTable table) {
        return new FilteredTableNextRowProvider(table, SearchDirection.FORWARD);
    }

    public static NextRowProvider getPreviousFilteredTableRow(JTable table) {
        return new FilteredTableNextRowProvider(table, SearchDirection.REVERSE);
    }

    public static NextRowProvider getFilteredTableRow(JTable table, SearchDirection direction) {
        return new FilteredTableNextRowProvider(table, direction);
    }
}
