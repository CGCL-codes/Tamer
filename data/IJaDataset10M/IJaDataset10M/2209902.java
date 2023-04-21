package com.charwot.buddi.plugins.gui;

import java.util.Date;
import javax.swing.table.DefaultTableCellRenderer;
import org.homeunix.thecave.buddi.plugin.api.util.TextFormatter;

public class DateTableCellRenderer extends DefaultTableCellRenderer {

    private static final long serialVersionUID = 1L;

    public DateTableCellRenderer() {
    }

    @Override
    protected void setValue(Object value) {
        if (value instanceof Date) {
            value = TextFormatter.getFormattedDate((Date) value);
        }
        super.setValue(value);
    }
}
