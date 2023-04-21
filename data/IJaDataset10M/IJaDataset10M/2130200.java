package org.gudy.azureus2.plugins.ui.tables;

/**
 * Mouse event information for 
 * {@link org.gudy.azureus2.plugins.ui.tables.TableCellMouseListener}
 * <br><br>
 * Note: 3.0.1.7 moved most functions to {@link TableRowMouseEvent}
 * 
 * @author TuxPaper
 * @created Jan 10, 2006
 * @since 2.3.0.7
 */
public class TableCellMouseEvent extends TableRowMouseEvent {

    /**
	 * TableCell that the mouse trigger applies to
	 *  
	 * @since 2.3.0.7
	 */
    public TableCell cell;
}
