package com.itextpdf.tool.xml.html.pdfelement;

import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.tool.xml.html.table.TableStyleValues;

/**
 * HtmlCell is created to provide more parameters to better mimic the behavior of table cell in HTML.
 *
 * @author Emiel Ackermann
 *
 */
public class HtmlCell extends PdfPCell {

    private float fixedWidth;

    private TableStyleValues values = new TableStyleValues();

    /**
	 * Default constructor for a HTMLCell with no fixedWidth.
	 */
    public HtmlCell() {
        super();
        fixedWidth = 0;
        setPaddingLeft(0);
        setPaddingRight(0);
        setPaddingTop(0);
        setPaddingBottom(0);
        setUseAscender(true);
        setUseDescender(true);
    }

    /**
	 * Constructor used for replacing a PdfPCell with a HtmlCell and setting its last in row boolean.
	 * @param pdfPCell {@link PdfPCell}.
	 * @param b boolean sets the {@link TableStyleValues#setLastInRow(boolean)} method.
	 */
    public HtmlCell(final PdfPCell pdfPCell, final boolean b) {
        this(pdfPCell);
        values.setLastInRow(b);
    }

    /**
	 * Constructor used for replacing a PdfPCell with a HtmlCell.
	 * @param pdfPCell {@link PdfPCell}.
	 */
    public HtmlCell(final PdfPCell pdfPCell) {
        super(pdfPCell);
    }

    /**
	 * Sets the fixed width of the HtmlCell.
	 * @param fixedWidth the fixed cell width
	 */
    public void setFixedWidth(final float fixedWidth) {
        this.fixedWidth = fixedWidth;
    }

    /**
	 * Gets the fixed width of the HtmlCell.
	 * @return the fixed Width value
	 */
    public float getFixedWidth() {
        return fixedWidth;
    }

    /**
	 * Gets the {@link TableStyleValues} of the HtmlCell.
	 * @return TableStyleValues
	 */
    public TableStyleValues getCellValues() {
        return values;
    }

    /**
	 * Sets the {@link TableStyleValues} of the HtmlCell.
	 * @param values the TableStyleValues
	 */
    public void setCellValues(final TableStyleValues values) {
        this.values = values;
    }
}
