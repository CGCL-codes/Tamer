package hu.scytha.gui.dialog;

import hu.scytha.common.Util;
import java.lang.reflect.InvocationTargetException;
import org.eclipse.core.runtime.*;
import org.eclipse.jface.dialogs.*;
import org.eclipse.jface.operation.*;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.*;

/**
 * A modal dialog that displays progress during a long running operation.
 * <p>
 * This concrete dialog class can be instantiated as is, or further subclassed
 * as required.
 * </p>
 * <p>
 * Typical usage is:
 * 
 * <pre>
 * 
 *  
 *   try {
 *      IRunnableWithProgress op = ...;
 *      new ProgressMonitorDialog(activeShell).run(true, true, op);
 *   } catch (InvocationTargetException e) {
 *      // handle exception
 *   } catch (InterruptedException e) {
 *      // handle cancelation
 *   }
 *   
 *  
 * </pre>
 * 
 * </p>
 * <p>
 * Note that the ProgressMonitorDialog is not intended to be used with multiple
 * runnables - this dialog should be discarded after completion of one
 * IRunnableWithProgress and a new one instantiated for use by a second or
 * sebsequent IRunnableWithProgress to ensure proper initialization.
 * </p>
 * <p>
 * Note that not forking the process will result in it running in the UI which
 * may starve the UI. The most obvious symptom of this problem is non
 * responsiveness of the cancel button. If you are running within the UI Thread
 * you should do the bulk of your work in another Thread to prevent starvation.
 * It is recommended that fork is set to true in most cases.
 * </p>
 */
public class TwoLabelsProgressMonitorDialog extends IconAndMessageDialog implements IRunnableContext {

    /**
    * Name to use for task when normal task name is empty string.
    */
    private static String DEFAULT_TASKNAME = JFaceResources.getString("ProgressMonitorDialog.message");

    private static int BAR_DLUS = 9;

    /**
    * The progress indicator control.
    */
    protected ProgressIndicator progressIndicator;

    /**
    * The label control for the task. Kept for backwards compatibility.
    */
    protected Label taskLabel;

    /**
    * The label control for the subtask.
    */
    protected Label subTaskLabel;

    /**
    * The label control for the subtask 2.
    */
    protected Label subTaskLabel2;

    /**
    * The Cancel button control.
    */
    protected Button cancel;

    /**
    * The Background button control.
    */
    protected Button background;

    /**
    * Indicates whether the Cancel button is to be shown.
    */
    protected boolean operationCancelableState = false;

    /**
    * Indicates whether the Cancel button is to be enabled.
    */
    protected boolean enableCancelButton;

    /**
    * The progress monitor.
    */
    private ProgressMonitor progressMonitor = new ProgressMonitor();

    /**
    * The name of the current task (used by ProgressMonitor).
    */
    private String task;

    /**
    * The nesting depth of currently running runnables.
    */
    private int nestingDepth;

    /**
    * The cursor used in the cancel button;
    */
    protected Cursor arrowCursor;

    /**
    * The cursor used in the shell;
    */
    private Cursor waitCursor;

    /**
    * Flag indicating whether to open or merely create the dialog before run.
    */
    private boolean openOnRun = true;

    /**
    * Internal progress monitor implementation.
    */
    private class ProgressMonitor implements IProgressMonitorWithBlocking {

        private String fSubTask = "";

        private String fSubTask2 = "";

        private boolean fIsCanceled;

        protected boolean forked = false;

        protected boolean locked = false;

        public void beginTask(String name, int totalWork) {
            if (progressIndicator.isDisposed()) {
                return;
            }
            if (name == null) {
                task = "";
            } else {
                task = name;
            }
            String s = task;
            if (s.length() <= 0) {
                s = DEFAULT_TASKNAME;
            }
            setMessage(s);
            if (!forked) {
                update();
            }
            if (totalWork == UNKNOWN) {
                progressIndicator.beginAnimatedTask();
            } else {
                progressIndicator.beginTask(totalWork);
            }
        }

        public void done() {
            if (!progressIndicator.isDisposed()) {
                progressIndicator.sendRemainingWork();
                progressIndicator.done();
            }
        }

        public void setTaskName(String name) {
            if (name == null) {
                task = "";
            } else {
                task = name;
            }
            String s = task;
            if (s.length() <= 0) {
                s = DEFAULT_TASKNAME;
            }
            setMessage(s);
            if (!forked) {
                update();
            }
        }

        public boolean isCanceled() {
            return fIsCanceled;
        }

        public void setCanceled(boolean b) {
            fIsCanceled = b;
            if (locked) clearBlocked();
        }

