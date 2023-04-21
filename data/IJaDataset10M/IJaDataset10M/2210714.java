package com.be.table;

import java.util.*;
import com.be.vo.JournalVO;
import com.util.comparator.*;
import com.util.table.*;

public class JournalModel implements ITableModel {

    protected JournalVO fTotal;

    protected JournalVO[] fTableData;

    protected String fSortedColumn;

    protected int[] fSortOrder;

    protected Comparator fComparator;

    public JournalModel() {
        super();
    }

    public void setData(Object[] data) {
        fTableData = (JournalVO[]) data;
        fSortOrder = new int[(fTableData != null) ? fTableData.length : 0];
        for (int i = 0; i < fSortOrder.length; i++) {
            fSortOrder[i] = i;
        }
    }

    public Object getValueAt(int pRow, String pColumn) {
        JournalVO vo = fTableData[fSortOrder[pRow]];
        if ("id".equals(pColumn)) return new Long(vo.getId());
        if ("orderID".equals(pColumn)) return new Long(vo.getOrderID());
        if ("journalID".equals(pColumn)) return new Long(vo.getJournalID());
        if ("bookDate".equals(pColumn)) return vo.getBookDate();
        if ("valueDate".equals(pColumn)) return vo.getValueDate();
        if ("ledgerDebit".equals(pColumn)) return new Long(vo.getLedgerDebit());
        if ("amountDebit".equals(pColumn)) return vo.getAmountDebit();
        if ("currencyDebitID".equals(pColumn)) return new Short(vo.getCurrencyDebitID());
        if ("exchangeRateDebit".equals(pColumn)) return new Double(vo.getExchangeRateDebit());
        if ("amountDebitReal".equals(pColumn)) return vo.getAmountDebitReal();
        if ("ledgerCredit".equals(pColumn)) return new Long(vo.getLedgerCredit());
        if ("amountCredit".equals(pColumn)) return vo.getAmountCredit();
        if ("currencyCreditID".equals(pColumn)) return new Short(vo.getCurrencyCreditID());
        if ("exchangeRateCredit".equals(pColumn)) return new Double(vo.getExchangeRateCredit());
        if ("amountCreditReal".equals(pColumn)) return vo.getAmountCreditReal();
        return null;
    }

