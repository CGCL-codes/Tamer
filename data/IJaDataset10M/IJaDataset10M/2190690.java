package orinoco.layout;

import java.io.IOException;
import orinoco.Table;
import orinoco.Column;
import orinoco.Alignment;
import orinoco.TextWriter;
import orinoco.Colour;

/**
 * A table, which lays out text in columns. This table only contains the cells
 * for one row at a time, which results in performance. It does, however, keep
 * hold of its column headings, enabling them to be re-written each time a new
 * page is thrown
 */
public class TableImpl implements Table {

    /**
	 * The overall containing document
	 */
    private DocumentLayout doc;

    /**
	 * The column definitions
	 */
    private ColumnDefinition[] columns;

    /**
	 * The column headings
	 */
    private HeaderDefinition[] headings;

    /**
	 * The owning flow object
	 */
    private BaseWriter writer;

    /**
	 * The amount of space between rows in points
	 */
    private double rowSpacing;

    /**
	 * The amount of space between columns in points
	 */
    private double columnSpacing;

    /**
	 * The total width taken up by this table
	 */
    private double tableWidth;

    /**
	 * The column positions
	 */
    private int[] columnPositions;

    /**
	 * The cells in the current row
	 */
    private Cell[] cells;

    /**
	 * The headings
	 */
    private Cell[] headingCells;

    /**
	 * The row height of the table headings row
	 */
    private double headingRowHeight;

    /**
	 * The descent of the fonts used in the heading
	 */
    private int headingDescent;

    /**
	 * The background for the column headings
	 */
    private Colour headingBackground;

    /**
	 * The background colour for the current row
	 */
    private Colour background;

    /**
	 * Flag to indicate whether there is an outstanding row to be written
	 */
    private boolean rowPending;

    /**
	 * The border to be drawn after each row
	 */
    private int rowBorder;

    /**
	 * The border to be drawn in between each column
	 */
    private int columnBorder;

    /**
	 * The border to be drawn at the top of each column heading
	 */
    private int topHeadingBorder;

    /**
	 * The border to be drawn at the bottom of each heading border
	 */
    private int bottomHeadingBorder;

    /**
	 * The border to be drawn around the entire table
	 */
    private int tableBorder;

    /**
	 * The start x pos of the table on this page
	 */
    private double tableXPos;

    /**
	 * The start y pos of the table on this page
	 */
    private double tableYPos;

    /**
	 * Closed
	 */
    private boolean closed;

    /**
	 * Initialized
	 */
    private boolean initialized;

    /**
	 * Flag to write the headings, even if the table is empty. Default is FALSE
	 */
    private boolean writeHeadingsIfEmpty;

    /**
	 * Constructor
	 * 
	 * @param w
	 *            the base writer
	 * @param dl
	 *            the owning document
	 * @param hdrs
	 *            the headers
	 * @param cols
	 *            the columns
	 */
    TableImpl(ColumnDefinition[] cols, HeaderDefinition[] hdrs, BaseWriter w, DocumentLayout dl) {
        columns = cols;
        writer = w;
        doc = dl;
        cells = new Cell[cols.length];
        headingBackground = Colour.DEFAULT_BACKGROUND;
        background = Colour.DEFAULT_BACKGROUND;
        rowPending = false;
        closed = false;
        initialized = false;
        writeHeadingsIfEmpty = false;
        rowBorder = 0;
        topHeadingBorder = 0;
        bottomHeadingBorder = 0;
        if (hdrs != null) {
            headings = new HeaderDefinition[cols.length];
            int headingNumber = 0;
            for (int i = 0; i < hdrs.length && headingNumber < headings.length; i++) {
                if (hdrs[i] == null) {
                    headings[headingNumber] = new BlankHeading();
                    headingNumber++;
                } else {
                    headings[headingNumber] = hdrs[i];
                    int columnSpan = headings[headingNumber].getColumnSpan();
                    columnSpan = Math.min(headings.length - headingNumber, columnSpan);
                    headings[headingNumber].setColumnSpan(columnSpan);
                    for (int j = 1; j < columnSpan; j++) {
                        headings[headingNumber + j] = new SpannedHeading(headingNumber);
                    }
                    headingNumber += columnSpan;
                }
            }
        }
    }

