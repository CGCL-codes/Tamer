package com.pyrphoros.erddb.model.data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 */
public class ForeignKey extends Constraint implements Serializable {

    private ArrayList<String> originalColumns;

    private String originalTable;

    private ACTION onUpdate;

    private ACTION onDelete;

    public enum ACTION implements Serializable {

        NOACTION, CASCADE, SETNULL, SETDEFAULT, RESTRICT
    }

    public ForeignKey(String name) {
        super(name, TYPE.FK);
        originalColumns = new ArrayList<String>();
    }

    public ForeignKey(ForeignKey fk) {
        super(fk);
        this.setOriginalColumns(new ArrayList<String>(fk.getOriginalColumns()));
        this.setOriginalTable(fk.getOriginalTable());
        this.setOnDelete(fk.getOnDelete());
        this.setOnUpdate(fk.getOnUpdate());
    }

    public void addOriginalColumn(String column) {
        originalColumns.add(column);
    }

    public boolean hasOriginalColumn(String column) {
        return originalColumns.contains(column);
    }

    public String getOriginalTable() {
        return originalTable;
    }

    public void setOriginalTable(String originalTable) {
        this.originalTable = originalTable;
    }

    public ArrayList<String> getOriginalColumns() {
        return originalColumns;
    }

    public void setOriginalColumns(ArrayList<String> originalColumns) {
        this.originalColumns = originalColumns;
    }

    public ACTION getOnDelete() {
        return onDelete;
    }

    public void setOnDelete(ACTION onDelete) {
        this.onDelete = onDelete;
    }

    public ACTION getOnUpdate() {
        return onUpdate;
    }

    public void setOnUpdate(ACTION onUpdate) {
        this.onUpdate = onUpdate;
    }
}
