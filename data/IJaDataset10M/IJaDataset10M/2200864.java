package net.sf.excompcel.poi.sheet.comparator;

import net.sf.excompcel.eGridAction;
import net.sf.excompcel.gui.model.MainModel;
import net.sf.excompcel.poi.comparator.compareobject.CompareObjectBase;
import net.sf.excompcel.poi.report.impl.ReportWorkbook;
import net.sf.excompcel.poi.sheet.comparator.compareobject.CellCompareObject;
import net.sf.excompcel.poi.sheet.comparator.compareobject.RowCompareObject;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.util.CellReference;

/**
 * Row Comparator. Compare two Rows. The Comparer contains a reference to an
 * Report Excel Sheet.
 * 
 * @author Detlev Struebig
 * @since v0.1
 * 
 */
public class RowComparator extends ComparatorSheetBase {

    /** Logger. */
    private static Logger log = Logger.getLogger(RowComparator.class);

    /** cell comparator. */
    private CellComparator cellComparator;

    /**
	 * Constructor.
	 * 
	 * @param wbReport The {@link ReportWorkbook} containing the Report.
	 */
    public RowComparator(ReportWorkbook wbReport) {
        super(wbReport);
        cellComparator = new CellComparator(getWbReport());
    }

    /**
	 * Compare Row in Sheet
	 * 
	 * @param master {@link RowCompareObject}
	 * @param slave {@link RowCompareObject}
	 * @return a negative integer, zero, or a positive integer as the first
	 *         argument is less than, equal to, or greater than the second.
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
    public final int compare(final RowCompareObject master, final RowCompareObject slave) {
        int ret = 0;
        int iRow = 0;
        short colMin = 0;
        short colMax = 0;
        if (master.isNull() && slave.isNull()) {
            log.debug("Both Rows are NULL. They are equal.");
            return 0;
        }
        MainModel model = getWbReport().getModel();
        if (master.isNull() && !slave.isNull()) {
            log.debug("Master Row exists. No Slave Row.");
            if (model.getRadioGridAction().equals(eGridAction.RANGE)) {
                log.info("Compare only a Range.");
                colMin = (short) CellReference.convertColStringToIndex(model.getRangeColStart());
                log.debug("Convert min. Column " + model.getRangeColStart() + " to " + colMin);
                colMax = (short) CellReference.convertColStringToIndex(model.getRangeColEnd());
                log.debug("Convert max. Column " + model.getRangeColEnd() + " to " + colMax);
                iRow = master.getRowPosition();
                ret = compareRange(master, slave, colMin, colMax, iRow);
            } else if (model.getRadioGridAction().equals(eGridAction.NONE)) {
                colMin = 0;
                colMax = slave.getRow().getLastCellNum();
                iRow = slave.getRowPosition();
                ret = compareRange(master, slave, colMin, colMax, iRow);
            }
        } else {
            log.debug("Master and Slave Row exists.");
            if (model.getRadioGridAction().equals(eGridAction.RANGE)) {
                log.info("Compare only a Range.");
                colMin = (short) CellReference.convertColStringToIndex(model.getRangeColStart());
                log.debug("Convert min. Column " + model.getRangeColStart() + " to " + colMin);
                colMax = (short) CellReference.convertColStringToIndex(model.getRangeColEnd());
                log.debug("Convert max. Column " + model.getRangeColEnd() + " to " + colMax);
                iRow = master.getRowPosition();
                ret = compareRange(master, slave, colMin, colMax, iRow);
            } else if (model.getRadioGridAction().equals(eGridAction.NONE)) {
                log.info("Compare all.");
                colMin = 0;
                colMax = master.getRow().getLastCellNum();
                iRow = master.getRowPosition();
                ret = compareRange(master, slave, colMin, colMax, iRow);
            }
        }
        if (ret == 0) {
            log.info("Is Equal.");
        }
        return ret;
    }

    /**
	 * Compare Range of Rows in Sheet.
	 * @param master {@link RowCompareObject}
	 * @param slave {@link RowCompareObject}
	 * @param colMin The Column start position.
	 * @param colMax The Column end position.
	 * @param rowCurrent The Row number to compare.
	 * @return
	 */
    private final int compareRange(final RowCompareObject master, final RowCompareObject slave, int colMin, int colMax, int rowCurrent) {
        log.info("Compare only a Range.");
        if (log.isDebugEnabled()) {
            StringBuffer bufLog = new StringBuffer();
            log.debug(bufLog.append("RowNum=").append(rowCurrent).append("  FirstCellNum=").append(colMin).append(" LastCellNum=").append(colMax));
        }
        int ret = 0;
        for (int iCol = colMin; iCol <= colMax; iCol++) {
            HSSFCell cellMaster = null;
            if (!master.isNull()) {
                cellMaster = master.getRow().getCell(iCol);
            }
            HSSFCell cellSlave = null;
            if (!slave.isNull()) {
                cellSlave = slave.getRow().getCell(iCol);
            }
            log.debug("Compare column " + iCol);
            int iCompare = cellComparator.compare(new CellCompareObject(cellMaster, rowCurrent, iCol), new CellCompareObject(cellSlave, rowCurrent, iCol));
            if (cellComparator.isDifferent(iCompare)) {
                ret = iCompare;
            } else {
                log.info("Cell Is Equal");
            }
        }
        return ret;
    }

    public int compare(CompareObjectBase master, CompareObjectBase slave) {
        return compare(master, slave);
    }
}
