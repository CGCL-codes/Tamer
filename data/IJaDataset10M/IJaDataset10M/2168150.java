package objectivehtml.htmlwidget;

import objectivehtml.htmlwidget.exception.*;
import java.util.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import objectivehtml.utils.*;
import java.io.*;

/**
 * <p>This class represents a html table.
 * i.e.
 * <pre>
 *	&lt;table&gt;
 *		&lt;tr&gt;&lt;td&gt;&lt;/td&gt;&lt;/tr&gt;
 *		&lt;tr&gt;&lt;td&gt;&lt;/td&gt;&lt;/tr&gt;
 *	&lt;/table&gt;
 * </pre>
 *
 * <p>This object allows users to place text and other widgets into
 * a tabular format. The table is always rectangular.
 *
 * <p>A table is made up of HtmlTableRow objects and HtmlTableCell objects. Each
 * HtmlTableRow object represents one row of the table and each HtmlTableCell object
 * represents one cell of the table. HtmlTableCell objects have the ability the span
 * either vertically or horizontally over other table cells.
 *
 * <p>Table rows and table columns
 * can be shown/hidden using the methods setRowVisible(int,boolean) and setColumnVisible(int,boolean).
 *
 * <p>The current implementation of HtmlTable only supports the simple table model. The complex
 * table model which was introduced in HTML 4.0 is not supported. The main reasons for this
 * is because most developers use the simple model and everything that can be
 * done with the complex version can be emulated with the simple version.
 *
 * @author Keith Wong
 */
public class HtmlTable extends HtmlContainerWidget {

    private int m_nRows;

    private int m_nColumns;

    private ArrayList m_alVisibleRows = new ArrayList();

    private ArrayList m_alVisibleColumns = new ArrayList();

    private HtmlTableCaption m_objHtmlTableCaption = null;

    /**
	 * Constructs an instance of HtmlTable with 1 row and 1 column.
	 * @param	objParent	the parent object of this table
	 * @throws	InvalidParentWidgetException	if the specified parent widget is not valid
	 * @throws	InvalidChildWidgetException		if the parent widget does not accept this widget as a child
	 */
    public HtmlTable(HtmlContainerWidget objParent) throws InvalidParentWidgetException, InvalidChildWidgetException {
        this(objParent, 1, 1);
    }

    /**
	 * Constructs an instance of HtmlTable with nRows rows and nColumns columns.
	 * @param	objParent	the parent object of this table
	 * @param	nRows		the number of rows the table starts with
	 * @param	nColumns	the number of columns the table starts with
	 * @throws	InvalidParentWidgetException	if the specified parent widget is not valid
	 * @throws	InvalidChildWidgetException		if the parent widget does not accept this widget as a child
	 */
    public HtmlTable(HtmlContainerWidget objParent, int nRows, int nColumns) throws InvalidParentWidgetException, InvalidChildWidgetException {
        super(objParent);
        m_sStartTag = "table";
        m_sEndTag = "table";
        try {
            m_objHtmlTableCaption = new HtmlTableCaption(this);
        } catch (InvalidParentElementException ipe) {
        }
        if (nRows <= 0) nRows = 1;
        if (nColumns <= 0) nColumns = 1;
        m_nRows = 0;
        m_nColumns = 0;
        setColumns(nColumns);
        setRows(nRows);
    }

    /**
	 * Returns a reference to the html table caption object.
	 * @return	a reference to the table caption object
	 * @see		#setCaptionText(String)
	 */
    public HtmlTableCaption getHtmlTableCaption() {
        return m_objHtmlTableCaption;
    }

    /**
	 * Sets the text for the caption of the table. To clear the
	 * text specify a null reference. When no caption is set (null) then the
	 * caption tag will not be drawn by the printHtml() methods.
	 * @param	sCaptionText	the caption text
	 * @see		#getCaptionText()
	 * @see		#getHtmlTableCaption()
	 */
    public void setCaptionText(String sCaptionText) {
        m_objHtmlTableCaption.setCaptionText(sCaptionText);
    }

