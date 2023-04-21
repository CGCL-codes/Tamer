package at.jku.xlwrap.spreadsheet;

import at.jku.xlwrap.common.XLWrapException;

/**
 * @author dorgon
 *
 */
public interface Workbook {

    /**
	 * @param sheetNum
	 * @return
	 * @throws XLWrapException 
	 */
    public Sheet getSheet(int sheetNum) throws XLWrapException;

    /**
	 * @param sheetName
	 * @return
	 * @throws XLWrapException 
	 */
    public Sheet getSheet(String sheetName) throws XLWrapException;

    /**
	 * @return
	 */
    public boolean supportsMultipleSheets();

    /**
	 * If this method is not supported by the implementation, range references
	 * will only work with sheet numbers and not with sheet names!
	 * 
	 * @return
	 */
    public String[] getSheetNames();

    /**
	 * get info about the location of the workbook used for error messages
	 * @return
	 */
    public String getWorkbookInfo();

    /**
	 * close and free resources
	 */
    public void close();
}
