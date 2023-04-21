package Model.Util;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import datechooser.beans.*;

public class DateChooserColumn extends AbstractCellEditor implements TableCellRenderer, TableCellEditor {

    private JTable table;

    private JButton renderButton;

    private JButton editButton;

    private String text;

    private DateChooserCombo[] dates1;

    public DateChooserCombo[] dates2;

    public DateChooserColumn(JTable table, int column) {
        super();
        this.table = table;
        renderButton = new JButton();
        editButton = new JButton();
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(column).setCellRenderer(this);
        columnModel.getColumn(column).setCellEditor(this);
        dates1 = new DateChooserCombo[table.getRowCount()];
        dates2 = new DateChooserCombo[table.getRowCount()];
        System.out.println("rows" + table.getRowCount());
        for (int i = 0; i < table.getRowCount(); i++) {
            dates1[i] = new DateChooserCombo();
            dates2[i] = new DateChooserCombo();
        }
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        dates1[row].setSelectedDate(dates2[row].getCurrent());
        return dates1[row];
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        dates2[row].setSelectedDate(dates2[row].getCurrent());
        return dates2[row];
    }

    public Object getCellEditorValue() {
        return "blah blah blah";
    }
}
