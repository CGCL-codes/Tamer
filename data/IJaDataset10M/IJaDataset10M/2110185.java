package objectivehtml.htmlwidget;

import objectivehtml.htmlwidget.exception.*;
import java.util.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.io.*;

/**
 * <p>This class represents a html table row.
 * i.e. <pre>&lt;tr ...&gt;</pre>
 *
 * <p>This object can only exist inside a HtmlTable object.
 *
 * @author	Keith Wong
 * @see		HtmlTable
 */
public class HtmlTableRow extends HtmlContainerWidget {

    /**
	 * Constructs an instance of HtmlTableRow with the nColumns columns.
	 * The only valid parent is a HtmlTableRow.
	 * @param	objParent		the parent for this widget
	 * @param	nColumns		the columns to table row is initially created with
	 * @throws	InvalidParentWidgetException	if the specified parent widget is not valid
	 * @throws	InvalidChildWidgetException		if the parent widget does not accept this widget as a child
	 */
    protected HtmlTableRow(HtmlTable objParent, int nColumns) throws InvalidParentWidgetException, InvalidChildWidgetException {
        super(objParent);
        m_sStartTag = "tr";
        m_sEndTag = "tr";
        for (int i = 0; i < nColumns; i++) {
            appendTableCell();
        }
    }

    /**
	 * Adds a new table cell to this table row.
	 */
    protected void appendTableCell() {
        try {
            new HtmlTableCell(this);
        } catch (InvalidParentWidgetException ipwe) {
        } catch (InvalidChildWidgetException ipwe) {
        }
    }

    /**
	 * Returns the HtmlTableCell object for the specified table cell.
	 * @param	nCol the col index
	 * @return	the HtmlTableCell object at the specified col index nCol
	 */
    public HtmlTableCell getTableCell(int nCol) {
        return (HtmlTableCell) getChildWidget(nCol);
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
	 * Suitable values are "left", "center", "right", "justify" and "char".
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
	 * Returns the vertical alignment style of this object.
	 *
	 * @return	the vertical alignment style of this object
	 * @see		#setVAlign(String)
	 */
    public String getVAlign() {
        return getAttribute("valign");
    }

    /**
	 * <p>Sets the vertical alignment style of this object.
	 * Suitable values are "top", "middle", "bottom" and "baseline".
	 *
	 * <p>The html attribute it sets is "valign".
	 *
	 * <p>If null is specified the attribute is cleared.
	 * @param	sVAlign			the vertical alignment style for this widget,
	 *							if null is specified then this attribute
	 *							will be cleared
	 * @see		#getVAlign()
	 */
    public void setVAlign(String sVAlign) {
        super.setAttribute("valign", sVAlign);
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
	 * Overrides the default version, so that only HtmlTableCell objects can be
	 * added to this object.
	 * @param	objHtmlWidget	the widget to test
	 * @throws	InvalidChildWidgetException if the widget is not a HtmlTableCell object
	 */
    protected void isValidChildWidget(HtmlWidget objHtmlWidget) throws InvalidChildWidgetException {
        if (!(objHtmlWidget instanceof HtmlTableCell)) {
            throw new InvalidChildWidgetException("The only valid child widget of HtmlTableRow is a HtmlTableCell object.");
        }
    }
}
