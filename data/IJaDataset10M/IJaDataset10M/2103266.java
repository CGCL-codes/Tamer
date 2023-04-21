package org.jfor.jfor.rtflib.rtfdoc;

import java.io.*;
import java.util.*;
import org.jfor.jfor.interfaces.ITableColumnsInfo;

public class RtfExtraRowSet extends RtfContainer {

    final int DEFAULT_IDNUM = 0;

    /** Parent table context
     * (added by Boris Poud�rous on july 2002 in order to process nested tables)
     */
    private ITableColumnsInfo parentITableColumnsInfo = null;

    /** While a top-level RtfTableRow is being rendered, we build a list of
     *  RtfTableCells that must be rendered in extra rows.
     *  This holds a cell with positioning information
     */
    private final List m_cells = new LinkedList();

    private static class PositionedCell implements Comparable {

        final RtfTableCell cell;

        final int xOffset;

        final int rowIndex;

        PositionedCell(RtfTableCell c, int index, int offset) {
            cell = c;
            xOffset = offset;
            rowIndex = index;
        }

        /** debugging dump */
        public String toString() {
            return "PositionedCell: row " + rowIndex + ", offset " + xOffset;
        }

        /** cells need to be sorted by row index and then by x offset */
        public int compareTo(Object o) {
            int result = 0;
            if (o == null) {
                result = 1;
            } else if (!(o instanceof PositionedCell)) {
                result = 1;
            } else {
                final PositionedCell pc = (PositionedCell) o;
                if (this.rowIndex < pc.rowIndex) {
                    result = -1;
                } else if (this.rowIndex > pc.rowIndex) {
                    result = 1;
                } else if (this.xOffset < pc.xOffset) {
                    result = -1;
                } else if (this.xOffset > pc.xOffset) {
                    result = 1;
                }
            }
            return result;
        }

        public boolean equals(Object o) {
            return o != null && this.compareTo(o) == 0;
        }
    }

    /** our maximum row index */
    private int m_maxRowIndex;

    /** an RtfExtraRowSet has no parent, it is only used temporary during
     *  generation of RTF for an RtfTableRow
     */
    RtfExtraRowSet(Writer w) throws IOException {
        super(null, w);
    }

    /** Add all cells of given Table to this set for later rendering in extra rows
     *  @return index of extra row to use for elements that follow this table in the same cell
     *  @param rowIndex index of first extra row to create to hold cells of tbl
     *  @param xOffset horizontal position of left edge of first column of tbl
     */
    int addTable(RtfTable tbl, int rowIndex, int xOffset) {
        for (Iterator it = tbl.getChildren().iterator(); it.hasNext(); ) {
            final RtfElement e = (RtfElement) it.next();
            if (e instanceof RtfTableRow) {
                addRow((RtfTableRow) e, rowIndex, xOffset);
                rowIndex++;
                m_maxRowIndex = Math.max(rowIndex, m_maxRowIndex);
            }
        }
        return rowIndex;
    }

    /** add all cells of given row to this set */
    private void addRow(RtfTableRow row, int rowIndex, int xOffset) {
        for (Iterator it = row.getChildren().iterator(); it.hasNext(); ) {
            final RtfElement e = (RtfElement) it.next();
            if (e instanceof RtfTableCell) {
                final RtfTableCell c = (RtfTableCell) e;
                m_cells.add(new PositionedCell(c, rowIndex, xOffset));
                xOffset += c.getCellWidth();
            }
        }
    }

    /** create an extra cell to hold content that comes after a nested table in a cell
     *  Modified by Boris Poud�rous in order to permit the extra cell to have the attributes of its parent cell
     */
    RtfTableCell createExtraCell(int rowIndex, int xOffset, int cellWidth, RtfAttributes parentCellAttributes) throws IOException {
        final RtfTableCell c = new RtfTableCell(null, m_writer, cellWidth, parentCellAttributes, DEFAULT_IDNUM);
        m_cells.add(new PositionedCell(c, rowIndex, xOffset));
        return c;
    }

    /** render extra RtfTableRows containing all the extra RtfTableCells that we contain */
    protected void writeRtfContent() throws IOException {
        Collections.sort(m_cells);
        List rowCells = null;
        int rowIndex = -1;
        for (Iterator it = m_cells.iterator(); it.hasNext(); ) {
            final PositionedCell pc = (PositionedCell) it.next();
            if (pc.rowIndex != rowIndex) {
                if (rowCells != null) writeRow(rowCells);
                rowIndex = pc.rowIndex;
                rowCells = new LinkedList();
            }
            rowCells.add(pc);
        }
        if (rowCells != null) writeRow(rowCells);
    }

    /** write one RtfTableRow containing given PositionedCells */
    private void writeRow(List cells) throws IOException {
        if (allCellsEmpty(cells)) return;
        final RtfTableRow row = new RtfTableRow(null, m_writer, DEFAULT_IDNUM);
        int cellIndex = 0;
        ITableColumnsInfo parentITableColumnsInfo = getParentITableColumnsInfo();
        parentITableColumnsInfo.selectFirstColumn();
        float xOffset = 0;
        float xOffsetOfLastPositionedCell = 0;
        for (Iterator it = cells.iterator(); it.hasNext(); ) {
            final PositionedCell pc = (PositionedCell) it.next();
            if (cellIndex == 0 && pc.xOffset > 0) {
                for (int i = 0; (xOffset < pc.xOffset) && (i < parentITableColumnsInfo.getNumberOfColumns()); i++) {
                    xOffset += parentITableColumnsInfo.getColumnWidth();
                    row.newTableCellMergedVertically((int) parentITableColumnsInfo.getColumnWidth(), pc.cell.m_attrib);
                    parentITableColumnsInfo.selectNextColumn();
                }
            }
            row.addChild(pc.cell);
            xOffsetOfLastPositionedCell = pc.xOffset + pc.cell.getCellWidth();
            cellIndex++;
        }
        if (parentITableColumnsInfo.getColumnIndex() < (parentITableColumnsInfo.getNumberOfColumns() - 1)) {
            parentITableColumnsInfo.selectNextColumn();
            while (parentITableColumnsInfo.getColumnIndex() < parentITableColumnsInfo.getNumberOfColumns()) {
                row.newTableCellMergedVertically((int) parentITableColumnsInfo.getColumnWidth(), m_attrib);
                parentITableColumnsInfo.selectNextColumn();
            }
        }
        row.writeRtf();
    }

    /** true if all cells of given list are empty
     *  @param cells List of PositionedCell objects
     */
    private static boolean allCellsEmpty(List cells) {
        boolean empty = true;
        for (Iterator it = cells.iterator(); it.hasNext(); ) {
            final PositionedCell pc = (PositionedCell) it.next();
            if (pc.cell.containsText()) {
                empty = false;
                break;
            }
        }
        return empty;
    }

    /** As this contains cells from several rows, we say that it's empty
     *  only if we have no cells.
     *  writeRow makes the decision about rendering specific rows
     */
    public boolean isEmpty() {
        return false;
    }

    /**
     * @return The table context of the parent table
     * Added by Boris Poud�rous on july 2002 in order to process nested tables
     */
    public ITableColumnsInfo getParentITableColumnsInfo() {
        return this.parentITableColumnsInfo;
    }

    public void setParentITableColumnsInfo(ITableColumnsInfo parentITableColumnsInfo) {
        this.parentITableColumnsInfo = parentITableColumnsInfo;
    }
}