    /**
	 * Sets up the internal data for this table, such as the column positions
	 */
    private void init() {
        columnPositions = new int[columns.length];
        int pos = 0;
        for (int i = 0; i < columns.length; i++) {
            columnPositions[i] = pos;
            pos += columns[i].getWidth() + columnSpacing + columnBorder;
        }
        tableWidth = pos - columnSpacing - columnBorder;
        if (headings != null) {
            headingCells = new Cell[columns.length];
            int i = 0;
            while (i < columns.length && i < headings.length) {
                headings[i].init(columns[i]);
                int width = 0;
                int columnSpan = headings[i].getColumnSpan();
                for (int j = 0; j < columnSpan; j++) {
                    width += columns[i + j].getWidth();
                }
                width += (columnSpan - 1) * columnSpacing;
                headingCells[i] = new Cell(headings[i].getText(), width, headings[i].getFont(), headings[i].getAlignment(), writer.getWriteFunctions());
                headingRowHeight = Math.max(headingRowHeight, headingCells[i].getHeight());
                headingDescent = Math.max(headingDescent, headingCells[i].getFont().getPointSize());
                i += columnSpan;
            }
            headingDescent = headingDescent / 3;
        }
        initialized = true;
    }

    /**
	 * Adds a row to the table, consisting of the specified strings
	 * 
	 * @exception IOException
	 * @param data
	 *            the cell data
	 */
    public void addRow(String[] data) throws IOException {
        addCells(data);
        writeRow();
    }

    /**
	 * Gets a writer for the specified cell
	 * 
	 * @param col
	 *            the zero-based column index
	 * @return the cell writer
	 */
    public TextWriter getCellWriter(int col, boolean borderTop, boolean borderBottom, boolean borderLeft, boolean borderRight) {
        if (col < 0 || col >= columns.length) {
            System.err.println("Warning:  column " + col + " not in table");
            return null;
        }
        ColumnDefinition column = columns[col];
        cells[col] = new Cell(column.getWidth(), column.getFont(), column.getAlignment(), writer.getWriteFunctions(), borderTop, borderBottom, borderLeft, borderRight);
        return new CellWriter(cells[col]);
    }

    /**
	 * Gets a writer for the specified cell
	 * 
	 * @param col
	 *            the zero-based column index
	 * @param colspan
	 *            the number of columns this text writer will span
	 * @return the cell writer
	 */
    public TextWriter getCellWriter(int col, int colspan, boolean borderTop, boolean borderBottom, boolean borderLeft, boolean borderRight) {
        if (col < 0 || col >= columns.length || colspan < 1 || col + colspan > columns.length) {
            System.err.println("Warning:  colspan " + col + "-" + (col + colspan) + " not in table");
            return null;
        }
        ColumnDefinition column = columns[col];
        int width = (int) Math.round(column.getWidth());
        for (int i = 0; i < colspan - 1; i++) {
            width += columnSpacing;
            width += columns[col + 1 + i].getWidth();
        }
        cells[col] = new Cell(width, column.getFont(), column.getAlignment(), writer.getWriteFunctions(), borderTop, borderBottom, borderLeft, borderRight);
        return new CellWriter(cells[col]);
    }

    /**
	 * Adds a new row consisting of empty cells
	 * 
	 * @exception IOException
	 */
    public void addCells() throws IOException {
        addCells(new String[0]);
    }

