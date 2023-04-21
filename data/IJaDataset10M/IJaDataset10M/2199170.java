package plweb.diagram.part;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.emf.core.GMFEditingDomainFactory;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import plweb.DiagramRoot;
import plweb.DiagramType;
import plweb.diagram.util.PathHelper;
import plweb.diagram.util.ProjectSynchronizer;
import plweb.diagram.util.projects.ProjectOperationException;

public class PlwebUpdatePWProjectAction implements IObjectActionDelegate {

    private IWorkbenchPart targetPart;

    private URI domainModelURI;

    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
        this.targetPart = targetPart;
    }

    public void selectionChanged(IAction action, ISelection selection) {
        domainModelURI = null;
        action.setEnabled(false);
        if (selection instanceof IStructuredSelection == false || selection.isEmpty()) {
            return;
        }
        IFile file = (IFile) ((IStructuredSelection) selection).getFirstElement();
        domainModelURI = URI.createPlatformResourceURI(file.getFullPath().toString(), true);
        action.setEnabled(true);
    }

    private Shell getShell() {
        return targetPart.getSite().getShell();
    }

    private DiagramRoot getElement(URI uri, TransactionalEditingDomain editingDomain) {
        ResourceSet resourceSet = editingDomain.getResourceSet();
        EObject element = null;
        try {
            Resource resource = resourceSet.getResource(uri, true);
            element = (EObject) resource.getContents().get(0);
        } catch (WrappedException ex) {
            PlwebDiagramEditorPlugin.getInstance().logError("Unable to load resource: " + uri, ex);
        }
        if (element == null || !(element instanceof DiagramRoot)) {
            MessageDialog.openError(getShell(), Messages.AddProduct_ResourceErrorDialogTitle, Messages.AddProduct_ResourceErrorDialogMessage);
            return null;
        }
        DiagramRoot diagramRoot = (DiagramRoot) element;
        return diagramRoot;
    }

    public void run(IAction action) {
        try {
            TransactionalEditingDomain editingDomain = GMFEditingDomainFactory.INSTANCE.createEditingDomain();
            DiagramRoot diagramRoot = getElement(domainModelURI, editingDomain);
            if (diagramRoot.getType().equals(DiagramType.PRODUCT)) {
                MessageDialog.openError(getShell(), "Not supported", "This action is not supported for products");
            } else {
                String resourcePath = domainModelURI.toPlatformString(true);
                String projectName = resourcePath.substring(1, resourcePath.indexOf("/", 1));
                String pathPw = PathHelper.getWorkspaceProjectPath(projectName);
                String pathWr = diagramRoot.getProjectPath();
                ProjectSynchronizer sync = new ProjectSynchronizer(pathWr, pathPw);
                sync.synchronizePw();
                ResourcesPlugin.getWorkspace().getRoot().getProject(projectName).refreshLocal(IResource.DEPTH_INFINITE, null);
            }
        } catch (ProjectOperationException e) {
            ErrorDialog.openError(getShell(), "Error", null, new Status(IStatus.ERROR, PlwebDiagramEditorPlugin.ID, "Error happened while updating PLWeb project"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        MessageDialog.openInformation(getShell(), "Done", "Operation completed successfully");
    }
}
