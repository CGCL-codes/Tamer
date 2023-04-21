package net.myphpshop.admin.gui.deliverycost;

import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import net.myphpshop.admin.configuration.ClientConfiguration;
import net.myphpshop.www.myPHPShopAdmin.DeliveryCost;
import net.myphpshop.www.myPHPShopAdmin.Language;

public class DeliveryCostTableModel extends AbstractTableModel {

    DeliveryCost _costs[] = new DeliveryCost[20];

    Language _language = null;

    public int getRowCount() {
        return _costs.length;
    }

    public DeliveryCost getDeliveryCost(int row) {
        return _costs[row];
    }

    public void removeAll() {
        for (int i = 0; i < _costs.length; i++) {
            _costs[i] = null;
        }
        this.fireTableChanged(new TableModelEvent(this));
    }

    public Object getValueAt(int row, int col) {
        if (_costs[row] == null) return null;
        if (col == 0) {
            return _costs[row].getDeliveryCostId();
        } else if (col == 1) {
            return _costs[row].getCountryCode();
        } else if (col == 2) {
            return _costs[row].getPrice().get_value() + " " + _costs[row].getPrice().getCurrency();
        } else if (col == 3) {
            return _costs[row].getWeight();
        } else if (col == 4) {
            for (int j = 0; j < _costs[row].getDispatchType().getDetail().length; j++) {
                if (_costs[row].getDispatchType().getDetail()[j].getLanguage().getValue().equals(ClientConfiguration.getInstance().getLanguage())) {
                    return _costs[row].getDispatchType().getDetail()[j].getName();
                }
            }
            return _costs[row].getDispatchType().getDetail()[0].getName();
        }
        return null;
    }

    public int getColumnCount() {
        return 5;
    }

    public void setValueAt(DeliveryCost val, int row) {
        _costs[row] = val;
        this.fireTableChanged(new TableModelEvent(this, row, row));
    }
}
