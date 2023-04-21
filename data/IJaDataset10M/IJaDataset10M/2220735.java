package net.confex.app.wizards;

import java.lang.reflect.InvocationTargetException;
import net.confex.app.Confex;
import net.confex.app.core.ConfexException;
import net.confex.app.core.INode;
import net.confex.app.core.ITree;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.internal.misc.StatusUtil;
import org.eclipse.ui.statushandlers.IStatusAdapterConstants;
import org.eclipse.ui.statushandlers.StatusAdapter;
import org.eclipse.ui.statushandlers.StatusManager;

public class NodeWizard extends BaseWizard implements INewWizard {

    public static final String ID = Confex.PLUGIN_ID + ".nodeWizard";

    private NodeWizardPage page;

    INode newNode;

    private ITree treeSelected;

    private INode nodeSelected;

    public NodeWizard() {
    }

    @Override
    public boolean performFinish() {
        createNewNode();
        if (newNode == null) return false;
        selectAndReveal(newNode);
        return true;
    }

    @Override
    public void addPages() {
        page = new NodeWizardPage();
        if (treeSelected != null) page.setTree(treeSelected);
        if (nodeSelected != null) page.setParent(nodeSelected);
        addPage(page);
    }

    private INode createNewNode() {
        if (newNode != null) return newNode;
        runCreation(getRunnable(), page.getNodeName());
        newNode = page.getNodeHandle();
        return newNode;
    }

    private IRunnableWithProgress getRunnable() {
        return new IRunnableWithProgress() {

            public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                if (monitor == null) {
                    monitor = new NullProgressMonitor();
                }
                monitor.beginTask("Creating Confex Node", 1);
                try {
                    INode node = page.getTree().getNode(page.getNodeId());
                    node.create(page.getNodeName(), new SubProgressMonitor(monitor, 1));
                } catch (ConfexException e) {
                    throw new InvocationTargetException(e);
                } catch (OperationCanceledException e) {
                    throw new InterruptedException();
                } finally {
                    monitor.done();
                }
            }
        };
    }

    @Override
    public void init(IWorkbench workbench, IStructuredSelection selection) {
        this.workbench = workbench;
        setNeedsProgressMonitor(true);
        setWindowTitle("New Node");
        processSelection(selection);
    }

    private void processSelection(IStructuredSelection selection) {
        if (selection == null || selection.isEmpty()) return;
        Object element = selection.getFirstElement();
        if (element instanceof ITree) treeSelected = (ITree) selection.getFirstElement();
        if (element instanceof INode) {
            nodeSelected = (INode) element;
            treeSelected = nodeSelected.getTree();
        }
    }

    @Override
    protected void handleCreationProblems(String name, Throwable t) {
        if (t instanceof ExecutionException && t.getCause() instanceof CoreException) {
            CoreException cause = (CoreException) t.getCause();
            StatusAdapter status;
            status = new StatusAdapter(StatusUtil.newStatus(cause.getStatus().getSeverity(), "Creation Problems", cause));
            status.setProperty(IStatusAdapterConstants.TITLE_PROPERTY, "Creation Problems");
            StatusManager.getManager().handle(status, StatusManager.BLOCK);
        } else {
            StatusAdapter status = new StatusAdapter(new Status(IStatus.WARNING, Confex.APP_ID, 0, NLS.bind("Internal error: {0}", t.getMessage()), t));
            status.setProperty(IStatusAdapterConstants.TITLE_PROPERTY, "Creation Problems");
            StatusManager.getManager().handle(status, StatusManager.LOG | StatusManager.BLOCK);
        }
    }
}
