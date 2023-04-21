package net.sourceforge.ehep.gui;

import net.sourceforge.ehep.core.EHEP;

/**
 * @author Marcel Palko
 * @author randallco@users.sourceforge.net
 */
public class HexTablePointer {

    private int rowIndex;

    private int columnIndex;

    public HexTablePointer(int rowIndex, int columnIndex) {
        super();
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
    }

    /**
	 * Zero-based projection from the pointer position (row,column) to index 
	 * @return zero-based position index
	 */
    public int getOffset() {
        return rowIndex * EHEP.TABLE_NUM_DATA_COLUMNS + columnIndex;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public HexTablePointer move(int offset) {
        int newOffset = getOffset() + offset;
        rowIndex = newOffset / EHEP.TABLE_NUM_DATA_COLUMNS;
        columnIndex = newOffset % EHEP.TABLE_NUM_DATA_COLUMNS;
        return this;
    }

    public boolean equals(HexTablePointer p) {
        return (rowIndex == p.getRowIndex() && columnIndex == p.getColumnIndex());
    }

    public HexTablePointer adjust() {
        if (rowIndex < 0) rowIndex = 0;
        if (columnIndex < 0) columnIndex = 0;
        return this;
    }

    public String toString() {
        return "Pointer: (row=" + rowIndex + ", column=" + columnIndex + ")";
    }
}
