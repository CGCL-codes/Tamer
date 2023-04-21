package org.qsardb.conversion.table;

import java.util.*;

public class TableSetup {

    private int offset = -1;

    private String beginRow = null;

    private String endRow = null;

    private Set<String> ignoredRows = new LinkedHashSet<String>();

    private boolean copyLabels = true;

    private Map<String, List<Mapping>> columnMappings = new LinkedHashMap<String, List<Mapping>>();

    public void reset() {
        this.offset = -1;
        this.beginRow = null;
        this.endRow = null;
        this.ignoredRows.clear();
        this.copyLabels = true;
        this.columnMappings.clear();
    }

    public int getOffset() {
        return this.offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String getBeginRow() {
        return this.beginRow;
    }

    public void setBeginRow(String beginRow) {
        this.beginRow = beginRow;
    }

    public String getEndRow() {
        return this.endRow;
    }

    public void setEndRow(String endRow) {
        this.endRow = endRow;
    }

    public boolean isIgnored(String id) {
        return this.ignoredRows.contains(id);
    }

    public void setIgnored(String id) {
        this.ignoredRows.add(id);
    }

    public void clearIgnored() {
        this.ignoredRows.clear();
    }

    public boolean getCopyLabels() {
        return this.copyLabels;
    }

    public void setCopyLabels(boolean copyLabels) {
        this.copyLabels = copyLabels;
    }

    public List<Mapping> getMappings(int column) {
        return getMappings(id(column));
    }

    public List<Mapping> getMappings(String id) {
        return this.columnMappings.get(id);
    }

    public void addMapping(String id, int column, Mapping mapping) {
        addMapping(id, mapping);
        addMapping(column, mapping);
    }

    public void addMapping(int column, Mapping mapping) {
        addMapping(id(column), mapping);
    }

    public void addMapping(String id, Mapping mapping) {
        List<Mapping> mappings = this.columnMappings.get(id);
        if (mappings == null) {
            mappings = new ArrayList<Mapping>();
            this.columnMappings.put(id, mappings);
        }
        mappings.add(mapping);
    }

    public void removeMapping(String id, int column, Mapping mapping) {
        removeMapping(id, mapping);
        removeMapping(column, mapping);
    }

    public void removeMapping(int column, Mapping mapping) {
        removeMapping(id(column), mapping);
    }

    public void removeMapping(String id, Mapping mapping) {
        List<Mapping> mappings = this.columnMappings.get(id);
        if (mappings != null) {
            mappings.remove(mapping);
            if (mappings.isEmpty()) {
                this.columnMappings.remove(id);
            }
        }
    }

    private static String id(int column) {
        return "_" + String.valueOf(column + 1);
    }
}
