package com.openbravo.pos.inventory.model;

import cn.ekuma.data.ui.swing.AbstractDTOTableModel;
import com.openbravo.bean.Product;
import com.openbravo.bean.ProductAuxiliar;
import com.openbravo.format.Formats;
import com.openbravo.pos.base.AppLocal;

public class ProductAuxiliarTableModel extends AbstractDTOTableModel<ProductAuxiliar> {

    @Override
    public String[] getColumnNames() {
        return new String[] { AppLocal.getIntString("label.prodbarcode"), AppLocal.getIntString("label.prodname"), AppLocal.getIntString("label.prodpricebuy"), AppLocal.getIntString("label.prodpricesell"), AppLocal.getIntString("label.baseCustomerPrice") };
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        ProductAuxiliar productAuxiliar = objs.get(rowIndex);
        Product inculdProduct = productAuxiliar.getProduct();
        switch(columnIndex) {
            case 0:
                return inculdProduct.getCode();
            case 1:
                return inculdProduct.getName();
            case 2:
                return Formats.CURRENCY.formatValue(inculdProduct.getPriceBuy());
            case 3:
                return Formats.CURRENCY.formatValue(inculdProduct.getPriceSell());
            case 4:
                return Formats.CURRENCY.formatValue(inculdProduct.getM_dCustomerPrice());
        }
        return null;
    }
}
