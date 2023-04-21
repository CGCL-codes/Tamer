package com.mat.table;

import java.util.*;
import com.mat.vo.ItemDetailVO;
import com.util.comparator.*;
import com.util.table.*;

public class ItemDetailModel implements ITableModel {

    protected ItemDetailVO fTotal;

    protected ItemDetailVO[] fTableData;

    protected String fSortedColumn;

    protected int[] fSortOrder;

    protected Comparator fComparator;

    public ItemDetailModel() {
        super();
    }

    public void setData(Object[] data) {
        fTableData = (ItemDetailVO[]) data;
        fSortOrder = new int[(fTableData != null) ? fTableData.length : 0];
        for (int i = 0; i < fSortOrder.length; i++) {
            fSortOrder[i] = i;
        }
    }

    public Object getValueAt(int pRow, String pColumn) {
        ItemDetailVO vo = fTableData[fSortOrder[pRow]];
        if ("id".equals(pColumn)) return new Long(vo.getId());
        if ("codeAn".equals(pColumn)) return vo.getCodeAn();
        if ("itemID".equals(pColumn)) return new Long(vo.getItemID());
        if ("locationID".equals(pColumn)) return new Long(vo.getLocationID());
        if ("count".equals(pColumn)) return new Long(vo.getCount());
        if ("guarantee".equals(pColumn)) return new Long(vo.getGuarantee());
        if ("availability".equals(pColumn)) return vo.getAvailability();
        if ("price".equals(pColumn)) return vo.getPrice();
        if ("discount".equals(pColumn)) return vo.getDiscount();
        if ("discountPercent".equals(pColumn)) return new Double(vo.getDiscountPercent());
        if ("vatID".equals(pColumn)) return new Long(vo.getVatID());
        if ("barCode".equals(pColumn)) return vo.getBarCode();
        if ("serialNr".equals(pColumn)) return vo.getSerialNr();
        if ("deliveryDate".equals(pColumn)) return vo.getDeliveryDate();
        if ("expiryDate".equals(pColumn)) return vo.getExpiryDate();
        if ("pinEncrypted".equals(pColumn)) return vo.getPinEncrypted();
        if ("pinCode".equals(pColumn)) return vo.getPinCode();
        if ("modified".equals(pColumn)) return vo.getModified();
        if ("validFrom".equals(pColumn)) return vo.getValidFrom();
        if ("validTo".equals(pColumn)) return vo.getValidTo();
        return null;
    }

    public Object getTotalValueAt(String pColumn) {
        if (fTotal != null) {
            if ("id".equals(pColumn)) return new Long(fTotal.getId());
            if ("codeAn".equals(pColumn)) return fTotal.getCodeAn();
            if ("itemID".equals(pColumn)) return new Long(fTotal.getItemID());
            if ("locationID".equals(pColumn)) return new Long(fTotal.getLocationID());
            if ("count".equals(pColumn)) return new Long(fTotal.getCount());
            if ("guarantee".equals(pColumn)) return new Long(fTotal.getGuarantee());
            if ("availability".equals(pColumn)) return fTotal.getAvailability();
            if ("price".equals(pColumn)) return fTotal.getPrice();
            if ("discount".equals(pColumn)) return fTotal.getDiscount();
            if ("discountPercent".equals(pColumn)) return new Double(fTotal.getDiscountPercent());
            if ("vatID".equals(pColumn)) return new Long(fTotal.getVatID());
            if ("barCode".equals(pColumn)) return fTotal.getBarCode();
            if ("serialNr".equals(pColumn)) return fTotal.getSerialNr();
            if ("deliveryDate".equals(pColumn)) return fTotal.getDeliveryDate();
            if ("expiryDate".equals(pColumn)) return fTotal.getExpiryDate();
            if ("pinEncrypted".equals(pColumn)) return fTotal.getPinEncrypted();
            if ("pinCode".equals(pColumn)) return fTotal.getPinCode();
            if ("modified".equals(pColumn)) return fTotal.getModified();
            if ("validFrom".equals(pColumn)) return fTotal.getValidFrom();
            if ("validTo".equals(pColumn)) return fTotal.getValidTo();
        }
        return null;
    }

