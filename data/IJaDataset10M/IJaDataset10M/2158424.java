package org.openXpertya.print.pdf.text.rtf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.openXpertya.print.pdf.text.DocumentException;
import org.openXpertya.print.pdf.text.Element;
import org.openXpertya.print.pdf.text.Row;
import org.openXpertya.print.pdf.text.Table;

/**
 * A Helper Class for the <CODE>RtfWriter</CODE>.
 * <P>
 * Do not use it directly, except if you want to write a <CODE>DocumentListener</CODE> for Rtf
 * 
 * ONLY FOR USE WITH THE RtfWriter NOT with the RtfWriter2.
 *
 * Parts of this Class were contributed by Steffen Stundzig. Many thanks for the
 * improvements.
 * Updates Benoit WIART <b.wiart@proxiad.com>
 */
public class RtfTable {

    /** Stores the different rows. */
    private ArrayList rowsList = new ArrayList();

    /** Stores the RtfWriter, which created this RtfTable. */
    private RtfWriter writer = null;

    /** Stores the Table, which this RtfTable is based on. */
    private Table origTable = null;

    /**
     * Create a new <code>RtfTable</code>.
     *
     * @param writer The <code>RtfWriter</code> that created this Table
     */
    public RtfTable(RtfWriter writer) {
        super();
        this.writer = writer;
    }

    /**
     * Import a <CODE>Table</CODE> into the <CODE>RtfTable</CODE>.
     * <P>
     * @param table A <code>Table</code> specifying the <code>Table</code> to be imported
     * @param pageWidth An <code>int</code> specifying the page width
     * @return true if importing the table succeeded
     */
    public boolean importTable(Table table, int pageWidth) {
        origTable = table;
        Iterator rows = table.iterator();
        Row row = null;
        int tableWidth = (int) table.widthPercentage();
        int cellpadding = (int) (table.cellpadding() * RtfWriter.TWIPSFACTOR);
        int cellspacing = (int) (table.cellspacing() * RtfWriter.TWIPSFACTOR);
        float[] propWidths = table.getProportionalWidths();
        int borders = table.border();
        java.awt.Color borderColor = table.borderColor();
        float borderWidth = table.borderWidth();
        for (int i = 0; i < table.size(); i++) {
            RtfRow rtfRow = new RtfRow(writer, this);
            rtfRow.pregenerateRows(table.columns());
            rowsList.add(rtfRow);
        }
        int i = 0;
        while (rows.hasNext()) {
            row = (Row) rows.next();
            row.setHorizontalAlignment(table.alignment());
            RtfRow rtfRow = (RtfRow) rowsList.get(i);
            rtfRow.importRow(row, propWidths, tableWidth, pageWidth, cellpadding, cellspacing, borders, borderColor, borderWidth, i);
            i++;
        }
        return true;
    }

    /**
     * Output the content of the <CODE>RtfTable</CODE> to an OutputStream.
     *
     * @param os The <code>OutputStream</code> that the content of the <code>RtfTable</code> is to be written to
     * @return true if writing the table succeeded
     * @throws DocumentException
     * @throws IOException
     */
    public boolean writeTable(ByteArrayOutputStream os) throws DocumentException, IOException {
        if (!this.writer.writingHeaderFooter()) {
            os.write(RtfWriter.escape);
            os.write(RtfWriter.paragraph);
        }
        int size = rowsList.size();
        for (int i = 0; i < size; i++) {
            RtfRow row = (RtfRow) rowsList.get(i);
            row.writeRow(os, i, origTable);
            os.write((byte) '\n');
        }
        if (!writer.writingHeaderFooter()) {
            os.write(RtfWriter.escape);
            os.write(RtfWriter.paragraphDefaults);
            os.write(RtfWriter.escape);
            os.write(RtfWriter.paragraph);
            switch(origTable.alignment()) {
                case Element.ALIGN_LEFT:
                    os.write(RtfWriter.escape);
                    os.write(RtfWriter.alignLeft);
                    break;
                case Element.ALIGN_RIGHT:
                    os.write(RtfWriter.escape);
                    os.write(RtfWriter.alignRight);
                    break;
                case Element.ALIGN_CENTER:
                    os.write(RtfWriter.escape);
                    os.write(RtfWriter.alignCenter);
                    break;
                case Element.ALIGN_JUSTIFIED:
                case Element.ALIGN_JUSTIFIED_ALL:
                    os.write(RtfWriter.escape);
                    os.write(RtfWriter.alignJustify);
                    break;
            }
        }
        return true;
    }

    /**
     * <code>RtfCell</code>s call this method to specify that a certain other cell is to be merged with it.
     *
     * @param x The column position of the cell to be merged
     * @param y The row position of the cell to be merged
     * @param mergeType The merge type specifies the kind of merge to be applied (MERGE_HORIZ_PREV, MERGE_VERT_PREV, MERGE_BOTH_PREV)
     * @param mergeCell The <code>RtfCell</code> that the cell at x and y is to be merged with
     */
    public void setMerge(int x, int y, int mergeType, RtfCell mergeCell) {
        RtfRow row = (RtfRow) rowsList.get(y);
        row.setMerge(x, mergeType, mergeCell);
    }

    /**
     * This method allows access to the original Table that led to this RtfTable.
     *
     * @return The Table object that is the basis of this RtfTable.
     */
    protected Table getOriginalTable() {
        return origTable;
    }
}
