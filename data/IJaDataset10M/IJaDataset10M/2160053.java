package org.dengues.commons.utils;

import java.lang.reflect.InvocationTargetException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.operation.ModalContext;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

/**
 * Qiang.Zhang.Adolf@gmail.com class global comment. Detailled comment <br/>
 * 
 * $Id: Dengues.epf 2007-11-21 qiang.zhang $
 * 
 */
public class UIUtils {

    /**
     * Qiang.Zhang.Adolf@gmail.com UIUtils constructor comment.
     */
    public UIUtils() {
    }

    /**
     * Open a error dialog.
     * 
     * @param msg String
     * @param e Exception
     */
    public static void openErrorDialog(final String msg, final Exception e) {
        final Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
        Display.getDefault().asyncExec(new Runnable() {

            public void run() {
                MessageDialog.openError(shell, msg, e.getMessage());
            }
        });
    }

    /**
     * Qiang.Zhang.Adolf@gmail.com Comment method "runWithProgress".
     * 
     * @param operation
     * @param fork
     * @param monitor
     * @param shell
     */
    public static void runWithProgress(final IRunnableWithProgress operation, final boolean fork, final IProgressMonitor monitor, final Shell shell) {
        final IRunnableWithProgress progress = new IRunnableWithProgress() {

            public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                Thread t = new Thread() {

                    @Override
                    public void run() {
                        try {
                            operation.run(monitor);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                t.start();
                try {
                    t.join();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Runnable run = new Runnable() {

            public void run() {
                try {
                    ModalContext.run(progress, fork, monitor, shell.getDisplay());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        BusyIndicator.showWhile(Display.getDefault(), run);
    }
}