    public void sort(String pColumn, String pSortDirection) {
        boolean up = true;
        if (!"u".equals(pSortDirection)) {
            up = false;
        }
        if ("id".equals(pColumn)) {
            Sorter sorter = new Sorter();
            long[] temp = new long[fTableData.length];
            for (int i = 0; i < temp.length; i++) {
                temp[i] = fTableData[i].getId();
            }
            fSortOrder = sorter.sortLong(temp, fSortOrder, up);
        }
        if ("codeAn".equals(pColumn)) {
            fComparator = new StringComparator();
            String[] temp = new String[fTableData.length];
            for (int i = 0; i < temp.length; i++) {
                temp[i] = fTableData[i].getCodeAn();
            }
            sort(temp, 0, temp.length - 1, up);
        }
        if ("itemID".equals(pColumn)) {
            Sorter sorter = new Sorter();
            long[] temp = new long[fTableData.length];
            for (int i = 0; i < temp.length; i++) {
                temp[i] = fTableData[i].getItemID();
            }
            fSortOrder = sorter.sortLong(temp, fSortOrder, up);
        }
        if ("locationID".equals(pColumn)) {
            Sorter sorter = new Sorter();
            long[] temp = new long[fTableData.length];
            for (int i = 0; i < temp.length; i++) {
                temp[i] = fTableData[i].getLocationID();
            }
            fSortOrder = sorter.sortLong(temp, fSortOrder, up);
        }
        if ("count".equals(pColumn)) {
            Sorter sorter = new Sorter();
            long[] temp = new long[fTableData.length];
            for (int i = 0; i < temp.length; i++) {
                temp[i] = fTableData[i].getCount();
            }
            fSortOrder = sorter.sortLong(temp, fSortOrder, up);
        }
        if ("guarantee".equals(pColumn)) {
            Sorter sorter = new Sorter();
            long[] temp = new long[fTableData.length];
            for (int i = 0; i < temp.length; i++) {
                temp[i] = fTableData[i].getGuarantee();
            }
            fSortOrder = sorter.sortLong(temp, fSortOrder, up);
        }
        if ("availability".equals(pColumn)) {
            fComparator = new StringComparator();
            String[] temp = new String[fTableData.length];
            for (int i = 0; i < temp.length; i++) {
                temp[i] = fTableData[i].getAvailability();
            }
            sort(temp, 0, temp.length - 1, up);
        }
        if ("price".equals(pColumn)) {
            fComparator = new BigDecimalComparator();
            java.math.BigDecimal[] temp = new java.math.BigDecimal[fTableData.length];
            for (int i = 0; i < temp.length; i++) {
                temp[i] = fTableData[i].getPrice();
            }
            sort(temp, 0, temp.length - 1, up);
        }
        if ("discount".equals(pColumn)) {
            fComparator = new BigDecimalComparator();
            java.math.BigDecimal[] temp = new java.math.BigDecimal[fTableData.length];
            for (int i = 0; i < temp.length; i++) {
                temp[i] = fTableData[i].getDiscount();
            }
            sort(temp, 0, temp.length - 1, up);
        }
        if ("discountPercent".equals(pColumn)) {
            Sorter sorter = new Sorter();
            double[] temp = new double[fTableData.length];
            for (int i = 0; i < temp.length; i++) {
                temp[i] = fTableData[i].getDiscountPercent();
            }
            fSortOrder = sorter.sortDouble(temp, fSortOrder, up);
        }
        if ("vatID".equals(pColumn)) {
            Sorter sorter = new Sorter();
            long[] temp = new long[fTableData.length];
            for (int i = 0; i < temp.length; i++) {
                temp[i] = fTableData[i].getVatID();
            }
            fSortOrder = sorter.sortLong(temp, fSortOrder, up);
        }
        if ("barCode".equals(pColumn)) {
            fComparator = new StringComparator();
            String[] temp = new String[fTableData.length];
            for (int i = 0; i < temp.length; i++) {
                temp[i] = fTableData[i].getBarCode();
            }
            sort(temp, 0, temp.length - 1, up);
        }
        if ("serialNr".equals(pColumn)) {
            fComparator = new StringComparator();
            String[] temp = new String[fTableData.length];
            for (int i = 0; i < temp.length; i++) {
                temp[i] = fTableData[i].getSerialNr();
            }
            sort(temp, 0, temp.length - 1, up);
        }
        if ("deliveryDate".equals(pColumn)) {
            fComparator = new DateComparator();
            java.sql.Date[] temp = new java.sql.Date[fTableData.length];
            for (int i = 0; i < temp.length; i++) {
                temp[i] = fTableData[i].getDeliveryDate();
            }
            sort(temp, 0, temp.length - 1, up);
        }
        if ("expiryDate".equals(pColumn)) {
            fComparator = new DateComparator();
            java.sql.Date[] temp = new java.sql.Date[fTableData.length];
            for (int i = 0; i < temp.length; i++) {
                temp[i] = fTableData[i].getExpiryDate();
            }
            sort(temp, 0, temp.length - 1, up);
        }
        if ("pinEncrypted".equals(pColumn)) {
            fComparator = new StringComparator();
            String[] temp = new String[fTableData.length];
            for (int i = 0; i < temp.length; i++) {
                temp[i] = fTableData[i].getPinEncrypted();
            }
            sort(temp, 0, temp.length - 1, up);
        }
        if ("pinCode".equals(pColumn)) {
            fComparator = new StringComparator();
            String[] temp = new String[fTableData.length];
            for (int i = 0; i < temp.length; i++) {
                temp[i] = fTableData[i].getPinCode();
            }
            sort(temp, 0, temp.length - 1, up);
        }
        if ("modified".equals(pColumn)) {
            fComparator = new TimestampComparator();
            java.sql.Timestamp[] temp = new java.sql.Timestamp[fTableData.length];
            for (int i = 0; i < temp.length; i++) {
                temp[i] = fTableData[i].getModified();
            }
            sort(temp, 0, temp.length - 1, up);
        }
        if ("validFrom".equals(pColumn)) {
            fComparator = new DateComparator();
            java.sql.Date[] temp = new java.sql.Date[fTableData.length];
            for (int i = 0; i < temp.length; i++) {
                temp[i] = fTableData[i].getValidFrom();
            }
            sort(temp, 0, temp.length - 1, up);
        }
        if ("validTo".equals(pColumn)) {
            fComparator = new DateComparator();
            java.sql.Date[] temp = new java.sql.Date[fTableData.length];
            for (int i = 0; i < temp.length; i++) {
                temp[i] = fTableData[i].getValidTo();
            }
            sort(temp, 0, temp.length - 1, up);
        }
        fSortedColumn = pColumn;
    }

