package pl.wat.wcy.sna.bundle;

import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import pl.wat.wcy.sna.core.SNADetailUpdate;

public class SNADetailCellEditorListener implements CellEditorListener {

    public void editingCanceled(ChangeEvent e) {
    }

    public void editingStopped(ChangeEvent e) {
        SNATableCellEditor k = (SNATableCellEditor) e.getSource();
        if (k.getCellEditorValue().equals("") || k.getRowIndex() == 0) k.SetOldValue(); else {
            SNADetailUpdate up = new SNADetailUpdate(k.getFA(), (String) k.getTable().getValueAt(0, 1), (String) k.getTable().getValueAt(k.getRowIndex(), 0), (String) k.getTable().getValueAt(k.getRowIndex(), 1));
            up.start();
        }
    }
}
