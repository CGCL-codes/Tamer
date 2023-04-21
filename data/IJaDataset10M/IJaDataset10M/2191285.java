package cn.ekuma.epos.crm.model;

import cn.ekuma.data.ui.swing.AbstractDTOTableModel;
import com.openbravo.bean.crm.CustomerScoreDiary;
import com.openbravo.format.Formats;
import com.openbravo.pos.base.AppLocal;
import com.openbravo.pos.util.IntegerUtils;

public class CustomerScoreDiaryTableModel extends AbstractDTOTableModel<CustomerScoreDiary> {

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        CustomerScoreDiary obj = getObj(rowIndex);
        switch(columnIndex) {
            case 0:
                return Formats.TIMESTAMP.formatValue(obj.getDate());
            case 1:
                return obj.getReason();
            case 2:
                return obj.getPrice();
            case 3:
                return obj.getUserId();
            case 4:
                return obj.getMemo();
        }
        return null;
    }

    private static int[] doubleInt = { 2 };

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (IntegerUtils.concat(columnIndex, doubleInt)) return Double.class;
        return super.getColumnClass(columnIndex);
    }

    @Override
    public String[] getColumnNames() {
        return new String[] { AppLocal.getIntString("label.createTime"), AppLocal.getIntString("label.CustomerStoreValueCardDiary.reason"), AppLocal.getIntString("label.CustomerStoreValueCardDiary.price"), AppLocal.getIntString("label.referncePerson"), AppLocal.getIntString("label.notes") };
    }
}
