package com.byterefinery.rmbench.dialogs;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.progress.IProgressService;
import com.byterefinery.rmbench.RMBenchConstants;
import com.byterefinery.rmbench.RMBenchPlugin;
import com.byterefinery.rmbench.util.ImageConstants;

/**
 * dialog that allows selecting a single file resource from the currently open projects.
 * <p/>
 * <em>amazingly I could not find such dialog in eclipse</em>
 * @author cse
 */
public class FileResourceSelectionDialog extends ElementTreeSelectionDialog {

    private static ITreeContentProvider contentProvider = new ITreeContentProvider() {

        public Object[] getChildren(Object element) {
            if (element instanceof IContainer) {
                try {
                    return ((IContainer) element).members();
                } catch (CoreException e) {
                }
            }
            return null;
        }

        public Object getParent(Object element) {
            return ((IResource) element).getParent();
        }

        public boolean hasChildren(Object element) {
            return element instanceof IContainer;
        }

        public Object[] getElements(Object input) {
            return (Object[]) input;
        }

        public void dispose() {
        }

        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        }
    };

    private static final IStatus OK = new Status(IStatus.OK, RMBenchConstants.PLUGIN_ID, 0, "", null);

    private static final IStatus ERROR = new Status(IStatus.ERROR, RMBenchConstants.PLUGIN_ID, 0, "", null);

    private static ISelectionStatusValidator validator = new ISelectionStatusValidator() {

        public IStatus validate(Object[] selection) {
            return selection.length == 1 && selection[0] instanceof IFile ? OK : ERROR;
        }
    };

    public FileResourceSelectionDialog(Shell parent, String title, String message) {
        super(parent, WorkbenchLabelProvider.getDecoratingWorkbenchLabelProvider(), contentProvider);
        setTitle(title);
        setMessage(message);
        setInput(computeInput(false));
        setValidator(validator);
    }

    protected Control createDialogArea(Composite parent) {
        Composite dialogArea = (Composite) super.createDialogArea(parent);
        Button refreshButton = new Button(dialogArea, SWT.PUSH);
        refreshButton.setImage(RMBenchPlugin.getImage(ImageConstants.REFRESH));
        refreshButton.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, false, false));
        refreshButton.setToolTipText(Messages.FileResourceSelectionDialog_refreshTooltip);
        refreshButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                setInput(computeInput(true));
                FileResourceSelectionDialog.this.getTreeViewer().refresh();
            }
        });
        return dialogArea;
    }

    private Object[] computeInput(boolean refresh) {
        try {
            ResourcesPlugin.getWorkspace().getRoot().refreshLocal(IResource.DEPTH_ONE, null);
        } catch (CoreException e) {
            RMBenchPlugin.logError(e);
        }
        IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
        List<IProject> openProjects = new ArrayList<IProject>(projects.length);
        for (int i = 0; i < projects.length; i++) {
            if (projects[i].isOpen()) {
                openProjects.add(projects[i]);
                if (refresh) {
                    new Refresher(projects[i]).run();
                }
            }
        }
        return openProjects.toArray();
    }

    private static class Refresher implements IRunnableWithProgress {

        private final IProject project;

        Refresher(IProject project) {
            this.project = project;
        }

        public void run() {
            IProgressService progressService = RMBenchPlugin.getDefault().getWorkbench().getProgressService();
            try {
                progressService.busyCursorWhile(this);
            } catch (Exception e) {
                RMBenchPlugin.logError(e);
            }
        }

        public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
            try {
                project.refreshLocal(IResource.DEPTH_INFINITE, null);
            } catch (CoreException e) {
                RMBenchPlugin.logError(e);
            }
        }
    }
}
