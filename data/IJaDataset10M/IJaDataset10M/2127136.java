package de.fuh.xpairtise.plugin.util;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import de.fuh.xpairtise.common.XPLog;
import de.fuh.xpairtise.plugin.Activator;
import de.fuh.xpairtise.plugin.ui.util.Dialogs;

/**
 * This class provides static methods for client-side logging debug output.
 */
public class ClientXPLog {

    /**
   * Logs an exception to Eclipse's error log and shows an error message to the
   * user.
   * 
   * @param errorCode 
   *          error code specific to the plug-in
   * @param dialogTitle 
   *          title of the dialog
   * @param message 
   *          message to be shown to the user
   * @param e 
   *          exception to be logged
   * @param synchronous 
   *          <code>true</code>if the dialog should be displayed
   *          synchronously or <code>false</code> if the dialog should be
   *          displayed in parallel
   */
    public static void logException(final int errorCode, final String dialogTitle, final String message, final Throwable e, final boolean synchronous) {
        final Throwable ex = (e == null) ? null : new Throwable(e);
        final String msg = (message == null) ? null : new String(message);
        final String title = (dialogTitle == null) ? new String("Exception") : new String(dialogTitle);
        final int code = errorCode;
        if (ex != null) {
            final String causeMsg = (ex.getCause().getMessage() == null) ? ex.getMessage() : ex.getCause().getMessage();
            final Display display = PlatformUI.getWorkbench().getDisplay();
            if (display != null && !display.isDisposed()) {
                Runnable runnable = new Runnable() {

                    public void run() {
                        Activator.logError(code, causeMsg, ex.getCause());
                        if (XPLog.isDebugEnabled()) {
                            ex.getCause().printStackTrace();
                        }
                        if (msg != null) {
                            Dialogs.showError(display.getActiveShell(), title, msg + "\n\nReason: " + causeMsg + "\n\nDetails can be found on the error log view.");
                        }
                    }
                };
                if (synchronous) {
                    display.syncExec(runnable);
                } else {
                    display.asyncExec(runnable);
                }
            }
        }
    }
}
