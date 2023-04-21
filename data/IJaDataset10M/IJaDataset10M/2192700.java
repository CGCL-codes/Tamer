package net.sourceforge.transumanza.writer.db.md;

public class TableColumnMetadata {

    public int ordinalPosition;

    public String columnName;

    public short dataType;

    public int columnSize;

    public String toString() {
        return "ordinalPosition=" + ordinalPosition + ", columnName=" + columnName + ", dataType=" + dataType + ", columnSize=" + columnSize;
    }
}
