package org.gudy.azureus2.plugins.ui.tables;

/**
 * @author TuxPaper
 * @created Sep 19, 2008
 *
 */
public interface TableColumnCreationListener {

    /**
	 * Triggered when a new column is created.  Use the column parameter to
	 * get information about the new column, such as which table created it
	 * 
	 * @param column
	 *
	 * @since 3.1.1.1
	 */
    public void tableColumnCreated(TableColumn column);
}
