package ag.ion.bion.officelayer.text.table.extended;

/**
 * Interface for extended table rows.
 * 
 * @author Miriam Sutter
 * @version $Revision: 10398 $
 */
public interface IETextTableRow {

    /**
	 * Returns cells of the text table row.
	 * 
	 * @return cells of the text table row
	 * 
	 * @author Miriam Sutter
	 */
    public IETextTableCell[] getCells();

    /**
	 * Returns the text table cell range.
	 * 
	 * @return text table cell range
	 * 
	 * @author Miriam Sutter
	 */
    public IETextTableCellRange getCellRange();
}
