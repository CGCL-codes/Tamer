package net.sf.doolin.gui.table.action;

import java.awt.Point;
import net.sf.doolin.gui.field.event.SelectionAction;

/**
 * This action responds to a selection event in a table.
 * 
 * @author Damien Coraboeuf
 * @version $Id: AbstractTableSelectionAction.java,v 1.1 2007/08/14 11:49:00 guinnessman Exp $
 * @param <T>
 *            Type of items in the table.
 */
public abstract class AbstractTableSelectionAction<T> extends AbstractTableAction<T> implements SelectionAction<Point> {

    /**
	 * Does nothing.
	 * 
	 * @see #getSelectedItem()
	 * @see net.sf.doolin.gui.field.event.SelectionAction#setSelection(java.lang.Object)
	 */
    public void setSelection(Point selection) {
    }
}