    @SuppressWarnings("unchecked")
    private void sort(Object[] a, int lo0, int hi0, boolean up) {
        int lo = lo0;
        int hi = hi0;
        if (lo >= hi) {
            return;
        }
        int mid = (lo + hi) / 2;
        sort(a, lo, mid, up);
        sort(a, mid + 1, hi, up);
        int end_lo = mid;
        int start_hi = mid + 1;
        while ((lo <= end_lo) && (start_hi <= hi)) {
            boolean isChange;
            if (up) {
                isChange = (fComparator.compare(a[fSortOrder[lo]], a[fSortOrder[start_hi]]) <= 0);
            } else {
                isChange = (fComparator.compare(a[fSortOrder[lo]], a[fSortOrder[start_hi]]) >= 0);
            }
            if (isChange) {
                lo++;
            } else {
                int T = fSortOrder[start_hi];
                for (int k = start_hi - 1; k >= lo; k--) {
                    fSortOrder[k + 1] = fSortOrder[k];
                }
                fSortOrder[lo] = T;
                lo++;
                end_lo++;
                start_hi++;
            }
        }
    }

    public String[] getSortedColumns() {
        if (fSortedColumn != null) {
            return new String[] { fSortedColumn };
        } else {
            return null;
        }
    }

    public Object getRowAt(int index) {
        return (fTableData != null ? fTableData[fSortOrder[index]] : null);
    }

    public Object getTotalRow() {
        return fTotal;
    }

    public int getRowCount() {
        return (fTableData != null) ? fTableData.length : 0;
    }
}
