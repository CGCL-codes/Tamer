package it.battlehorse.rcp.tools.log;

import org.eclipse.swt.widgets.Composite;

/**
 * Defines helper methods which others can use when dealing with {@code ILoggable} detail dialogs.
 * 
 * @author battlehorse
 * @since May 7, 2006
 */
public interface IDetailHelper {

    /**
	 * Adds a control to the given parent, which shows the details of a {@code ILoggable} object.
	 * If the loggable object represents an exception (as defined by its type and its {@code throwable} member)
	 * then the control will be a text area, otherwise it will be a tree which shows also the nested elements.
	 * 
	 * @param parent the parent
	 * @param detail the loggable object to be shown in detail
	 * @param colspan the column span
	 * @param widthHint the width hint, or {@code SWT.DEFAULT } to use the default one.
	 * @param heightHint the height hint, or {@code SWT.DEFAULT } to use the default one.
	 */
    public void addDetailControl(Composite parent, ILoggable detail, int colspan, int widthHint, int heightHint);

    /**
	 * Defines if the loggable object has nested subelements or not.
	 * 
	 * @param detail the loggable object
	 * @return true if the loggable object has nested subelements or not.
	 */
    public boolean haveDetails(ILoggable detail);
}
