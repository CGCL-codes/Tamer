package cn.ekuma.epos.crm.model;

import cn.ekuma.data.ui.swing.AbstractDTOTableModel;
import cn.ekuma.epos.db.table.crm.I_CustomerStoreValueCard;
import com.openbravo.bean.crm.CustomerStoreValueCard;
import com.openbravo.bean.crm.StoreValueCardOPEnum;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.model.Field;
import com.openbravo.format.Formats;
import com.openbravo.pos.base.AppLocal;

public class CustomerStoreValueCardTableModel extends AbstractDTOTableModel<CustomerStoreValueCard> {

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        CustomerStoreValueCard obj = getObj(rowIndex);
        switch(columnIndex) {
            case 0:
                return obj.getCardNum();
            case 1:
                return Formats.TIMESTAMP.formatValue(obj.getCreateDate());
            case 2:
                return getState(obj.getState());
            case 3:
                return Formats.TIMESTAMP.formatValue(obj.getValidityDate());
            case 4:
                return Formats.CURRENCY.formatValue(obj.getCurLimit());
            case 5:
                return Formats.CURRENCY.formatValue(obj.getBackLimit());
            case 6:
                return Formats.CURRENCY.formatValue(obj.getTotalRecharge());
            case 7:
                return Formats.CURRENCY.formatValue(obj.getTotalConsumer());
            case 8:
                return obj.getCustomerId();
            case 9:
                return obj.getMemo();
        }
        return null;
    }

    private static final String[] states = new String[] { AppLocal.getIntString("label.CustomerStoreValueCard.state.Unknown"), AppLocal.getIntString("label.CustomerStoreValueCard.state.UseAble"), AppLocal.getIntString("label.CustomerStoreValueCard.state.Back"), AppLocal.getIntString("label.CustomerStoreValueCard.state.Invalid"), AppLocal.getIntString("label.CustomerStoreValueCard.state.Loss") };

    public static String getState(int state) {
        if (state < states.length) return states[state];
        return String.valueOf(state);
    }

    @Override
    public String[] getColumnNames() {
        return new String[] { AppLocal.getIntString("label.CustomerStoreValueCard.cardNum"), AppLocal.getIntString("label.createTime"), AppLocal.getIntString("label.CustomerStoreValueCard.state"), AppLocal.getIntString("label.CustomerStoreValueCard.validityDate"), AppLocal.getIntString("label.CustomerStoreValueCard.curLimit"), AppLocal.getIntString("label.CustomerStoreValueCard.backLimit"), AppLocal.getIntString("label.CustomerStoreValueCard.totalRecharge"), AppLocal.getIntString("label.CustomerStoreValueCard.totalConsumer"), AppLocal.getIntString("label.customer"), AppLocal.getIntString("label.notes") };
    }

    public static Field[] getQBFFields() {
        return new Field[] { new Field(I_CustomerStoreValueCard.CARDNUM, Datas.STRING, Formats.STRING, AppLocal.getIntString("label.CustomerStoreValueCard.cardNum")), new Field(I_CustomerStoreValueCard.CURLIMIT, Datas.DOUBLE, Formats.DOUBLE, AppLocal.getIntString("label.CustomerStoreValueCard.curLimit")), new Field(I_CustomerStoreValueCard.BACKLIMIT, Datas.DOUBLE, Formats.DOUBLE, AppLocal.getIntString("label.CustomerStoreValueCard.backLimit")), new Field(I_CustomerStoreValueCard.STATE, Datas.INT, Formats.INT, AppLocal.getIntString("label.CustomerStoreValueCard.state")) };
    }

    public static String getStoreCardOp(StoreValueCardOPEnum opEnum) {
        switch(opEnum) {
            case lost:
                return AppLocal.getIntString("label.StoreValueCardOPEnum.lost");
            case recovery:
                return AppLocal.getIntString("label.StoreValueCardOPEnum.recovery");
            case able:
                return AppLocal.getIntString("label.StoreValueCardOPEnum.able");
            case back:
                return AppLocal.getIntString("label.StoreValueCardOPEnum.back");
            case invalid:
                return AppLocal.getIntString("label.StoreValueCardOPEnum.invalid");
            case transferIn:
                return AppLocal.getIntString("label.StoreValueCardOPEnum.transferIn");
            case transferOut:
                return AppLocal.getIntString("label.StoreValueCardOPEnum.transferOut");
            case consumer:
                return AppLocal.getIntString("label.StoreValueCardOPEnum.consumer");
            case recharge:
                return AppLocal.getIntString("label.StoreValueCardOPEnum.recharge");
        }
        return null;
    }
}
