package csheets.ui.ctrl;

import java.util.EventListener;

/**
 * A listener for receiving notification changes when a new active workbook,
 * spreadsheet and/or cell is selected.
 * @author Einar Pehrson
 */
public interface SelectionListener extends EventListener {

    /**
	 * Invoked when the active workbook, spreadsheet and/or cell of the
	 * application is changed.
	 * @param event the selection event that was fired
	 */
    public void selectionChanged(SelectionEvent event);
}
