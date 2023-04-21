package org.wicketrad.jdbc.metadata;

import java.io.Serializable;
import java.sql.Connection;
import java.util.List;

public class TableMetadata implements Serializable {

    private String name;

    private List<ColumnMetadata> columns;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ColumnMetadata> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnMetadata> columns) {
        this.columns = columns;
    }

    public TableMetadata(String name, Connection con) {
    }
}