    public Object getTotalValueAt(String pColumn) {
        if (fTotal != null) {
            if ("id".equals(pColumn)) return new Long(fTotal.getId());
            if ("orderID".equals(pColumn)) return new Long(fTotal.getOrderID());
            if ("journalID".equals(pColumn)) return new Long(fTotal.getJournalID());
            if ("bookDate".equals(pColumn)) return fTotal.getBookDate();
            if ("valueDate".equals(pColumn)) return fTotal.getValueDate();
            if ("ledgerDebit".equals(pColumn)) return new Long(fTotal.getLedgerDebit());
            if ("amountDebit".equals(pColumn)) return fTotal.getAmountDebit();
            if ("currencyDebitID".equals(pColumn)) return new Short(fTotal.getCurrencyDebitID());
            if ("exchangeRateDebit".equals(pColumn)) return new Double(fTotal.getExchangeRateDebit());
            if ("amountDebitReal".equals(pColumn)) return fTotal.getAmountDebitReal();
            if ("ledgerCredit".equals(pColumn)) return new Long(fTotal.getLedgerCredit());
            if ("amountCredit".equals(pColumn)) return fTotal.getAmountCredit();
            if ("currencyCreditID".equals(pColumn)) return new Short(fTotal.getCurrencyCreditID());
            if ("exchangeRateCredit".equals(pColumn)) return new Double(fTotal.getExchangeRateCredit());
            if ("amountCreditReal".equals(pColumn)) return fTotal.getAmountCreditReal();
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
        if ("orderID".equals(pColumn)) {
            Sorter sorter = new Sorter();
            long[] temp = new long[fTableData.length];
            for (int i = 0; i < temp.length; i++) {
                temp[i] = fTableData[i].getOrderID();
            }
            fSortOrder = sorter.sortLong(temp, fSortOrder, up);
        }
        if ("journalID".equals(pColumn)) {
            Sorter sorter = new Sorter();
            long[] temp = new long[fTableData.length];
            for (int i = 0; i < temp.length; i++) {
                temp[i] = fTableData[i].getJournalID();
            }
            fSortOrder = sorter.sortLong(temp, fSortOrder, up);
        }
        if ("bookDate".equals(pColumn)) {
            fComparator = new DateComparator();
            java.sql.Date[] temp = new java.sql.Date[fTableData.length];
            for (int i = 0; i < temp.length; i++) {
                temp[i] = fTableData[i].getBookDate();
            }
            sort(temp, 0, temp.length - 1, up);
        }
        if ("valueDate".equals(pColumn)) {
            fComparator = new DateComparator();
            java.sql.Date[] temp = new java.sql.Date[fTableData.length];
            for (int i = 0; i < temp.length; i++) {
                temp[i] = fTableData[i].getValueDate();
            }
            sort(temp, 0, temp.length - 1, up);
        }
        if ("ledgerDebit".equals(pColumn)) {
            Sorter sorter = new Sorter();
            long[] temp = new long[fTableData.length];
            for (int i = 0; i < temp.length; i++) {
                temp[i] = fTableData[i].getLedgerDebit();
            }
            fSortOrder = sorter.sortLong(temp, fSortOrder, up);
        }
        if ("amountDebit".equals(pColumn)) {
            fComparator = new BigDecimalComparator();
            java.math.BigDecimal[] temp = new java.math.BigDecimal[fTableData.length];
            for (int i = 0; i < temp.length; i++) {
                temp[i] = fTableData[i].getAmountDebit();
            }
            sort(temp, 0, temp.length - 1, up);
        }
        if ("currencyDebitID".equals(pColumn)) {
            Sorter sorter = new Sorter();
            short[] temp = new short[fTableData.length];
            for (int i = 0; i < temp.length; i++) {
                temp[i] = fTableData[i].getCurrencyDebitID();
            }
            fSortOrder = sorter.sortShort(temp, fSortOrder, up);
        }
        if ("exchangeRateDebit".equals(pColumn)) {
            Sorter sorter = new Sorter();
            double[] temp = new double[fTableData.length];
            for (int i = 0; i < temp.length; i++) {
                temp[i] = fTableData[i].getExchangeRateDebit();
            }
            fSortOrder = sorter.sortDouble(temp, fSortOrder, up);
        }
        if ("amountDebitReal".equals(pColumn)) {
            fComparator = new BigDecimalComparator();
            java.math.BigDecimal[] temp = new java.math.BigDecimal[fTableData.length];
            for (int i = 0; i < temp.length; i++) {
                temp[i] = fTableData[i].getAmountDebitReal();
            }
            sort(temp, 0, temp.length - 1, up);
        }
        if ("ledgerCredit".equals(pColumn)) {
            Sorter sorter = new Sorter();
            long[] temp = new long[fTableData.length];
            for (int i = 0; i < temp.length; i++) {
                temp[i] = fTableData[i].getLedgerCredit();
            }
            fSortOrder = sorter.sortLong(temp, fSortOrder, up);
        }
        if ("amountCredit".equals(pColumn)) {
            fComparator = new BigDecimalComparator();
            java.math.BigDecimal[] temp = new java.math.BigDecimal[fTableData.length];
            for (int i = 0; i < temp.length; i++) {
                temp[i] = fTableData[i].getAmountCredit();
            }
            sort(temp, 0, temp.length - 1, up);
        }
        if ("currencyCreditID".equals(pColumn)) {
            Sorter sorter = new Sorter();
            short[] temp = new short[fTableData.length];
            for (int i = 0; i < temp.length; i++) {
                temp[i] = fTableData[i].getCurrencyCreditID();
            }
            fSortOrder = sorter.sortShort(temp, fSortOrder, up);
        }
        if ("exchangeRateCredit".equals(pColumn)) {
            Sorter sorter = new Sorter();
            double[] temp = new double[fTableData.length];
            for (int i = 0; i < temp.length; i++) {
                temp[i] = fTableData[i].getExchangeRateCredit();
            }
            fSortOrder = sorter.sortDouble(temp, fSortOrder, up);
        }
        if ("amountCreditReal".equals(pColumn)) {
            fComparator = new BigDecimalComparator();
            java.math.BigDecimal[] temp = new java.math.BigDecimal[fTableData.length];
            for (int i = 0; i < temp.length; i++) {
                temp[i] = fTableData[i].getAmountCreditReal();
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