    /**
	 * Adds cells to the table, but doesn't write out the row
	 * 
	 * @exception IOException
	 * @param data
	 *            the cell data
	 */
    public void addCells(String[] data) throws IOException {
        if (!initialized) {
            init();
            writeHeadings();
        }
        for (int i = 0; i < columns.length; i++) {
            if ((i >= data.length) || (i < data.length && data[i] == null)) {
                cells[i] = null;
            } else {
                cells[i] = new Cell(data[i], columns[i].getWidth(), columns[i].getFont(), columns[i].getAlignment(), writer.getWriteFunctions());
            }
        }
        rowPending = true;
    }

    /**
	 * Writes out the cells in the current row
	 * 
	 * @exception IOException
	 */
    public void writeRow() throws IOException {
        double rowHeight = 0;
        BaseFont f = writer.getFont();
        int maxPointSize = 0;
        for (int i = 0; i < cells.length; i++) {
            if (cells[i] != null) {
                rowHeight = Math.max(rowHeight, cells[i].getHeight());
                maxPointSize = Math.max(cells[i].getFont().getPointSize(), maxPointSize);
            }
        }
        double rowYPos = writer.getYPos();
        if (rowYPos + rowHeight > doc.getHeight() - doc.getFooterHeight()) {
            writePageLines(true);
            doc.writeNewPage();
            writeHeadings();
            rowYPos = writer.getYPos();
        }
        if (background != Colour.DEFAULT_BACKGROUND) {
            writer.getWriteFunctions().writeFillBox(0, rowYPos, tableWidth, rowHeight + rowSpacing + maxPointSize / 4, background);
        }
        writeCells(cells, rowYPos);
        double newYPos = rowYPos + rowHeight + rowSpacing;
        writer.getWriteFunctions().writeMoveTo(0, newYPos);
        writer.baseSetFont(f);
        background = Colour.DEFAULT_BACKGROUND;
        cells = new Cell[columns.length];
        rowPending = false;
        if (rowBorder != 0) {
            drawLine(rowBorder);
        }
    }

    /**
	 * Closes off the table
	 * 
	 * @exception IOException
	 */
    public void close() throws IOException {
        if (closed) {
            return;
        }
        if (!initialized) {
            if (writeHeadingsIfEmpty) {
                init();
                writeHeadings();
                writePageLines(false);
            }
            writer.closeTable();
            closed = true;
            return;
        }
        if (rowPending) {
            writeRow();
        }
        writePageLines(true);
        writer.closeTable();
        closed = true;
    }

    /**
	 * Sets the background colour for the column headings
	 * 
	 * @param c
	 *            the background colour for the headings
	 */
    public void setHeaderBackground(Colour c) {
        headingBackground = c;
    }

    /**
	 * Sets the background colour for the current row. This will only affect the
	 * current row - subsequent rows will have the default background unless
	 * setBackground is called again
	 * 
	 * @param c
	 *            the background colour for the current row
	 */
    public void setBackground(Colour c) {
        background = c;
    }

    /**
	 * Sets the vertical spacing for all subsequent rows
	 * 
	 * @param amount
	 *            the space in cm
	 */
    public void setRowSpacing(double amount) {
        rowSpacing = amount * Constants.POINTS_PER_CM;
    }

    /**
	 * 
	 * Sets the horizontal spacing between columns for all subsequent rows
	 * 
	 * @param amount
	 *            the space in cm
	 */
    public void setColumnSpacing(double amount) {
        columnSpacing = amount * Constants.POINTS_PER_CM;
    }

