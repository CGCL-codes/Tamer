package com.metanology.mde.core.ui.common;

import java.lang.reflect.InvocationTargetException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import com.metanology.mde.core.codeFactory.LogFactory;
import com.metanology.mde.core.ui.plugin.MDEPlugin;
import com.metanology.mde.utils.Messages;

/**
 * Show a modal dialog with a progress bar
 * 
 * @author wwang
 *
 */
public class ModalProgressMonitorDialog extends ProgressMonitorDialog {

    private static final long MINITOR_SLEEP_TIME = 500;

    private static final int DEFAULT_TOTAL_SEC = 2;

    private int estimatedTotalSec = DEFAULT_TOTAL_SEC;

    private int numOfJobs = 0;

    private String taskName = "Operation";

    private Cursor arrowCursor;

    private WorkingThread worker;

    /**
	 * Constructor for ModalProgressMonitorDialog.
	 * @param parent
	 */
    public ModalProgressMonitorDialog(Shell parent) {
        super(parent);
    }

    protected void createButtonsForButtonBar(Composite parent) {
        cancel = createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, true);
        if (arrowCursor == null) arrowCursor = new Cursor(cancel.getDisplay(), SWT.CURSOR_ARROW);
        cancel.setCursor(arrowCursor);
        cancel.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event e) {
                handleCancelAction();
            }
        });
        setOperationCancelButtonEnabled(enableCancelButton);
    }

    protected void setOperationCancelButtonEnabled(boolean b) {
        operationCancelableState = b;
        cancel.setEnabled(b);
    }

    /**
	 * @see org.eclipse.jface.operation.IRunnableContext#run(boolean, boolean, IRunnableWithProgress)
	 */
    public void run(boolean fork, boolean cancelable, IRunnableWithProgress runnable) throws InvocationTargetException, InterruptedException {
        MDEPlugin.getDefault().resetConsoleViewers();
        setCancelable(cancelable);
        open();
        try {
            int length = 0;
            if (numOfJobs <= 1 && estimatedTotalSec > 0) {
                length = (int) (estimatedTotalSec * 1000 / MINITOR_SLEEP_TIME);
            } else {
                length = numOfJobs;
            }
            if (length > 0) {
                if (this.getProgressMonitor() != null) {
                    this.getProgressMonitor().beginTask(taskName, length);
                }
            }
            MonitorThread monitor = new MonitorThread();
            worker = new WorkingThread();
            monitor.length = length;
            if (numOfJobs > 1) {
                monitor.length = 0;
            }
            monitor.workor = worker;
            monitor.progress = this.getProgressMonitor();
            monitor.display = this.getParentShell().getDisplay();
            monitor.taskName = this.taskName;
            worker.runnable = runnable;
            worker.display = this.getParentShell().getDisplay();
            worker.monitor = this.getProgressMonitor();
            worker.start();
            monitor.start();
        } finally {
        }
    }

    protected void handleCancelAction() {
        if (worker != null) {
            if (worker.runnable instanceof ControllableOperation) {
                ((ControllableOperation) worker.runnable).stop();
            }
        }
    }

    private class MonitorThread extends Thread {

        Display display;

        IProgressMonitor progress;

        Thread workor;

        int length;

        int worked = 0;

        String taskName;

        private void updateProgress() {
            if (progress != null && display != null) {
                display.asyncExec(new Runnable() {

                    public void run() {
                        if (worked >= length) {
                            progress.beginTask(taskName, length);
                        }
                        progress.worked(1);
                        worked++;
                    }
                });
            }
        }

        public void run() {
            this.setPriority(Thread.MIN_PRIORITY);
            if (length > 0) {
                updateProgress();
            }
            while (workor != null && workor.isAlive()) {
                try {
                    sleep(MINITOR_SLEEP_TIME);
                } catch (Exception ignore) {
                }
                if (length > 0) {
                    updateProgress();
                }
            }
            if (length > 0 && progress != null) {
                updateProgress();
            }
        }
    }

    private class WorkingThread extends Thread {

        IRunnableWithProgress runnable;

        IProgressMonitor monitor;

        Display display;

        public void run() {
            try {
                runnable.run(monitor);
            } catch (InterruptedException interrupted) {
            } catch (Exception e) {
                LogFactory.getLog().error(Messages.ERR_RUN_THREAD);
                e.printStackTrace();
            } finally {
                display.asyncExec(new Runnable() {

                    public void run() {
                        close();
                    }
                });
            }
        }
    }

    /**
	 * Sets the estimated total second for the job
     * The dialog will update the progress bar when 
     * execution time progress. This value will be 
     * ignored if numOfJobs is set.
     * 
	 * @param estimatedTotalSec The estimatedTotalSec to set
	 */
    public void setEstimatedTotalSec(int estimatedTotalSec) {
        this.estimatedTotalSec = estimatedTotalSec;
    }

    /**
	 * Sets the total number of jobs. The runnable is 
     * responsible to update the progress bar.
     * 
	 * @param numOfJobs The numOfJobs to set
	 */
    public void setNumOfJobs(int numOfJobs) {
        this.numOfJobs = numOfJobs;
    }

    /**
	 * Sets the taskName.
	 * @param taskName The taskName to set
	 */
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
}
