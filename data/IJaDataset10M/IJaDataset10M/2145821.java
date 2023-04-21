package org.plazmaforge.framework.datawarehouse.transfer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransferBlock extends AbstarctTransferQuery {

    private String sourcesAttribute;

    private String targetsAttribute;

    private String sourceTablesAttribute;

    private String targetTablesAttribute;

    private Map<String, List<String>> sourceTableColumns = new HashMap<String, List<String>>();

    private Map<String, List<String>> targetTableColumns = new HashMap<String, List<String>>();

    private List<String> sourceTables;

    private List<String> targetTables;

    public String getSourcesAttribute() {
        return sourcesAttribute;
    }

    public void setSourcesAttribute(String sourcesAttribute) {
        this.sourcesAttribute = sourcesAttribute;
    }

    public String getSourceTablesAttribute() {
        return sourceTablesAttribute;
    }

    public void setSourceTablesAttribute(String sourceTablesAttribute) {
        this.sourceTablesAttribute = sourceTablesAttribute;
    }

    public String getTargetsAttribute() {
        return targetsAttribute;
    }

    public void setTargetsAttribute(String targetsAttribute) {
        this.targetsAttribute = targetsAttribute;
    }

    public String getTargetTablesAttribute() {
        return targetTablesAttribute;
    }

    public void setTargetTablesAttribute(String targetTablesAttribute) {
        this.targetTablesAttribute = targetTablesAttribute;
    }

    public Map<String, List<String>> getSourceTableColumns() {
        if (sourceTableColumns == null) {
            sourceTableColumns = new HashMap<String, List<String>>();
        }
        return sourceTableColumns;
    }

    public void addSourceTableColumns(String table, List<String> columns) {
        getSourceTableColumns().put(table, columns);
    }

    public Map<String, List<String>> getTargetTableColumns() {
        if (targetTableColumns == null) {
            targetTableColumns = new HashMap<String, List<String>>();
        }
        return targetTableColumns;
    }

    public void addTargetTableColumns(String table, List<String> columns) {
        getTargetTableColumns().put(table, columns);
    }

    public List<String> getSourceTables() {
        return sourceTables;
    }

    public void setSourceTables(List<String> sourceTables) {
        this.sourceTables = sourceTables;
    }

    public List<String> getTargetTables() {
        return targetTables;
    }

    public void setTargetTables(List<String> targetTables) {
        this.targetTables = targetTables;
    }
}
