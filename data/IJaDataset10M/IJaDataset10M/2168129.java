package org.ujac.print.tag;

import org.ujac.print.PdfCellContainer;
import org.ujac.print.DocumentHandlerException;
import com.lowagie.text.pdf.PdfPCell;

/**
 * Name: PdfCellTag<br>
 * Description: A class handling 'cell' items.<br>
 * 
 * @author lauerc
 */
public class PdfCellTag extends BaseCellTag {

    /** The item's name. */
    public static final String TAG_NAME = "cell";

    /** The cell container. */
    private PdfCellContainer cellContainer = null;

    /**
   * Constructs a PdfCellTag instance with no specific attributes.
   */
    public PdfCellTag() {
        super(TAG_NAME);
    }

    /**
   * Gets a brief description for the item.
   * @return The item's description.
   */
    public String getDescription() {
        return "Adds a cell to its surrounding table or surrounding cell.";
    }

    /**
   * Initializes the item. 
   * @exception DocumentHandlerException If something went badly wrong.
   */
    public void initialize() throws DocumentHandlerException {
        super.initialize();
        this.cellContainer = documentHandler.latestPdfCellContainer();
    }

    /**
   * Opens the item.
   * @exception DocumentHandlerException Thrown in case something went wrong while opening the document item.
   */
    public void openItem() throws DocumentHandlerException {
        super.openItem();
        if (!isValid()) {
            return;
        }
        if (fillRow) {
            this.colspan = this.cellContainer.getRemainingPdfColumns();
        }
    }

    /**
   * Gets the parent attributes.
   * @return The parent attributes.
   */
    protected CellAttributes getParentAttributes() {
        return cellContainer.getCellAttributes();
    }

    /**
   * Closes the item.
   * @exception DocumentHandlerException Thrown in case something went wrong while closing the cell tag.
   */
    public void closeItem() throws DocumentHandlerException {
        if (!isValid()) {
            return;
        }
        super.closeItem();
        itemClosed = true;
        PdfPCell cell = getCell();
        if (cell == null) {
            cell = buildCell();
        }
        cellContainer.addCell(this, cell);
    }
}
