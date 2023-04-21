package com.googlecode.greenbridge.storyharvester.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ryan
 */
public class DefaultConfluenceContentParseStrategy implements ConfluenceContentParseStrategy {

    public static final String MARK = "{excerpt}";

    @Override
    public List<String> getNarrative(String content) {
        List<String> narratives = new ArrayList<String>();
        int firstIndex = content.indexOf(MARK) + MARK.length();
        String narrativeContent = content.substring(firstIndex, content.lastIndexOf(MARK));
        narrativeContent = narrativeContent.trim();
        String[] lines = narrativeContent.split("\r\n|\r|\n");
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            narratives.add(line);
        }
        return narratives;
    }

    @Override
    public Datatable getDatatable(String content) {
        String table = content.substring(content.indexOf("||"), content.lastIndexOf("|") + 1);
        String header = table.substring(0, table.lastIndexOf("||"));
        String[] columns = header.split("\\|\\|");
        if (columns.length == 0) return null;
        Datatable datatable = new Datatable();
        List<String> properties = new ArrayList<String>();
        datatable.setDatatableProperties(properties);
        for (int i = 1; i < columns.length; i++) {
            String prop = columns[i].trim();
            properties.add(prop);
        }
        List<Map<String, String>> dataset = new ArrayList<Map<String, String>>();
        datatable.setDatatable(dataset);
        String body = table.substring(table.lastIndexOf("||") + 4, table.length());
        body = body + " ";
        String[] rows = body.split("\n");
        for (int i = 0; i < rows.length; i++) {
            Map<String, String> datarow = new HashMap<String, String>();
            dataset.add(datarow);
            String row = rows[i];
            String[] cells = row.split("\\|");
            for (int j = 1; j < (cells.length - 1); j++) {
                String cell = cells[j].trim();
                datarow.put(properties.get(j - 1), cell);
            }
        }
        return datatable;
    }
}
