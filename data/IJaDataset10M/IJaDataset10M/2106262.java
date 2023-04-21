package cn.ekuma.epos.bean.util;

import com.openbravo.format.Formats;
import com.openbravo.bean.Product;
import com.openbravo.bean.Tax;

/**
 *
 * @author Administrator
 */
public class ProductInfoHelper {

    public static String printPriceSell(Product p) {
        return Formats.CURRENCY.formatValue(new Double(p.getPriceSell()));
    }

    public static String printPriceSellTax(Product p, Tax tax) {
        return Formats.CURRENCY.formatValue(new Double(p.getPriceSellTax(tax)));
    }

    public static String printCustomerPriceSell(Product p) {
        double customPrice = p.getM_dCustomerPrice();
        customPrice = customPrice == 0 ? p.getPriceSell() : customPrice;
        return Formats.CURRENCY.formatValue(new Double(customPrice));
    }
}
