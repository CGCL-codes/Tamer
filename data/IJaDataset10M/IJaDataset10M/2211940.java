package secd.simulacion;

import java.awt.Component;
import java.util.EventObject;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import javax.swing.text.JTextComponent;

public class TablaConSobreescritura extends JTable {

    private static final long serialVersionUID = 1L;

    TablaConSobreescritura(AbstractTableModel modelo) {
        super(modelo);
    }

    public boolean editCellAt(int row, int column, EventObject e) {
        boolean result = super.editCellAt(row, column, e);
        final Component editor = getEditorComponent();
        if (editor != null && editor instanceof JTextComponent) {
            if (e == null) {
                ((JTextComponent) editor).selectAll();
            } else {
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        ((JTextComponent) editor).selectAll();
                    }
                });
            }
        }
        return result;
    }
}
