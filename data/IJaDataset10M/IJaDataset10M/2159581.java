package org.alcatel.jsce.servicecreation.ui;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 *  Description:
 * <p>
 * Window used for wating for Search components Job.
 * The window cvan be uesd as progress monitor.
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class WaitDialog extends Dialog implements IProgressMonitor {

    /** Custom message*/
    private String customMessage = "";

    /** The widget*/
    private WaitingWidget waitWidget = null;

    /** The stored task name*/
    private String taskName = "Current task";

    /**
	 * @param parent
	 * @param message
	 */
    public WaitDialog(Shell parent, String message) {
        super(parent);
        setBlockOnOpen(false);
        this.customMessage = message;
    }

    protected Control createContents(Composite parent) {
        waitWidget = new WaitingWidget(parent, SWT.NONE, this.customMessage);
        waitWidget.setTaskName(taskName);
        return waitWidget;
    }

    /**
	 * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
	 */
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText("Mobicents EclipSLEE ");
    }

    public void setText(String msg) {
        this.waitWidget.setMsg(msg);
    }

    public void beginTask(String name, int totalWork) {
    }

    public void done() {
    }

    public void internalWorked(double work) {
    }

    public boolean isCanceled() {
        return false;
    }

    public void setCanceled(boolean value) {
    }

    public void setTaskName(String name) {
        if (waitWidget != null) this.waitWidget.setTaskName(name);
        taskName = name;
    }

    public void subTask(String name) {
        setText(name);
    }

    public void worked(int work) {
    }
}
