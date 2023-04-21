package symore.sentinel;

import java.io.Serializable;
import symore.sql.lang.TableName;

/**
 * This class is used to identify a sentinel. The identifier is build from the table, row, col and type
 * of the sentinel so that two independent classes describing the same sentinel are equal.
 * @author Frank Bregulla, Manuel Scholz
 *
 */
public class SentinelId implements Serializable {

    private static final long serialVersionUID = 6338619746521195066L;

    private TableName table;

    private String row;

    private String col;

    private int type;

    public SentinelId(TableName table, String row, String col, int type) {
        this.table = table;
        this.row = row;
        this.col = col;
        this.type = type;
    }

    public SentinelId(TableName table) {
        super();
        this.table = table;
        this.type = SentinelDefinition.TABLESENTINEL;
    }

    /**
	 * Creates a new sentinelId. Creates a rowSentinel if row=true, a colSentinel otherwise
	 * @param table
	 * @param Id rowId or columnName
	 * @param row boolean
	 */
    public SentinelId(TableName table, String Id, boolean row) {
        this.table = table;
        if (row) {
            this.row = Id;
            this.type = SentinelDefinition.ROWSENTINEL;
        } else {
            this.col = Id;
            this.type = SentinelDefinition.COLUMNSENTINEL;
        }
    }

    public SentinelId(TableName table, String rowId, String colId) {
        this.table = table;
        this.row = rowId;
        this.col = colId;
        this.type = SentinelDefinition.CELLSENTINEL;
    }

    public boolean equals(Object o) {
        boolean ret = false;
        if (o instanceof SentinelId) {
            SentinelId sId = (SentinelId) o;
            if ((table != null)) ret = (this.table.equals(sId.table) && (this.type == sId.type)); else ret = ((sId.table == null) && (this.type == sId.type));
            if (row != null) ret = ret && this.row.equals(sId.row); else ret = ret && (sId.row == null);
            if (col != null) ret = ret && this.col.toUpperCase().equals(sId.col.toUpperCase()); else ret = ret && (sId.col == null);
        }
        return ret;
    }

    public int hashCode() {
        int hash = new Integer(type).hashCode();
        if (table != null) hash += table.hashCode();
        if (row != null) hash += row.hashCode();
        if (col != null) hash += col.toUpperCase().hashCode();
        return hash;
    }

    public String getCol() {
        return col;
    }

    public String getRow() {
        return row;
    }

    public TableName getTable() {
        return table;
    }

    public int getType() {
        return type;
    }

    public String toString() {
        switch(type) {
            case SentinelDefinition.TABLESENTINEL:
                return "TABLE: " + this.getTable().toString();
            case SentinelDefinition.ROWSENTINEL:
                return "ROW: " + this.getTable().toString() + "." + this.getRow().toString();
            case SentinelDefinition.COLUMNSENTINEL:
                return "COLUMN: " + this.getTable() + "." + this.getCol();
            case SentinelDefinition.CELLSENTINEL:
                return "CELL: " + this.getTable() + "." + this.getRow() + "." + this.getCol();
            default:
                throw new IllegalStateException("" + type);
        }
    }
}
