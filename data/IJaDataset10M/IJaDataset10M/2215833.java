package com.ecmdeveloper.plugin.ui.jobs;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ListSelectionDialog;
import com.ecmdeveloper.plugin.core.model.IDocument;
import com.ecmdeveloper.plugin.core.model.tasks.IFetchPropertiesTask;
import com.ecmdeveloper.plugin.core.model.tasks.IGetDocumentVersionsTask;
import com.ecmdeveloper.plugin.core.util.PluginMessage;
import com.ecmdeveloper.plugin.ui.Activator;

/**
 * @author Ricardo.Belfor
 *
 */
public class GetDocumentVersionJob extends Job {

    private static final String SELECTION_MESSAGE = "Select the document versions";

    private static final String SELECTION_TITLE = "Document Versions Selection";

    private static final String JOB_NAME = "Get Document Versions";

    private static final String FAILED_MESSAGE = "Getting document versions for \"{0}\" failed";

    private static final String MONITOR_MESSAGE = "Getting document versions";

    private IStatus status;

    private IDocument document;

    private Shell shell;

    private List<IDocument> selectedVersions;

    public GetDocumentVersionJob(IDocument document, Shell shell) {
        super(JOB_NAME);
        this.document = document;
        this.shell = shell;
    }

    public Collection<IDocument> getSelectedVersions() {
        return selectedVersions;
    }

    @Override
    protected IStatus run(IProgressMonitor monitor) {
        try {
            monitor.beginTask(MONITOR_MESSAGE, IProgressMonitor.UNKNOWN);
            final List<IDocument> versions = getDocumentVersions();
            monitor.done();
            shell.getDisplay().syncExec(new Runnable() {

                @Override
                public void run() {
                    status = selectDocumentVersion(versions);
                }
            });
            return status;
        } catch (Exception e) {
            String message = MessageFormat.format(FAILED_MESSAGE, document.getName());
            PluginMessage.openErrorFromThread(shell, JOB_NAME, message, e);
        }
        return Status.OK_STATUS;
    }

    private List<IDocument> getDocumentVersions() throws ExecutionException {
        IGetDocumentVersionsTask task = document.getTaskFactory().getGetDocumentVersionsTask(document);
        Activator.getDefault().getTaskManager().executeTaskSync(task);
        List<IDocument> versions = task.getVersions();
        return versions;
    }

    private IStatus selectDocumentVersion(List<IDocument> versions) {
        ListSelectionDialog dialog = createSelectionDialog(versions);
        int returnCode = dialog.open();
        if (returnCode == Window.CANCEL || dialog.getResult().length == 0) {
            return Status.CANCEL_STATUS;
        }
        selectedVersions = new ArrayList<IDocument>();
        for (Object selectedVersion : dialog.getResult()) {
            selectedVersions.add((IDocument) selectedVersion);
        }
        return Status.OK_STATUS;
    }

    private ListSelectionDialog createSelectionDialog(List<IDocument> versions) {
        LabelProvider labelProvider = new DocumentVersionLabelProvider();
        ListSelectionDialog dialog = new ListSelectionDialog(shell, versions.toArray(), new ArrayContentProvider(), labelProvider, SELECTION_MESSAGE);
        dialog.setTitle(SELECTION_TITLE);
        dialog.setMessage(SELECTION_MESSAGE);
        return dialog;
    }

    class DocumentVersionLabelProvider extends LabelProvider {

        private static final String VERSION_LABEL_FORMAT = "Version {1} ({0})";

        @Override
        public Image getImage(Object element) {
            String imageKey = ISharedImages.IMG_OBJ_FILE;
            return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
        }

        @Override
        public String getText(Object element) {
            if (element instanceof IDocument) {
                IDocument version = (IDocument) element;
                String name = version.getName();
                return MessageFormat.format(VERSION_LABEL_FORMAT, name, version.getVersionLabel());
            }
            return super.getText(element);
        }
    }
}
