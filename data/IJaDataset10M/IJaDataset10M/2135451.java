package com.cashier3.client.gui;

import java.util.ArrayList;
import java.util.List;
import com.cashier3.shared.Transaction;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;

public class TransactionTable {

    protected CellTable<Transaction> table;

    protected TextColumn<Transaction> valueColumn;

    protected TextColumn<Transaction> storeColumn;

    protected TextColumn<Transaction> userColumn;

    protected TextColumn<Transaction> dateColumn;

    protected List<Transaction> data;

    public TransactionTable() {
        table = new CellTable<Transaction>();
        data = new ArrayList<Transaction>();
        valueColumn = new TextColumn<Transaction>() {

            @Override
            public String getValue(Transaction ta) {
                return Double.toString(ta.getValue());
            }
        };
        storeColumn = new TextColumn<Transaction>() {

            @Override
            public String getValue(Transaction ta) {
                return ta.getStore();
            }
        };
        userColumn = new TextColumn<Transaction>() {

            @Override
            public String getValue(Transaction ta) {
                return ta.getDisplayName();
            }
        };
        dateColumn = new TextColumn<Transaction>() {

            @Override
            public String getValue(Transaction ta) {
                return ta.getDate().toString();
            }
        };
        table.addColumn(valueColumn, "Value");
        table.addColumn(storeColumn, "Store");
        table.addColumn(userColumn, "User");
        table.addColumn(dateColumn, "Date");
        table.setPageSize(5);
        updateTable();
    }

    public void addTransaction(Transaction ta) {
        data.add(ta);
    }

    public void emptyTableData() {
        data.clear();
    }

    public void updateTable() {
        table.setRowData(0, data);
        table.setRowCount(data.size());
        table.redraw();
    }

    public CellTable<Transaction> getTable() {
        return table;
    }
}
