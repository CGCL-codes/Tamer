package com.pustral.comvey.client.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.google.gwt.gen2.table.client.FixedWidthFlexTable;
import com.google.gwt.gen2.table.client.FixedWidthGrid;
import com.google.gwt.gen2.table.client.ScrollTable;
import com.google.gwt.user.client.ui.CheckBox;

public class BismTable extends ScrollTable {

    private DataTable dataTable;

    private FixedWidthFlexTable headerTable;

    private ArrayList<String> fieldNames = new ArrayList<String>();

    private ArrayList<String> fieldTitles = new ArrayList<String>();

    private ArrayList<Integer> fieldWidths = new ArrayList<Integer>();

    private ArrayList<String> keyFields = new ArrayList<String>();

    public BismTable(DataTable dataTable, FixedWidthFlexTable headerTable) {
        super(dataTable, headerTable);
        this.dataTable = dataTable;
        this.headerTable = headerTable;
        setResizePolicy(ScrollTable.ResizePolicy.FILL_WIDTH);
    }

    public BismTable() {
        super(new DataTable(), new FixedWidthFlexTable());
        this.dataTable = (DataTable) getDataTable();
        this.headerTable = getHeaderTable();
        setResizePolicy(ScrollTable.ResizePolicy.FILL_WIDTH);
        this.setCellPadding(5);
        this.setCellSpacing(0);
    }

    public void addField(String fieldName, String title) {
        addField(fieldName, title, 0);
    }

    public void addField(String fieldName, String title, int width) {
        addField(fieldName, title, width, false);
    }

    public void addField(String fieldName, String title, boolean primaryKey) {
        addField(fieldName, title, 0, primaryKey);
    }

    public void addField(String fieldName, String title, int width, boolean primaryKey) {
        if (primaryKey) {
            keyFields.add(fieldName);
        }
        fieldNames.add(fieldName);
        fieldTitles.add(title);
        fieldWidths.add(width);
        int idx = fieldNames.indexOf(fieldName);
        headerTable.setText(0, idx, title);
    }

    public Map<String, String> getPrimaryKeyValues(int row) {
        Map<String, String> pkValueMap = new HashMap<String, String>();
        String[] rowValues = dataTable.getRowValues(row);
        for (String aKey : keyFields) {
            int keyIndex = fieldNames.indexOf(aKey);
            pkValueMap.put(aKey, rowValues[keyIndex]);
        }
        return pkValueMap;
    }

    public void setData(String[] data) {
        dataTable.resizeRows(0);
        int clmCount = this.getHeaderTable().getColumnCount();
        int rowCount = data.length / clmCount;
        dataTable.resize(rowCount, clmCount);
        int i = 0;
        int row = 0;
        while (i < data.length) {
            for (int clm = 0; clm < clmCount; clm++) {
                if (data[i] == null) data[i] = "";
                if (data[i].equalsIgnoreCase("true") || data[i].equalsIgnoreCase("false")) {
                    CheckBox cb = new CheckBox();
                    cb.setValue(data[i].equalsIgnoreCase("true"));
                    cb.setEnabled(false);
                    dataTable.setWidget(row, clm, cb);
                } else {
                    dataTable.setText(row, clm, data[i]);
                }
                i++;
            }
            row++;
        }
    }

    public void addDblClickHandler(DataTable.EHDblClick clickHandler) {
        dataTable.addDblClickHandler(clickHandler);
    }

    public String[] getFieldNames() {
        if (fieldNames.size() == 0) return null;
        String[] names = new String[fieldNames.size()];
        fieldNames.toArray(names);
        return names;
    }
}