    /**
	 * Writes out the cells
	 * 
	 * @exception IOException
	 * @param c
	 *            the cells to write
	 * @param rowYPos
	 *            the position of the cursor before this row was written
	 */
    private void writeCells(Cell[] c, double rowYPos) throws IOException {
        double lineHeight = 0;
        for (int i = 0; i < c.length; i++) {
            if (c[i] != null) {
                Line[] lines = c[i].getLines();
                double cellHeight = 0;
                for (int j = 0; j < lines.length; j++) {
                    cellHeight += lines[j].getHeight();
                }
                lineHeight = Math.max(lineHeight, cellHeight);
            }
        }
        for (int i = 0; i < c.length; i++) {
            if (c[i] != null) {
                writer.getWriteFunctions().writeSetY(rowYPos);
                double x1 = columnPositions[i] - (columnSpacing + columnBorder) / 2;
                double x2 = x1 + c[i].getWidth() + columnSpacing;
                double y1 = writer.getYPos();
                Line[] lines = c[i].getLines();
                for (int j = 0; j < lines.length; j++) {
                    writeLine(lines[j], columnPositions[i], c[i].getAlignment(), c[i].getWidth());
                }
                double y2 = y1 + lineHeight + rowSpacing;
                if (c[i].isBorderTop()) {
                    writer.getWriteFunctions().writeDrawLine(x1, y1, x2, y1, 1);
                }
                if (c[i].isBorderBottom()) {
                    writer.getWriteFunctions().writeDrawLine(x1, y2, x2, y2, 1);
                }
                if (c[i].isBorderLeft()) {
                    writer.getWriteFunctions().writeDrawLine(x1, y1, x1, y2, 1);
                }
                if (c[i].isBorderRight()) {
                    writer.getWriteFunctions().writeDrawLine(x2, y1, x2, y2, 1);
                }
            }
        }
    }

    /**
	 * Writes out the heading cells, taking account of spanned headings
	 * 
	 * @exception IOException
	 * @param c
	 *            the cells to write
	 * @param rowYPos
	 *            the position of the cursor before this row was written
	 */
    private void writeHeadingCells(Cell[] c, double rowYPos) throws IOException {
        double xpos = 0;
        for (int i = 0; i < c.length; i++) {
            if (c[i] != null) {
                int span = headings[i].getColumnSpan();
                double width = 0;
                for (int j = 0; j < span; j++) {
                    width += columns[i + j].getWidth();
                }
                width += (span - 1) * columnSpacing;
                writer.getWriteFunctions().writeSetY(rowYPos);
                Line[] lines = c[i].getLines();
                for (int j = 0; j < lines.length; j++) {
                    writeLine(lines[j], columnPositions[i], c[i].getAlignment(), width);
                }
            }
        }
    }

    /**
	 * Writes the line
	 * 
	 * @exception IOException
	 * @param l
	 *            the line to write
	 * @param a
	 *            the alignment
	 * @param width
	 *            the width of the cell
	 * @param x
	 *            the x position of the current cell
	 */
    private void writeLine(Line l, double x, Alignment a, double width) throws IOException {
        writer.getWriteFunctions().writeAdjustY(l.getHeight());
        if (a == Alignment.LEFT) {
            writer.getWriteFunctions().writeSetX(x);
        } else if (a == Alignment.RIGHT) {
            writer.getWriteFunctions().writeSetX(x + width - l.getLength());
        } else if (a == Alignment.CENTRE) {
            writer.getWriteFunctions().writeSetX(x + (width - l.getLength()) / 2.0);
        }
        writer.writeLine(l);
    }

