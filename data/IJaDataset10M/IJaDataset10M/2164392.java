package net.sf.doolin.gui.service;

import java.awt.Cursor;

/**
 * This service provides a way to create custom cursors.
 * 
 * @author Damien Coraboeuf
 * @version $Id$
 */
public interface CursorFactory {

    /**
	 * Gets a cursor for the given id.
	 * 
	 * @param cursorId
	 *            Cursor id
	 * @return Custom cursor or <code>null</code> if none is defined for this
	 *         id.
	 */
    Cursor getCursor(String cursorId);
}
