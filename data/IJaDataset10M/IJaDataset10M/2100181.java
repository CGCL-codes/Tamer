package net.sf.excompcel.poi.sheet.comparator;

import net.sf.excompcel.poi.report.impl.ReportWorkbook;
import org.apache.log4j.Logger;

/**
 * @author detlev struebig
 * @version v0.7
 *
 */
public abstract class RowComparatorBase extends ComparatorSheetBase {

    /** Logger. */
    @SuppressWarnings("unused")
    private static Logger log = Logger.getLogger(RowComparatorBase.class);

    /** cell comparator. */
    private CellComparator cellComparator;

    /**
	 * Constructor. 
	 * @param wbReport {@link ReportWorkbook}
	 */
    public RowComparatorBase(ReportWorkbook wbReport) {
        super(wbReport);
        setCellComparator(new CellComparator(getWbReport()));
    }

    /**
	 * Set {@link CellComparator}.
	 * @param cellComparator the {@link CellComparator} to set
	 */
    public void setCellComparator(CellComparator cellComparator) {
        this.cellComparator = cellComparator;
    }

    /**
	 * Get {@link CellComparator}.
	 * @return the {@link CellComparator}
	 */
    public CellComparator getCellComparator() {
        return cellComparator;
    }
}