    /**
	 * Returns the caption text for this table object. If no caption is
	 * set then null is returned.
	 * @return	the caption text
	 * @see		#setCaptionText(String)
	 * @see		#getHtmlTableCaption()
	 */
    public String getCaptionText() {
        return m_objHtmlTableCaption.getCaptionText();
    }

    /**
	 * Sets the visibility of this object.
	 * Overrides protected method to be public.
	 * @param	bVisible	true if visible, false if invisible
	 */
    public void setVisible(boolean bVisible) {
        super.setVisible(bVisible);
    }

    /**
	 * Returns the alignment style of this object.
	 *
	 * @return	the alignment style of this object
	 * @see		#setAlign(String)
	 */
    public String getAlign() {
        return getAttribute("align");
    }

    /**
	 * <p>Sets the alignment style of this object.
	 * Suitable values are "left", "center" and "right".
	 *
	 * <p>The html attribute it sets is "align".
	 *
	 * <p>If null is specified the attribute is cleared.
	 * @param	sAlign			the alignment style for this widget,
	 *							if null is specified then this attribute
	 *							will be cleared
	 * @see		#getAlign()
	 */
    public void setAlign(String sAlign) {
        super.setAttribute("align", sAlign);
    }

    /**
	 * Returns the background color of this object.
	 *
	 * @return	the background color of this object
	 * @see		#setBgColor(String)
	 */
    public String getBgColor() {
        return getAttribute("bgcolor");
    }

    /**
	 * <p>Sets the background color of this object.
	 * There 16 preset values "black", "silver", "gray", "white", "maroon", "red",
	 * "purple", "fushcia", "green", "lime", "olive", "yellow", "navy", "blue", "teal"
	 * and "navy". For all other colors you can specify the color in the format SRGB,
	 * i.e. #RRGGBB.
	 *
	 * <p>The html attribute it sets is "bgcolor".
	 *
	 * <p>If null is specified the attribute is cleared.
	 * @param	sBgColor		the background color for this widget,
	 *							if null is specified then this attribute
	 *							will be cleared
	 * @see		#getBgColor()
	 */
    public void setBgColor(String sBgColor) {
        super.setAttribute("bgcolor", sBgColor);
    }

    /**
	 * Returns the width of this object.
	 *
	 * @return	the width of this object
	 * @see		#setWidth(String)
	 */
    public String getWidth() {
        return getAttribute("width");
    }

    /**
	 * <p>Sets the width of this object.
	 * There are 2 formats that can be used. The first is to give a precise
	 * pixel value, e.g. "400", meaning the object will attempt to be 400 pixels wide.
	 * The second is to give a percentage, this means the object will attempt
	 * to occupy that percentage of the available space to it. e.g. "80%", meaning the
	 * object will attempt to occupy 80 percent of the width space that is available to it.
	 *
	 * <p>The html attribute it sets is "width".
	 *
	 * <p>If null is specified the attribute is cleared.
	 * @param	sWidth		the width size for this widget,
	 *							if null is specified then this attribute
	 *							will be cleared
	 * @see		#getWidth()
	 */
    public void setWidth(String sWidth) {
        super.setAttribute("width", sWidth);
    }

    /**
	 * Returns the border of this object.
	 *
	 * @return	the border of this object
	 * @see		#setBorder(String)
	 */
    public String getBorder() {
        return getAttribute("border");
    }

    /**
	 * <p>Sets the border of this table.
	 * The border size is specified in pixels.
	 *
	 * <p>The html attribute it sets is "border".
	 *
	 * <p>If null is specified the attribute is cleared.
	 * @param	sBorder		the border size for this widget,
	 *							if null is specified then this attribute
	 *							will be cleared
	 * @see		#getBorder()
	 */
    public void setBorder(String sBorder) {
        super.setAttribute("border", sBorder);
    }

    /**
	 * Returns the cell padding of this object.
	 *
	 * @return	the cell padding of this object
	 * @see		#setCellPadding(String)
	 */
    public String getCellPadding() {
        return getAttribute("cellpadding");
    }

