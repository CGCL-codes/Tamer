package de.mpiwg.vspace.exception.beta.service;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import de.mpiwg.vspace.exception.beta.internal.ExceptionHandlingDialog;
import de.mpiwg.vspace.extension.interfaces.IExceptionHandler;

public class ExceptionHandler implements IExceptionHandler {

    public void processException(Throwable exception) {
        processException("", exception);
    }

    public void processException(String msg, Throwable exception) {
        msg += "\n" + exception.getMessage();
        String trace = "Stacktrace is : \n";
        StringWriter strWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(strWriter);
        exception.printStackTrace(writer);
        trace += strWriter.getBuffer().toString();
        msg = msg + "\n" + trace;
        final String finalMsg = msg;
        Display.getDefault().asyncExec(new Runnable() {

            public void run() {
                ExceptionHandlingDialog dlg = new ExceptionHandlingDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(), finalMsg);
                dlg.open();
            }
        });
    }
}
