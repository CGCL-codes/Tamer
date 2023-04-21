package com.openbravo.pos.inventory.model;

import cn.ekuma.data.ui.swing.AbstractDTOTableModel;
import com.openbravo.bean.ProductScore;
import com.openbravo.pos.base.AppLocal;

public class ProductScoreTableModel extends AbstractDTOTableModel<ProductScore> {

    public String[] getColumnNames() {
        return new String[] { AppLocal.getIntString("label.ProductScore.type"), AppLocal.getIntString("label.score") };
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        ProductScore p = getObj(rowIndex);
        switch(columnIndex) {
            case 0:
                if (p.getType() == ProductScore.TYPE_Online) return AppLocal.getIntString("label.ProductScore.type.online");
                return AppLocal.getIntString("label.ProductScore.type.store");
            case 1:
                return p.getScore();
        }
        return null;
    }
}