    /**
	 * Writes out the table headings
	 * 
	 * @exception IOException
	 */
    private void writeHeadings() throws IOException {
        boolean borderDrawn = false;
        double headingBorderWidth = columnBorder != 0 ? tableWidth + 1 : tableWidth;
        tableXPos = writer.getXPos();
        tableYPos = writer.getYPos();
        BaseFont f = writer.getFont();
        if (headingBackground != Colour.DEFAULT_BACKGROUND && headingCells != null) {
            writer.getWriteFunctions().writeFillBox(0, tableYPos, headingBorderWidth, headingRowHeight + rowSpacing + headingDescent, headingBackground);
        }
        if (headingCells == null) {
            return;
        }
        if (tableBorder != 0) {
            borderDrawn = true;
        }
        if (topHeadingBorder != 0 && !borderDrawn) {
            writer.getWriteFunctions().writeDrawLine(0, tableYPos, headingBorderWidth, tableYPos, topHeadingBorder);
            borderDrawn = true;
        }
        if (rowBorder != 0 && !borderDrawn) {
            writer.getWriteFunctions().writeDrawLine(0, tableYPos, headingBorderWidth, tableYPos, rowBorder);
            borderDrawn = true;
        }
        writeHeadingCells(headingCells, tableYPos);
        double newYPos = tableYPos + headingRowHeight + headingDescent + rowSpacing;
        writer.getWriteFunctions().writeMoveTo(0, newYPos);
        borderDrawn = false;
        if (bottomHeadingBorder != 0) {
            writer.getWriteFunctions().writeDrawLine(0, newYPos, headingBorderWidth, newYPos, bottomHeadingBorder);
            writer.getWriteFunctions().writeSetY(newYPos + bottomHeadingBorder);
            borderDrawn = true;
        }
        if (rowBorder != 0 && !borderDrawn) {
            writer.getWriteFunctions().writeDrawLine(0, newYPos, headingBorderWidth, newYPos, rowBorder);
            writer.getWriteFunctions().writeSetY(newYPos + rowBorder);
            borderDrawn = true;
        }
        writer.baseSetFont(f);
    }

    /**
	 * Draws a horizontal line across the entire width of the table
	 * 
	 * @exception IOException
	 */
    public void drawLine() throws IOException {
        drawLine(Constants.DEFAULT_LINE_WIDTH);
    }

    /**
	 * Draws a horizontal line across the width of the table
	 * 
	 * @exception IOException
	 * @param ps
	 *            the width of the line to be drawn, in points
	 */
    public void drawLine(int ps) throws IOException {
        if (!initialized) {
            init();
            writeHeadings();
        }
        if (rowPending) {
            writeRow();
        }
        double y = writer.getYPos() + 2;
        writer.getWriteFunctions().writeDrawLine(0, y, tableWidth, y, ps);
        writer.getWriteFunctions().writeSetY(writer.getYPos() + ps + 2);
    }

    /**
	 * Increases the y position by the specified number of cms Used for small
	 * gaps eg. between a row and an underlining Any pending row is ejected
	 * first
	 * 
	 * @exception IOException
	 * @param amount
	 *            the amount of vertical space in cms
	 */
    public void space(double amount) throws IOException {
        if (!initialized) {
            init();
            writeHeadings();
        }
        if (rowPending) {
            writeRow();
        }
        writer.getWriteFunctions().writeAdjustY((int) (amount * Constants.POINTS_PER_CM));
    }

    /**
	 * The border to be drawn after each row is written. This is equivalent to a
	 * client call to drawline after each row has been written
	 * 
	 * @param ps
	 *            the width of the border
	 * @exception IOException
	 */
    public void setRowBorder(int ps) throws IOException {
        rowBorder = ps;
    }

    /**
	 * The border to be placed around the table heading only. If the row border
	 * has been set, then the values set as a result of this method will be
	 * ignored
	 * 
	 * @param top
	 *            the width of the border at the top of the table headings
	 * @param bottom
	 *            the width of the border at the bottom of the heading
	 * @exception IOException
	 */
    public void setHeaderBorder(int top, int bottom) throws IOException {
        topHeadingBorder = top;
        bottomHeadingBorder = bottom;
    }

    /**
	 * The border to be drawn around the entire table when it is closed. The
	 * border is also drawn at each page break
	 * 
	 * @param ps
	 *            the width of the border
	 * @exception IOException
	 */
    public void setBorder(int ps) {
        tableBorder = ps;
    }

    /**
	 * The border to be drawn in between each column.
	 * 
	 * @param ps
	 *            the width of the border
	 * @exception IOException
	 */
    public void setColumnBorder(int ps) throws IOException {
        columnBorder = ps;
    }

