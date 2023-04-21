package de.erdesignerng.visual.editor;

/**
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-01-15 19:22:44 $
 */
public interface DialogConstants {

    /**
	 * Result code for a correct closed modal dialog.
	 */
    int MODAL_RESULT_OK = 1;

    /**
	 * Result code for a canceled modal dialog.
	 */
    int MODAL_RESULT_CANCEL = 2;

    /**
	 * Show the dialog and return it's result code.
	 * 
	 * @return The model result state
	 */
    int showModal();
}