    /**
	 * <p>Sets the cell padding of this table.
	 * The cell padding size is specified in pixels.
	 * Cell padding is the space between the outermost widgets inside a
	 * table cell and the border of the table cell.
	 *
	 * <p>The html attribute it sets is "cellpadding".
	 *
	 * <p>If null is specified the attribute is cleared.
	 * @param	sCellPadding		the cell padding size for this widget,
	 *							if null is specified then this attribute
	 *							will be cleared
	 * @see		#getCellPadding()
	 */
    public void setCellPadding(String sCellPadding) {
        super.setAttribute("cellpadding", sCellPadding);
    }

    /**
	 * Returns the cell spacing of this object.
	 *
	 * @return	the cell spacing of this object
	 * @see		#setCellSpacing(String)
	 */
    public String getCellSpacing() {
        return getAttribute("cellspacing");
    }

    /**
	 * <p>Sets the cell spacing of this table.
	 * The cell spacing size is specified in pixels.
	 * Cell spacing is the space between the table cells.
	 *
	 * <p>The html attribute it sets is "cellspacing".
	 *
	 * <p>If null is specified the attribute is cleared.
	 * @param	sCellSpacing		the cell spacing size for this widget,
	 *							if null is specified then this attribute
	 *							will be cleared
	 * @see		#getCellSpacing()
	 */
    public void setCellSpacing(String sCellSpacing) {
        super.setAttribute("cellspacing", sCellSpacing);
    }

    /**
	 * <p>Adds a new row to this table. If bCloneLastRow is specified to be
	 * true then the new row will have all the properties of the last row, otherwise
	 * the new row will have no default properties.
	 *
	 * @param	bCloneLastRow	indicates whether the new row will clone the properties of the last row
	 * @return	the HtmlTableRow created
	 */
    public HtmlTableRow appendTableRow(boolean bCloneLastRow) {
        HtmlTableRow objNewTableRow;
        int nNewRowIdx = getRows();
        setRows(nNewRowIdx + 1);
        objNewTableRow = getTableRow(nNewRowIdx);
        if (bCloneLastRow) {
            HtmlTableRow objLastTableRow = getTableRow(nNewRowIdx - 1);
            objNewTableRow.m_hmAttributes = objLastTableRow.getAttributes();
            for (int i = 0; i < objLastTableRow.getChildrenCount(); i++) {
                HtmlTableCell objTableCell1 = objLastTableRow.getTableCell(i);
                HtmlTableCell objTableCell2 = objNewTableRow.getTableCell(i);
                objTableCell2.m_hmAttributes = objTableCell1.getAttributes();
            }
        }
        return objNewTableRow;
    }

    /**
	 * <p>Sets the number of table rows.
	 * <p>If nTableRows is greater than the
	 * current number of rows then new table rows and table cells are created
	 * and appended to the end.
	 * <p>If nTableRows is less than the current number of rows then the table
	 * rows and table cells (starting from the end) are deleted. All widgets
	 * contained in the table cells will also be destroyed.
	 * @param	nTableRows	the new number of table rows
	 * @see		#getRows()
	 * @see		#setColumns(int)
	 */
    public void setRows(int nTableRows) {
        if (m_nRows < nTableRows) {
            for (int nRowsAdded = 0; nRowsAdded < (nTableRows - m_nRows); nRowsAdded++) {
                try {
                    new HtmlTableRow(this, m_nColumns);
                } catch (InvalidParentWidgetException ipwe) {
                } catch (InvalidChildWidgetException icwe) {
                }
                m_alVisibleRows.add(new Boolean(true));
            }
        } else if (m_nRows > nTableRows) {
            while (getChildrenCount() > nTableRows) {
                ((HtmlWidget) getChildWidget(nTableRows)).destroy();
                m_alVisibleRows.remove(nTableRows);
            }
        }
        m_nRows = nTableRows;
        updateVisibility();
    }