    /**
	 * Draws a border around this table on the current page
	 * 
	 * @param rows
	 *            TRUE if there are rows on this table, FALSE otherwise
	 * @exception IOException
	 */
    private void drawTableBorder(boolean rows) throws IOException {
        writer.getWriteFunctions().writeDrawLine(0, tableYPos, tableWidth + 1, tableYPos, tableBorder);
        double bottomBorderPos = writer.getYPos() + tableBorder + 2;
        if (rowBorder != 0 && rows) {
            bottomBorderPos = bottomBorderPos - rowBorder - 4;
        }
        if (!rows) {
            bottomBorderPos = tableYPos + headingRowHeight + rowSpacing + headingDescent;
        }
        writer.getWriteFunctions().writeDrawLine(0, bottomBorderPos, tableWidth + 1, bottomBorderPos, tableBorder);
        writer.getWriteFunctions().writeDrawLine(0, tableYPos, 0, bottomBorderPos, tableBorder);
        writer.getWriteFunctions().writeDrawLine(tableWidth + 1, tableYPos, tableWidth + 1, bottomBorderPos, tableBorder);
        writer.getWriteFunctions().writeSetY(writer.getYPos() + tableBorder + 2);
    }

    /**
	 * Inserts the column lines into this table
	 * 
	 * @param rows
	 *            TRUE if this table has rows, FALSE otherwise
	 * @exception IOException
	 */
    private void drawColumnBorders(boolean rows) throws IOException {
        int topBorderSize = tableBorder;
        topBorderSize = topBorderSize == 0 ? topHeadingBorder : topBorderSize;
        topBorderSize = topBorderSize == 0 ? rowBorder : topBorderSize;
        double bottomBorderPos = writer.getYPos() + 1;
        double headerBottomBorder = tableYPos + headingRowHeight + rowSpacing + headingDescent;
        if (rowBorder != 0 && rows) {
            bottomBorderPos = bottomBorderPos - rowBorder - 1;
        }
        if (!rows) {
            bottomBorderPos = headerBottomBorder;
        }
        for (int i = 1; i < columnPositions.length; i++) {
            double borderPos = columnPositions[i] - (columnSpacing + columnBorder) / 2;
            double columnTop = tableYPos;
            if (headings != null && headings[i] instanceof SpannedHeading) {
                columnTop = headerBottomBorder;
            }
            writer.getWriteFunctions().writeDrawLine(borderPos, columnTop, borderPos, bottomBorderPos, columnBorder);
        }
        if (tableBorder != 0) {
            return;
        }
        writer.getWriteFunctions().writeDrawLine(0, tableYPos, 0, bottomBorderPos, columnBorder);
        writer.getWriteFunctions().writeDrawLine(tableWidth + 1, tableYPos, tableWidth + 1, bottomBorderPos, columnBorder);
        writer.getWriteFunctions().writeSetY(writer.getYPos() + tableBorder + 2);
    }

    /**
	 * Called when there is a page break in the middle of the table data or when
	 * the table has finally been closed. This method writes out any column
	 * lines and table borders
	 * 
	 * @param rows
	 *            - TRUE if there are rows on this table, FALSE otherwise
	 * @exception IOException
	 */
    private void writePageLines(boolean rows) throws IOException {
        if (columnBorder != 0) {
            drawColumnBorders(rows);
        }
        if (tableBorder != 0) {
            drawTableBorder(rows);
        }
    }

    /**
	 * Indicates whether or not this tables has had any rows added to it
	 * 
	 * @return TRUE if this table is not empty, FALSE if it is
	 */
    public boolean hasRows() {
        return initialized;
    }

    /**
	 * Sets a flag to write the headings, even when the table is empty. Default
	 * value is FALSE
	 * 
	 * @param w
	 *            TRUE to write the headings for an empty table, FALSE otherwise
	 */
    public void setWriteHeadingsIfEmpty(boolean write) {
        writeHeadingsIfEmpty = write;
    }
}
