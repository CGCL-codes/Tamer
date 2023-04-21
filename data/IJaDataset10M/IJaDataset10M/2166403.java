package net.sf.excompcel.spreadsheet.impl.poihssf;

import net.sf.excompcel.spreadsheet.ECCell;
import net.sf.excompcel.spreadsheet.ECSheet;
import net.sf.excompcel.spreadsheet.impl.base.ECRowBase;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

/**
 * 
 * @author Detlev Struebig
 * @since v0.8
 *
 */
public class PoiECRow extends ECRowBase<HSSFRow, HSSFCell, HSSFCellStyle, HSSFFont, HSSFRichTextString> {

    /** Logger. */
    @SuppressWarnings("unused")
    private static final Logger log = Logger.getLogger(PoiECRow.class);

    /**
	 * Constructor.
	 * @param row {@link HSSFRow}
	 */
    public PoiECRow(HSSFRow row) {
        super(row);
        if (row != null) {
            setRowIndex(row.getRowNum());
        }
    }

    /**
	 * Constructor.
	 * @param row {@link HSSFRow}
	 * @param rowIndex Row Index
	 */
    public PoiECRow(HSSFRow row, int rowIndex) {
        super(row);
        setRowIndex(rowIndex);
    }

    public ECCell<HSSFCell, HSSFCellStyle, HSSFFont, HSSFRichTextString> getCell(int index) {
        HSSFCell cell = null;
        if (hasOriginalObject()) {
            cell = getTheObject().getCell(index);
        }
        return new PoiECCell(cell);
    }

    public ECCell<HSSFCell, HSSFCellStyle, HSSFFont, HSSFRichTextString> getOrCreateCell(int index) {
        HSSFCell cell = null;
        if (hasOriginalObject()) {
            cell = getTheObject().getCell(index);
            if (cell == null) {
                cell = getTheObject().createCell(index);
            }
        }
        return new PoiECCell(cell);
    }

    public int getRowIndex() {
        if (hasOriginalObject()) return getTheObject().getRowNum(); else return super.getRowIndex();
    }

    public ECCell<HSSFCell, HSSFCellStyle, HSSFFont, HSSFRichTextString> createCell(int index) {
        if (hasOriginalObject()) return new PoiECCell(getTheObject().createCell(index)); else return null;
    }

    public int getLastCellIndex() {
        if (hasOriginalObject()) return getTheObject().getLastCellNum(); else return 0;
    }

    public int getFirstCellIndex() {
        if (hasOriginalObject()) return getTheObject().getFirstCellNum(); else return 0;
    }

    public ECSheet<HSSFSheet, HSSFRow, HSSFCell, HSSFCellStyle, HSSFFont, HSSFRichTextString> getSheet() {
        if (hasOriginalObject()) return new PoiECSheet(getTheObject().getSheet()); else return new PoiECSheet(null);
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("PoiECRow [ Rowindex=").append(getRowIndex()).append(" FirstCellIndex=").append(getFirstCellIndex()).append(" LastCellIndex=").append(getLastCellIndex()).append("]");
        return buf.toString();
    }

    @Override
    public void setRowIndex(int row) {
        if (!hasOriginalObject()) {
            super.setRowIndex(row);
        } else {
            if (row != getRow().getRowNum()) {
                throw new IllegalArgumentException("Can not chnage Row Index of existing Original Object.");
            }
        }
    }
}
