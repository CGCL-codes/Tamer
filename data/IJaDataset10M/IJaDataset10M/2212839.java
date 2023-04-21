package com.empower.client.tablemodels;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import com.empower.model.DeliveryChallanDetailsModel;
import com.empower.model.PSCnsgnmntDtlsModel;
import com.empower.utils.ECSUtil;

public class DCViewTableModel extends javax.swing.table.AbstractTableModel {

    private static int COLUMN_COUNT = 8;

    private ArrayList dataList = new ArrayList(0);

    private String[] columnNames = getColumnNames();

    public DeliveryChallanDetailsModel getDeliveryChallanDetailsModelAt(int row) {
        return (DeliveryChallanDetailsModel) dataList.get(row);
    }

    public void removeDeliveryChallanDetailsModelAt(int row) {
        dataList.remove(row);
        fireTableCellUpdated(row, -1);
    }

    public void addDeliveryChallanDetailsModel(DeliveryChallanDetailsModel model) {
        dataList.add(model);
        fireTableDataChanged();
    }

    public void addDeliveryChallanDetailsModelList(List l) {
        dataList.addAll(l);
        fireTableDataChanged();
    }

    public DCViewTableModel(List l) {
        dataList.addAll(l);
    }

    public DCViewTableModel() {
    }

    public String getColumnName(int i) {
        return columnNames[i];
    }

    public List getDataList() {
        return dataList;
    }

    public Class getColumnClass(int col) {
        return getValueAt(0, col).getClass();
    }

    public int getColumnCount() {
        return COLUMN_COUNT;
    }

    public Object getValueAt(int row, int col) {
        Object obj = null;
        DeliveryChallanDetailsModel dlvryChallanDetailsModel = (DeliveryChallanDetailsModel) dataList.get(row);
        switch(col) {
            case -1:
                obj = dlvryChallanDetailsModel;
                break;
            case 0:
                obj = dlvryChallanDetailsModel.getCnsgnmntNbr();
                break;
            case 1:
                obj = dlvryChallanDetailsModel.getConsigneeName();
                break;
            case 2:
                obj = new Double(dlvryChallanDetailsModel.getAmttoPay());
                break;
            case 3:
                obj = new Double(dlvryChallanDetailsModel.getCodDod());
                break;
            case 4:
                obj = new Double(dlvryChallanDetailsModel.getWeightOfPkg());
                break;
            case 5:
                obj = new Double(dlvryChallanDetailsModel.getChargeableWeight());
                break;
            case 6:
                obj = new Long(dlvryChallanDetailsModel.getPkgsCnt());
                break;
            case 7:
                obj = ECSUtil.formatDateToGUIFormat(dlvryChallanDetailsModel.getDlvryDateTime());
                break;
            default:
                System.out.println("Invalid Entry");
        }
        return obj;
    }

    public boolean isCellEditable(int row, int col) {
        switch(col) {
            case 0:
                return false;
            case 4:
                return false;
            case 6:
                return false;
            default:
                return true;
        }
    }

    public String[] getColumnNames() {
        String[] columnNames = new String[COLUMN_COUNT];
        columnNames[0] = "CONSIGNMENT NO.";
        columnNames[1] = "CONSIGNEE NAME";
        columnNames[2] = "AMOUNT TO PAY";
        columnNames[3] = "COD/DOD";
        columnNames[4] = "WEIGHT";
        columnNames[5] = "CHARGEABLE WEIGHT";
        columnNames[6] = "NO.OF PACKAGES";
        columnNames[7] = "DELIVERED DATE & TIME";
        return columnNames;
    }

    public void setValueAt(Object aValue, int row, int col) {
        if (aValue == null) {
            return;
        }
        DeliveryChallanDetailsModel dlvryChallanDetailsModel = (DeliveryChallanDetailsModel) dataList.get(row);
        switch(col) {
            case 0:
                dlvryChallanDetailsModel.setCnsgnmntNbr(aValue.toString());
                break;
            case 1:
                dlvryChallanDetailsModel.setConsigneeName(aValue.toString());
                break;
            case 2:
                dlvryChallanDetailsModel.setAmttoPay(((Double) aValue).doubleValue());
                break;
            case 3:
                dlvryChallanDetailsModel.setCodDod(((Double) aValue).doubleValue());
                break;
            case 4:
                dlvryChallanDetailsModel.setWeightOfPkg(((Double) aValue).doubleValue());
                break;
            case 5:
                dlvryChallanDetailsModel.setChargeableWeight(((Double) aValue).doubleValue());
                break;
            case 6:
                dlvryChallanDetailsModel.setPkgsCnt(((Long) aValue).longValue());
                break;
            case 7:
                dlvryChallanDetailsModel.setDlvryDateTime(ECSUtil.formatGUIDateToYYYYMMDD(aValue.toString()));
                break;
        }
        fireTableCellUpdated(row, col);
    }

    public int getRowCount() {
        return dataList.size();
    }
}
