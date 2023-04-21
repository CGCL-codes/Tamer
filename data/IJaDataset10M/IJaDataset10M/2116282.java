package agentgui.envModel.graph.components;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 * Is used in the {@link ComponentTypeDialog}.
 *
 * @author Satyadeep Karnati - CSE - Indian Institute of Technology, Guwahati
 */
public class TableCellEditor4TableButton extends AbstractCellEditor implements TableCellEditor, ActionListener {

    private static final long serialVersionUID = 3607367692654837941L;

    @SuppressWarnings("unused")
    private JTable table = null;

    private JButton button = new JButton();

    int clickCountToStart = 1;

    /**
     * Constructor of this class
     *
     * @param table the table
     */
    public TableCellEditor4TableButton(JTable table) {
        this.table = table;
        this.button.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        button.setText(value.toString());
        return button;
    }

    public Object getCellEditorValue() {
        return button.getText();
    }

    public boolean isCellEditable(EventObject anEvent) {
        if (anEvent instanceof MouseEvent) {
            return ((MouseEvent) anEvent).getClickCount() >= clickCountToStart;
        }
        return true;
    }

    public boolean shouldSelectCell(EventObject anEvent) {
        return true;
    }

    public boolean stopCellEditing() {
        return super.stopCellEditing();
    }

    public void cancelCellEditing() {
        super.cancelCellEditing();
    }
}
