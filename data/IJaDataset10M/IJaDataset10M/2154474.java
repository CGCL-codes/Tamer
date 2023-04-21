package jmash;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import javax.swing.AbstractCellEditor;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author Alessandro
 */
public class SpinnerEditor extends AbstractCellEditor implements TableCellEditor {

    /**
	 * 
	 */
    private static final long serialVersionUID = -5561494234490223938L;

    final JSpinner spinner = new JSpinner();

    public SpinnerEditor() {
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.spinner.setValue(value);
        return this.spinner;
    }

    @Override
    public boolean isCellEditable(EventObject evt) {
        if (evt instanceof MouseEvent) {
            return ((MouseEvent) evt).getClickCount() >= 1;
        }
        return true;
    }

    public Object getCellEditorValue() {
        return this.spinner.getValue();
    }
}