    /**
	 * Returns the number of table rows.
	 * @return	the number of the table rows
	 * @see		#setRows(int)
	 */
    public int getRows() {
        return m_nRows;
    }

    /**
	 * <p>Sets the number of table columns.
	 * <p>If nTableColumns is greater than the
	 * current number of columns then new table cells are created
	 * and appended to the end.
	 * <p>If nTableColumns is less than the current number of columns then the
	 * table cells (starting from the end) are deleted. All widgets
	 * contained in the table cells will also be destroyed.
	 * @param	nTableColumns	the new number of table columns
	 * @see		#getColumns()
	 * @see		#setRows(int)
	 */
    public void setColumns(int nTableColumns) {
        if (m_nColumns < nTableColumns) {
            for (Iterator itTableRows = getChildrenWidgets().iterator(); itTableRows.hasNext(); ) {
                HtmlTableRow objTableRow = (HtmlTableRow) itTableRows.next();
                for (int nColumnsAdded = 0; nColumnsAdded < (nTableColumns - m_nColumns); nColumnsAdded++) {
                    objTableRow.appendTableCell();
                }
            }
            for (int nColumnsAdded = 0; nColumnsAdded < (nTableColumns - m_nColumns); nColumnsAdded++) {
                m_alVisibleColumns.add(new Boolean(true));
            }
        } else if (m_nColumns > nTableColumns) {
            for (Iterator itTableRows = getChildrenWidgets().iterator(); itTableRows.hasNext(); ) {
                HtmlTableRow objTableRow = (HtmlTableRow) itTableRows.next();
                while (objTableRow.getChildrenCount() > nTableColumns) {
                    ((HtmlWidget) objTableRow.getChildWidget(nTableColumns)).destroy();
                }
            }
            for (int nColumnsRemoved = 0; nColumnsRemoved < (m_nColumns - nTableColumns); nColumnsRemoved++) {
                m_alVisibleColumns.remove(m_nColumns - nColumnsRemoved - 1);
            }
        }
        m_nColumns = nTableColumns;
        updateVisibility();
    }

    /**
	 * Returns the number of table columns.
	 * @return	the number of the table columns
	 * @see		#setColumns(int)
	 */
    public int getColumns() {
        return m_nColumns;
    }

    /**
	 * Returns the HtmlTableRow object for the specified table row.
	 * @param	nRow the row index
	 * @return	the HtmlTableRow object at the specified row index nRow
	 */
    public HtmlTableRow getTableRow(int nRow) {
        return (HtmlTableRow) getChildWidget(nRow);
    }

    /**
	 * Returns the HtmlTableCell object for the specified table cell.
	 * @param	the row index
	 * @param	the column index
	 * @return	the HtmlTableCell object at the specified position (nRow, nColumn)
	 */
    public HtmlTableCell getTableCell(int nRow, int nColumn) {
        return (HtmlTableCell) (((HtmlTableRow) getChildWidget(nRow)).getChildWidget(nColumn));
    }

    /**
	 * Sets a table row to be either hidden or visible. If the row is set to
	 * invisible then all the objects contained in the HtmlTableRow object will also
	 * become invisible.
	 *
	 * @param	nRow	the row index
	 * @param	bVisible	true if the row is to visible, false otherwise
	 * @see		#isRowVisible(int)
	 * @see		#setColumnVisible(int, boolean)
	 * @see		#updateVisibility()
	 */
    public void setRowVisible(int nRow, boolean bVisible) {
        m_alVisibleRows.set(nRow, new Boolean(bVisible));
        updateVisibility();
    }

    /**
	 * Returns if a table row is visible or hidden.
	 * @return	true if visible, false otherwise
	 * @see		#setRowVisible(int, boolean)
	 */
    public boolean isRowVisible(int nRow) {
        return ((Boolean) m_alVisibleRows.get(nRow)).booleanValue();
    }

