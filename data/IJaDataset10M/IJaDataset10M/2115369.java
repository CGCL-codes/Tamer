package org.eclipse.ui.internal.progress;

import java.util.Collection;
import java.util.Iterator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.WorkbenchImages;
import org.eclipse.ui.progress.IProgressConstants;

/**
 * The ProgressAnimationItem is the animation items that uses
 * the progress bar.
 */
public class ProgressAnimationItem extends AnimationItem implements FinishedJobs.KeptJobsListener {

    ProgressBar bar;

    MouseListener mouseListener;

    Composite top;

    ToolBar toolbar;

    ToolItem toolButton;

    ProgressRegion progressRegion;

    Image noneImage, okImage, errorImage;

    boolean animationRunning;

    JobInfo lastJobInfo;

    private int flags;

    /**
     * Create an instance of the receiver in the supplied region.
     * 
     * @param region The ProgressRegion that contains the receiver.
     * @param flags flags to use for creation of the progress bar
     */
    ProgressAnimationItem(ProgressRegion region, int flags) {
        super(region.workbenchWindow);
        this.flags = flags;
        FinishedJobs.getInstance().addListener(this);
        progressRegion = region;
        mouseListener = new MouseAdapter() {

            public void mouseDoubleClick(MouseEvent e) {
                doAction();
            }
        };
    }

    void doAction() {
        JobTreeElement[] jobTreeElements = FinishedJobs.getInstance().getJobInfos();
        for (int i = jobTreeElements.length - 1; i >= 0; i--) {
            if (jobTreeElements[i] instanceof JobInfo) {
                JobInfo ji = (JobInfo) jobTreeElements[i];
                Job job = ji.getJob();
                if (job != null) {
                    IStatus status = job.getResult();
                    if (status != null && status.getSeverity() == IStatus.ERROR) {
                        String title = ProgressMessages.NewProgressView_errorDialogTitle;
                        String msg = ProgressMessages.NewProgressView_errorDialogMessage;
                        if (!getManager().showErrorFor(job, title, msg)) {
                            ErrorDialog.openError(toolbar.getShell(), title, msg, status);
                            JobTreeElement topElement = (JobTreeElement) ji.getParent();
                            if (topElement == null) {
                                topElement = ji;
                            }
                            FinishedJobs.getInstance().remove(topElement);
                        }
                        return;
                    }
                    IAction action = null;
                    Object property = job.getProperty(IProgressConstants.ACTION_PROPERTY);
                    if (property instanceof IAction) {
                        action = (IAction) property;
                    }
                    if (action != null && action.isEnabled()) {
                        action.run();
                        JobTreeElement topElement = (JobTreeElement) ji.getParent();
                        if (topElement == null) {
                            topElement = ji;
                        }
                        FinishedJobs.getInstance().remove(topElement);
                        return;
                    }
                }
            }
        }
        if (getManager().hasErrors()) {
            getManager().showErrorFor(null, null, null);
        }
        progressRegion.processDoubleClick();
        refresh();
    }

    private IAction getAction(Job job) {
        Object property = job.getProperty(IProgressConstants.ACTION_PROPERTY);
        if (property instanceof IAction) {
            return (IAction) property;
        }
        return null;
    }

    private void refresh() {
        if (!PlatformUI.isWorkbenchRunning()) {
            return;
        }
        if (toolbar == null || toolbar.isDisposed()) {
            return;
        }
        lastJobInfo = null;
        JobTreeElement[] jobTreeElements = FinishedJobs.getInstance().getJobInfos();
        for (int i = jobTreeElements.length - 1; i >= 0; i--) {
            if (jobTreeElements[i] instanceof JobInfo) {
                JobInfo ji = (JobInfo) jobTreeElements[i];
                lastJobInfo = ji;
                Job job = ji.getJob();
                if (job != null) {
                    IStatus status = job.getResult();
                    if (status != null && status.getSeverity() == IStatus.ERROR) {
                        initButton(errorImage, NLS.bind(ProgressMessages.ProgressAnimationItem_error, job.getName()));
                        return;
                    }
                    IAction action = getAction(job);
                    if (action != null && action.isEnabled()) {
                        String tt = action.getToolTipText();
                        if (tt == null || tt.trim().length() == 0) {
                            tt = NLS.bind(ProgressMessages.ProgressAnimationItem_ok, job.getName());
                        }
                        initButton(okImage, tt);
                        return;
                    }
                    initButton(noneImage, ProgressMessages.ProgressAnimationItem_tasks);
                    return;
                }
            }
        }
        ErrorNotificationManager errorNotificationManager = ProgressManager.getInstance().errorManager;
        if (errorNotificationManager.hasErrors()) {
            Collection errors = errorNotificationManager.getErrors();
            for (Iterator iter = errors.iterator(); iter.hasNext(); ) {
                ErrorInfo info = (ErrorInfo) iter.next();
                initButton(errorImage, NLS.bind(ProgressMessages.ProgressAnimationItem_error, info.getJob().getName()));
                return;
            }
        }
        if (animationRunning) {
            initButton(noneImage, ProgressMessages.ProgressAnimationItem_tasks);
            return;
        }
        toolbar.setVisible(false);
    }

