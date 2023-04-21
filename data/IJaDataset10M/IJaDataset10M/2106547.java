package de.jakop.rugby.zeit.gui;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import de.jakop.rugby.util.UhrHelper;
import de.jakop.rugby.zeit.Timer;

/**
 * Ein {@link TableCellRenderer}, der eine Zeit in Sekunden im Format
 * <pre>mm:ss</pre> anzeigt. 
 * @author jakop
 */
public class ZeitTableCellRenderer extends DefaultTableCellRenderer {

    /**
	 * 
	 */
    private static final long serialVersionUID = -7908217778610734323L;

    @Override
    protected void setValue(Object value) {
        setText(UhrHelper.format(((Timer) value).getRestzeit()));
    }
}
