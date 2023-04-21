package net.myphpshop.admin.gui.vat;

import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import net.myphpshop.www.myPHPShopAdmin.VAT;

public class VATTableModel extends AbstractTableModel {

    VAT _vats[] = new VAT[20];

    public int getRowCount() {
        return _vats.length;
    }

    public VAT getVAT(int row) {
        return _vats[row];
    }

    public void removeAll() {
        for (int i = 0; i < _vats.length; i++) {
            _vats[i] = null;
        }
        this.fireTableChanged(new TableModelEvent(this));
    }

    public Object getValueAt(int row, int col) {
        if (_vats[row] == null) return null;
        if (col == 0) {
            return _vats[row].getVATId();
        } else if (col == 1) {
            return _vats[row].getTaxLevel();
        }
        return null;
    }

    public int getColumnCount() {
        return 5;
    }

    public void setValueAt(VAT val, int row) {
        _vats[row] = val;
        this.fireTableChanged(new TableModelEvent(this, row, row));
    }
}
