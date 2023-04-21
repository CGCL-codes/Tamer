package com.comarch.depth;

import org.eclipse.core.runtime.ILogListener;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Display;

public class LogHandler implements ILogListener {

    public void logging(IStatus status, String plugin) {
        String pluginName = status.getPlugin();
        if (!pluginName.startsWith("org.eclipse")) if (status.getSeverity() == Status.ERROR || status.getCode() == 1) openErrorDialog(status);
    }

    private void openErrorDialog(IStatus status) {
        final IStatus resultStatus;
        Throwable e = status.getException();
        if (e != null) {
            resultStatus = createStatus(e, status.getMessage(), status);
        } else {
            resultStatus = status;
        }
        Display.getDefault().syncExec(new Runnable() {

            public void run() {
                DepthErrorDialog.open(Display.getCurrent().getActiveShell(), null, null, resultStatus);
            }
        });
    }

    /**
	 * Builds a MultiStatus that includes the whole stacktrace.
	 * @param exception
	 * @param message
	 * @param status
	 * @return
	 */
    private IStatus createStatus(Throwable exception, String message, IStatus status) {
        if (exception != null) {
            MultiStatus ms = new MultiStatus(status.getPlugin(), status.getCode(), message, exception);
            for (StackTraceElement ste : exception.getStackTrace()) {
                ms.add(new Status(status.getSeverity(), status.getPlugin(), status.getCode(), ste.toString(), null));
            }
            IStatus cause = createStatus(exception.getCause(), "Caused by:  ", status);
            if (cause != null) {
                ms.add(cause);
            }
            return ms;
        }
        return null;
    }
}