        /**
       * "\n"
       */
        public void subTask(String name) {
            if (subTaskLabel.isDisposed()) {
                return;
            }
            if (name == null) {
                fSubTask = "";
            } else {
                int index = name.indexOf("\n");
                fSubTask = name.substring(0, index);
                subTask2(name.substring(index + 1));
            }
            subTaskLabel.setText(shortenText(fSubTask, subTaskLabel));
            if (!forked) {
                subTaskLabel.update();
            }
        }

        private void subTask2(String name) {
            if (subTaskLabel2.isDisposed()) {
                return;
            }
            if (name == null) {
                fSubTask2 = "";
            } else {
                fSubTask2 = name;
            }
            subTaskLabel2.setText(shortenText(fSubTask2, subTaskLabel2));
            if (!forked) {
                subTaskLabel2.update();
            }
        }

        public void worked(int work) {
            internalWorked(work);
        }

        public void internalWorked(double work) {
            if (!progressIndicator.isDisposed()) progressIndicator.worked(work);
        }

        public void clearBlocked() {
            locked = false;
            updateForClearBlocked();
        }

        public void setBlocked(IStatus reason) {
            locked = true;
            updateForSetBlocked(reason);
        }
    }

    /**
    * Clear blocked state from the receiver.
    */
    protected void updateForClearBlocked() {
        setMessage(task);
        imageLabel.setImage(getImage());
    }

    /**
    * Set blocked state from the receiver.
    * 
    * @param reason
    *            IStatus that gives the details
    */
    protected void updateForSetBlocked(IStatus reason) {
        setMessage(reason.getMessage());
        imageLabel.setImage(getImage());
    }

    /**
    * Creates a progress monitor dialog under the given shell. The dialog has a
    * standard title and no image. <code>open</code> is non-blocking.
    * 
    * @param parent
    *            the parent shell, or <code>null</code> to create a top-level
    *            shell
    */
    public TwoLabelsProgressMonitorDialog(Shell parent) {
        super(parent);
        setShellStyle(getDefaultOrientation() | SWT.BORDER | SWT.TITLE | SWT.APPLICATION_MODAL);
        setBlockOnOpen(false);
    }

    /**
    * Enables the cancel button (asynchronously).
    * @param b The state to set the button to.
    */
    private void asyncSetOperationCancelButtonEnabled(final boolean b) {
        if (getShell() != null) {
            getShell().getDisplay().asyncExec(new Runnable() {

                public void run() {
                    setOperationCancelButtonEnabled(b);
                }
            });
        }
    }

    /**
    * The cancel button has been pressed.
    */
    protected void cancelPressed() {
        cancel.setEnabled(false);
        progressMonitor.setCanceled(true);
        super.cancelPressed();
    }

    /**
    * The <code>ProgressMonitorDialog</code> implementation of this method
    * only closes the dialog if there are no currently running runnables.
    */
    public boolean close() {
        if (getNestingDepth() <= 0) {
            clearCursors();
            return super.close();
        }
        return false;
    }

