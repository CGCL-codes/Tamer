package com.gwtspreadsheetinput.jsf.component;

import javax.faces.model.DataModel;

public abstract class SpreadSheetModel extends DataModel {

    /** Check if the current row is empty. If this method returns true
	 *  {@link #getRowData()} will return null.
	 *  @return true if row is available and row data object is empty.
	 **/
    public abstract boolean isRowEmpty();

    /**Deletes data object for the current row.*/
    public abstract void makeRowEmpty();

    /**Creates new data object for the current row.
	 **/
    public abstract void createRowDataObject();

    /** Data objects starting from current row
	 *  are removed. Also, empty rows immediately before current
	 *  row are cleared too. 
	 **/
    public abstract void clearFromCurrentRow();
}