    private void initButton(Image im, String tt) {
        toolButton.setImage(im);
        toolButton.setToolTipText(tt);
        toolbar.setVisible(true);
        toolbar.getParent().layout();
    }

    protected Control createAnimationItem(Composite parent) {
        if (okImage == null) {
            Display display = parent.getDisplay();
            noneImage = WorkbenchImages.getWorkbenchImageDescriptor("progress/progress_none.gif").createImage(display);
            okImage = WorkbenchImages.getWorkbenchImageDescriptor("progress/progress_ok.gif").createImage(display);
            errorImage = WorkbenchImages.getWorkbenchImageDescriptor("progress/progress_error.gif").createImage(display);
        }
        top = new Composite(parent, SWT.NULL);
        top.addDisposeListener(new DisposeListener() {

            public void widgetDisposed(DisposeEvent e) {
                FinishedJobs.getInstance().removeListener(ProgressAnimationItem.this);
                noneImage.dispose();
                okImage.dispose();
                errorImage.dispose();
            }
        });
        boolean isCarbon = "carbon".equals(SWT.getPlatform());
        GridLayout gl = new GridLayout();
        if (isHorizontal()) gl.numColumns = isCarbon ? 3 : 2;
        gl.marginHeight = 0;
        gl.marginWidth = 0;
        if (isHorizontal()) {
            gl.horizontalSpacing = 2;
        } else {
            gl.verticalSpacing = 2;
        }
        top.setLayout(gl);
        bar = new ProgressBar(top, flags | SWT.INDETERMINATE);
        bar.setVisible(false);
        bar.addMouseListener(mouseListener);
        GridData gd;
        int hh = 12;
        if (isHorizontal()) {
            gd = new GridData(SWT.BEGINNING, SWT.CENTER, true, false);
            gd.heightHint = hh;
        } else {
            gd = new GridData(SWT.CENTER, SWT.BEGINNING, false, true);
            gd.widthHint = hh;
        }
        bar.setLayoutData(gd);
        toolbar = new ToolBar(top, SWT.FLAT);
        toolbar.setVisible(false);
        toolButton = new ToolItem(toolbar, SWT.NONE);
        toolButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                doAction();
            }
        });
        if (isCarbon) {
            new Label(top, SWT.NONE).setLayoutData(new GridData(4, 4));
        }
        refresh();
        return top;
    }

    /**
	 * @return <code>true</code> if the control is horizontally oriented
	 */
    private boolean isHorizontal() {
        return (flags & SWT.HORIZONTAL) != 0;
    }

    public Control getControl() {
        return top;
    }

    void animationDone() {
        super.animationDone();
        animationRunning = false;
        if (bar.isDisposed()) {
            return;
        }
        bar.setVisible(false);
        refresh();
    }

    /**
     * @return <code>true</code> when the animation is running
     */
    public boolean animationRunning() {
        return animationRunning;
    }

    void animationStart() {
        super.animationStart();
        animationRunning = true;
        if (bar.isDisposed()) {
            return;
        }
        bar.setVisible(true);
        refresh();
    }

    public void removed(JobTreeElement info) {
        final Display display = Display.getDefault();
        display.asyncExec(new Runnable() {

            public void run() {
                refresh();
            }
        });
    }

    public void finished(final JobTreeElement jte) {
        final Display display = Display.getDefault();
        display.asyncExec(new Runnable() {

            public void run() {
                refresh();
            }
        });
    }

    private ErrorNotificationManager getManager() {
        return ProgressManager.getInstance().errorManager;
    }
}
