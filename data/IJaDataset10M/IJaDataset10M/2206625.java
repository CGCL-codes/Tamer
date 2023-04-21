package sky4s.test.ui.table.editable;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 * @version 1.0 08/22/99
 */
public class EditableHeaderTableExample2 extends JFrame {

    public EditableHeaderTableExample2() {
        super("EditableHeader Example");
        JTable table = new JTable(7, 5);
        TableColumnModel columnModel = table.getColumnModel();
        table.setTableHeader(new EditableHeader(columnModel));
        String[] items = { "Dog", "Cat" };
        JComboBox combo = new JComboBox();
        for (int i = 0; i < items.length; i++) {
            combo.addItem(items[i]);
        }
        ComboRenderer renderer = new ComboRenderer(items);
        EditableHeaderTableColumn col;
        col = (EditableHeaderTableColumn) table.getColumnModel().getColumn(1);
        col.setHeaderValue(combo.getItemAt(0));
        col.setHeaderRenderer(renderer);
        col.setHeaderEditor(new DefaultCellEditor(combo));
        col = (EditableHeaderTableColumn) table.getColumnModel().getColumn(3);
        col.setHeaderValue(combo.getItemAt(0));
        col.setHeaderEditor(new DefaultCellEditor(combo));
        col.setHeaderEditable(true);
        JScrollPane pane = new JScrollPane(table);
        getContentPane().add(pane);
    }

    class ComboRenderer extends JComboBox implements TableCellRenderer {

        ComboRenderer(String[] items) {
            for (int i = 0; i < items.length; i++) {
                addItem(items[i]);
            }
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setSelectedItem(value);
            return this;
        }
    }

    public static void main(String[] args) {
        EditableHeaderTableExample2 frame = new EditableHeaderTableExample2();
        frame.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        frame.setSize(300, 100);
        frame.setVisible(true);
    }
}
