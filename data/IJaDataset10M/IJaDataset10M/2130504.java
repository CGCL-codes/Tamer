package org.jcryptool.core.logging.ui;

import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.ui.PlatformUI;

/**
 * Convenience "wrapper" for simple access to the ErrorDialog.
 * 
 * @author t-kern
 *
 */
public class JCTErrorDialog {

    /**
	 * Convenience method for opening a localized ErrorDialog.
	 * 
	 * @see org.eclipse.jface.dialogs.ErrorDialog
	 * 
	 * @param pluginID			The ID of the Plug-in from which this method is called
	 * @param localizedTitle	The localized title
	 * @param localizedMessage	The localized message
	 * @param throwable			The Exception that caused this call
	 * @return					The return value of <code>ErrorDialog.openError(Shell parent, String dialogTitle, String message, IStatus status) </code>
	 */
    public static int openErrorDialog(String pluginID, String localizedTitle, String localizedMessage, Throwable throwable) {
        return ErrorDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), localizedTitle, localizedMessage, new Status(Status.ERROR, pluginID, Status.ERROR, localizedMessage, throwable));
    }
}
