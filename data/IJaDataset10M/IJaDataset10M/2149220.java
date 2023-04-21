package net.sf.callmesh.view;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public final class ErrorDisplay {

    public static final ErrorDisplay create() {
        return new ErrorDisplay(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
    }

    private final Shell shell;

    public ErrorDisplay(Shell shell) {
        this.shell = shell;
    }

    /** show an error dialog */
    public void showError(final String title, final String message) {
        final Display display = shell.getDisplay();
        display.asyncExec(new Runnable() {

            public void run() {
                MessageDialog.openError(shell, prefixTitle(title), message);
            }
        });
    }

    /** show an error dialog */
    public void showError(final Throwable t) {
        t.printStackTrace();
        final Display display = shell.getDisplay();
        display.asyncExec(new Runnable() {

            public void run() {
                final String title = prefixTitle("Caught an Exception");
                final String message = "An Exception occurred";
                final int mask = IStatus.OK | IStatus.CANCEL | IStatus.ERROR | IStatus.WARNING | IStatus.INFO;
                final Status status = new Status(IStatus.ERROR, "callMesh", IStatus.ERROR, t.getMessage(), t);
                ErrorDialog dialog = new ErrorDialog(shell, title, message, status, mask);
                dialog.open();
            }
        });
    }

    private String prefixTitle(String title) {
        return "CallMesh: " + title;
    }
}