    /**
    * Clear the cursors in the dialog.
    */
    protected void clearCursors() {
        if (cancel != null && !cancel.isDisposed()) {
            cancel.setCursor(null);
        }
        Shell shell = getShell();
        if (shell != null && !shell.isDisposed()) {
            shell.setCursor(null);
        }
        if (arrowCursor != null) {
            arrowCursor.dispose();
        }
        if (waitCursor != null) {
            waitCursor.dispose();
        }
        arrowCursor = null;
        waitCursor = null;
    }

    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        shell.setText(JFaceResources.getString("ProgressMonitorDialog.title"));
        shell.setImage(Util.getImageRegistry().get("scytha"));
        if (waitCursor == null) {
            waitCursor = new Cursor(shell.getDisplay(), SWT.CURSOR_WAIT);
        }
        shell.setCursor(waitCursor);
    }

    protected void createButtonsForButtonBar(Composite parent) {
        createCancelButton(parent);
    }

    /**
    * Creates the cancel button.
    * 
    * @param parent the parent composite
    */
    protected void createCancelButton(Composite parent) {
        cancel = createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, true);
        if (arrowCursor == null) {
            arrowCursor = new Cursor(cancel.getDisplay(), SWT.CURSOR_ARROW);
        }
        cancel.setCursor(arrowCursor);
        setOperationCancelButtonEnabled(enableCancelButton);
    }

    protected Control createDialogArea(Composite parent) {
        setMessage(DEFAULT_TASKNAME);
        createMessageArea(parent);
        taskLabel = messageLabel;
        progressIndicator = new ProgressIndicator(parent);
        GridData gd = new GridData();
        gd.heightHint = convertVerticalDLUsToPixels(BAR_DLUS);
        gd.horizontalAlignment = GridData.FILL;
        gd.grabExcessHorizontalSpace = true;
        gd.horizontalSpan = 2;
        progressIndicator.setLayoutData(gd);
        subTaskLabel = new Label(parent, SWT.LEFT);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        subTaskLabel.setLayoutData(gd);
        subTaskLabel.setFont(parent.getFont());
        subTaskLabel2 = new Label(parent, SWT.LEFT);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        subTaskLabel2.setLayoutData(gd);
        subTaskLabel2.setFont(parent.getFont());
        return parent;
    }

    protected Point getInitialSize() {
        Point calculatedSize = super.getInitialSize();
        if (calculatedSize.x < 450) {
            calculatedSize.x = 450;
        }
        return calculatedSize;
    }

    /**
    * Returns the progress monitor to use for operations run in this progress
    * dialog.
    * 
    * @return the progress monitor
    */
    public IProgressMonitor getProgressMonitor() {
        return progressMonitor;
    }

    public void run(boolean fork, boolean cancelable, IRunnableWithProgress runnable) throws InvocationTargetException, InterruptedException {
        setCancelable(cancelable);
        try {
            aboutToRun();
            progressMonitor.forked = fork;
            ModalContext.run(runnable, fork, getProgressMonitor(), getShell().getDisplay());
        } finally {
            finishedRun();
        }
    }

    /**
    * Returns whether the dialog should be opened before the operation is run.
    * Defaults to <code>true</code>
    * 
    * @return <code>true</code> to open the dialog before run,
    *         <code>false</code> to only create the dialog, but not open it
    */
    public boolean getOpenOnRun() {
        return openOnRun;
    }

    /**
    * Sets whether the dialog should be opened before the operation is run.
    * NOTE: Setting this to false and not forking a process may starve any
    * asyncExec that tries to open the dialog later.
    * 
    * @param openOnRun
    *            <code>true</code> to open the dialog before run,
    *            <code>false</code> to only create the dialog, but not open
    *            it
    */
    public void setOpenOnRun(boolean pOpenOnRun) {
        this.openOnRun = pOpenOnRun;
    }

    /**
    * Returns the nesting depth of running operations.
    * 
    * @return the nesting depth of running operations
    */
    protected int getNestingDepth() {
        return nestingDepth;
    }

    /**
    * Increments the nesting depth of running operations.
    */
    protected void incrementNestingDepth() {
        nestingDepth++;
    }

    /**
    * Decrements the nesting depth of running operations. 
    */
    protected void decrementNestingDepth() {
        nestingDepth--;
    }

    /**
    * Called just before the operation is run. Default behaviour is to open or
    * create the dialog, based on the setting of <code>getOpenOnRun</code>,
    * and increment the nesting depth.
    */
    protected void aboutToRun() {
        if (getOpenOnRun()) {
            open();
        } else {
            create();
        }
        incrementNestingDepth();
    }

    /**
    * Called just after the operation is run. Default behaviour is to decrement
    * the nesting depth, and close the dialog.
    */
    protected void finishedRun() {
        decrementNestingDepth();
        close();
    }

    /**
    * Sets whether the progress dialog is cancelable or not.
    * 
    * @param cancelable
    *            <code>true</code> if the end user can cancel this progress
    *            dialog, and <code>false</code> if it cannot be canceled
    */
    public void setCancelable(boolean cancelable) {
        if (cancel == null) {
            enableCancelButton = cancelable;
        } else {
            asyncSetOperationCancelButtonEnabled(cancelable);
        }
    }

    /**
    * Helper to enable/disable Cancel button for this dialog.
    * 
    * @param b
    *            <code>true</code> to enable the cancel button, and
    *            <code>false</code> to disable it
    */
    protected void setOperationCancelButtonEnabled(boolean b) {
        operationCancelableState = b;
        cancel.setEnabled(b);
    }

    protected Image getImage() {
        return getInfoImage();
    }

    /**
    * Set the message in the message label.
    * @param messageString The string for the new message.
    */
    private void setMessage(String messageString) {
        message = messageString == null ? "" : messageString;
        if (messageLabel == null || messageLabel.isDisposed()) {
            return;
        }
        messageLabel.setText(shortenText(message, messageLabel));
    }

    /**
    * Update the message label. Required if the monitor is forked.
    */
    private void update() {
        if (messageLabel == null || messageLabel.isDisposed()) {
            return;
        }
        messageLabel.update();
    }

    public int open() {
        if (!getOpenOnRun()) {
            if (getNestingDepth() == 0) {
                return OK;
            }
        }
        return super.open();
    }
}
