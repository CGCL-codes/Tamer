package org.bifrost.tool;

import java.util.ArrayList;

public class FK {

    private ArrayList<Column> columnList;

    private Table references;

    public FK() {
        columnList = new ArrayList<Column>();
    }

    public ArrayList<Column> getColumnList() {
        return columnList;
    }

    public void setColumnList(ArrayList<Column> columnList) {
        this.columnList = columnList;
    }

    public Table getReferences() {
        return references;
    }

    public void setReferences(Table references) {
        this.references = references;
    }
}
