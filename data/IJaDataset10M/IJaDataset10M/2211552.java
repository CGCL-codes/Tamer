package com.idna.utils;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class CsvRecordsHelper extends AbstractMap<String, String> {

    List<String> columnNames = new ArrayList<String>();

    List<String[]> records = new ArrayList<String[]>();

    public CsvRecordsHelper(String[] columnNames) {
    }

    public List<String> getRowDataAsList(int index) {
        List<String> list = Arrays.asList(records.get(index));
        return list;
    }

    public String[] getRowData(int index) {
        String[] data = records.get(index);
        return data;
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    public void addRecord(String[] record) {
        records.add(record);
    }

    @Override
    public int size() {
        return records.size();
    }

    @Override
    public Set<java.util.Map.Entry<String, String>> entrySet() {
        Set<java.util.Map.Entry<String, String>> set = new HashSet<java.util.Map.Entry<String, String>>();
        for (String[] record : records) {
            Map<String, String> map = new HashMap<String, String>();
            for (int i = 0; i < columnNames.size(); i++) {
                String column = columnNames.get(i);
                String recordField = record[i];
                map.put(column, recordField);
            }
        }
        return set;
    }
}
