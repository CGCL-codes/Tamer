package net.sf.excompcel.spreadsheet.comparator;

import net.sf.excompcel.EnumGridPositionRangeType;
import net.sf.excompcel.model.MainModel;
import net.sf.excompcel.spreadsheet.ECRow;
import net.sf.excompcel.spreadsheet.comparator.compareobject.ComparatorBase;
import net.sf.excompcel.spreadsheet.comparator.compareobject.RowCompareObject;
import net.sf.excompcel.spreadsheet.comparator.compareobject.SheetCompareObject;
import net.sf.excompcel.spreadsheet.impl.base.ECReportBase;
import org.apache.log4j.Logger;

/**
 * 
 * @author Detlev Struebig
 * @version v0.8
 *
 */
public class SheetContains extends SheetComparatorBase {

    /** Logger. */
    private static Logger log = Logger.getLogger(SheetContains.class);

    /**
	 * Constructor.
	 * 
	 * @param wbReport The {@link ECReportBase} containing the Report.
	 */
    @SuppressWarnings("unchecked")
    public SheetContains(ECReportBase wbReport) {
        super(wbReport);
        setComparator(new RowContains(getWbReport()));
    }

    /**
	 * Compare two {@link SheetCompareObject}.
	 * @param master {@link SheetCompareObject}
	 * @param slave {@link SheetCompareObject}
	 * @return 0 if RowCompareObject are equal. Otherwise less or greater than 0.
	 */
    public int compare(SheetCompareObject master, SheetCompareObject slave) {
        int ret = 0;
        if (!master.isObjectExists()) {
            if (!slave.isObjectExists()) {
                log.debug("Both Sheets are NULL. They are equal (0).");
                return 0;
            } else {
                log.debug("Master Sheet is NULL. Slave exists. Return less than (-1).");
                return -1;
            }
        } else if (master.isObjectExists() && !slave.isObjectExists()) {
            log.debug("Master exists. Slave Sheet is NULL. Return greater than (+1).");
            return 1;
        }
        MainModel model = getWbReport().getModel();
        if (model.getCompareColRow().getEnumGridPositionRangeType().equals(EnumGridPositionRangeType.ROW)) {
            log.info("Find Contains.");
            ret = compareRadioContains(master, slave);
        } else if (model.getCompareColRow().getEnumGridPositionRangeType().equals(EnumGridPositionRangeType.COL)) {
            compareRadioNone(master, slave);
        }
        return ret;
    }

    public int compare(ComparatorBase master, ComparatorBase slave) {
        boolean bMaster = master instanceof SheetCompareObject;
        boolean bSlave = slave instanceof SheetCompareObject;
        if (bMaster && bSlave) {
            return compare((SheetCompareObject) master, (SheetCompareObject) slave);
        } else if (bMaster) {
            return 1;
        } else if (bSlave) {
            return -1;
        }
        return 0;
    }

    /**
	 * 
	 * @param master {@link SheetCompareObject}
	 * @param slave {@link SheetCompareObject}
	 * @return 0 if RowCompareObject are equal. Otherwise less or greater than 0.
	 */
    @SuppressWarnings("unchecked")
    private int compareRadioContains(SheetCompareObject master, SheetCompareObject slave) {
        log.info("Find Contains.");
        int ret = 0;
        MainModel model = this.getWbReport().getModel();
        if (model.getCompareColRow().getEnumGridPositionRangeType().equals(EnumGridPositionRangeType.ROW)) {
            log.debug("Compare only one Row");
            int rowReferenceIndex = model.getCompareReferenceRow() - 1;
            int rowCompareIndex = model.getCompareCompareRow() - 1;
            log.debug("rowReference Index=" + rowReferenceIndex + " rowCompare Index=" + rowCompareIndex);
            setRowCompareStart(rowReferenceIndex);
            setRowCompareEnd(rowReferenceIndex);
            ECRow rowMaster = master.getSheet().getRow(rowReferenceIndex);
            ECRow rowSlave = slave.getSheet().getRow(rowCompareIndex);
            ret = getComparator().compare(new RowCompareObject(rowMaster, rowReferenceIndex), new RowCompareObject(rowSlave, rowCompareIndex));
            tickRow();
        } else {
            log.debug("Compare one Column");
            ret = compareRadioNone(master, slave);
        }
        return ret;
    }

    /**
	 * Compare all Rows. From 0 to last used Row.
	 * @param master {@link SheetCompareObject}
	 * @param slave {@link SheetCompareObject}
	 * @return 0 if RowCompareObject are equal. Otherwise less or greater than 0.
	 */
    @SuppressWarnings("unchecked")
    private int compareRadioNone(final SheetCompareObject master, final SheetCompareObject slave) {
        log.info("Compare all.");
        int ret = 0;
        int rowIdxMin = 0;
        int rowIdxMax = master.getSheet().getLastRowIndex();
        log.debug("Min Row=" + rowIdxMin + " Max Row=" + rowIdxMax);
        setRowCompareStart(rowIdxMin);
        setRowCompareEnd(rowIdxMax);
        for (int i = rowIdxMin; i <= rowIdxMax; i++) {
            if (getWbReport().getModel().isStopProcess()) {
                log.info("Manually Stop Comparison Process.");
                break;
            }
            ECRow rowMaster = master.getSheet().getRow(i);
            ECRow rowSlave = slave.getSheet().getRow(i);
            if (log.isDebugEnabled()) {
                log.debug("Master Row=" + i);
            }
            int iCompare = getComparator().compare(new RowCompareObject(rowMaster, i), new RowCompareObject(rowSlave, i));
            tickRow();
            if (getComparator().isDifferent(iCompare)) {
                ret = iCompare;
            }
        }
        return ret;
    }
}