    /**
	 * Sets a column to be either hidden or visible. If the column is set to
	 * invisible then all the objects contained in the associated HtmlTableCell objects will also
	 * become invisible.
	 *
	 * @param	nColumn		the column index
	 * @param	bVisible	true if the column is to visible, false otherwise
	 * @see		#isColumnVisible(int)
	 * @see		#setRowVisible(int, boolean)
	 * @see		#updateVisibility()
	 */
    public void setColumnVisible(int nColumn, boolean bVisible) {
        m_alVisibleColumns.set(nColumn, new Boolean(bVisible));
        updateVisibility();
    }

    /**
	 * Returns if a table column is visible or hidden.
	 * @return	true if visible, false otherwise
	 * @see		#setColumnVisible(int, boolean)
	 */
    public boolean isColumnVisible(int nColumn) {
        return ((Boolean) m_alVisibleColumns.get(nColumn)).booleanValue();
    }

    /**
	 * <p>Swaps the position of the 2 table rows.
	 * Moves the table row at position nFirstRow to nSecondRow,
	 * and moves the table row at position nSecondRow to nFirstRow.
	 *
	 * <p>Overrides the default implementation. When rows are swapped
	 * the visibility is applied to the rows accordingly.
	 * @param	nFirstRow	the first row
	 * @param	nSecondRow	the second row
	 * @see		#updateVisibility()
	 */
    public void swapHtmlWidgets(int nFirstRow, int nSecondRow) {
        super.swapHtmlWidgets(nFirstRow, nSecondRow);
        updateVisibility();
    }

    /**
	 * Updates the visible flag of all the widgets in the table. All
	 * hide/show methods will call this method to
	 * update the visibility of the affected widgets.
	 */
    protected void updateVisibility() {
        if (isVisible()) {
            boolean[][] aabTableCellsDone = new boolean[m_nRows][m_nColumns];
            int nRow = 0;
            int nRowPtr = 0;
            int nColumn = 0;
            int nColumnPtr = 0;
            for (int i = 0; i < aabTableCellsDone.length; i++) {
                for (int j = 0; j < aabTableCellsDone[i].length; j++) {
                    aabTableCellsDone[i][j] = false;
                }
            }
            for (Iterator itTableRows = getChildrenWidgets().iterator(); itTableRows.hasNext(); ) {
                HtmlTableRow objTableRow = (HtmlTableRow) itTableRows.next();
                if (isRowVisible(nRow)) {
                    objTableRow.setVisible(true);
                    nColumn = 0;
                    nColumnPtr = 0;
                    for (Iterator itTableCells = objTableRow.getChildrenWidgets().iterator(); itTableCells.hasNext(); ) {
                        HtmlTableCell objTableCell = (HtmlTableCell) itTableCells.next();
                        if (isColumnVisible(nColumn) && aabTableCellsDone[nRowPtr][nColumnPtr] == false) {
                            objTableCell.setVisible(true);
                            for (int i = nRowPtr; i < (nRowPtr + objTableCell.getRowSpan()) && i < m_nRows; i++) {
                                for (int j = nColumnPtr; j < (nColumnPtr + objTableCell.getColSpan()) && j < m_nColumns; j++) {
                                    aabTableCellsDone[i][j] = true;
                                }
                            }
                            nColumnPtr++;
                        } else {
                            objTableCell.setVisible(false);
                            if (isColumnVisible(nColumn) && aabTableCellsDone[nRowPtr][nColumnPtr]) nColumnPtr++;
                        }
                        nColumn++;
                    }
                    nRowPtr++;
                } else {
                    objTableRow.setVisible(false);
                }
                nRow++;
            }
        }
    }

    /**
	 * Overrides the default version, so that only HtmlTableRow objects can be
	 * added to this object.
	 * @param	objHtmlWidget	the widget to test
	 * @throws	InvalidChildWidgetException if the widget is not a HtmlTableRow object
	 */
    protected void isValidChildWidget(HtmlWidget objHtmlWidget) throws InvalidChildWidgetException {
        if (!(objHtmlWidget instanceof HtmlTableRow)) {
            throw new InvalidChildWidgetException("The only valid child widget of HtmlTable is a HtmlTableRow object.");
        }
    }
}
