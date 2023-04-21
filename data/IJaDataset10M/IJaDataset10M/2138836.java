package com.yeep.universedesign.model;

import java.io.Serializable;
import java.util.List;

/**
 * Table Class
 * @author Yiro
 */
@SuppressWarnings("serial")
public class Table implements Serializable {

    private String id;

    private String tableName;

    private List<Column> columns;

    protected Table() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }
}
