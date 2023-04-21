package EJBTool.persistent.part;

import java.lang.reflect.InvocationTargetException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.WorkspaceModifyOperation;

/**
 * @generated
 */
public class EJBToolCreationWizard extends Wizard implements INewWizard {

    /**
	 * @generated
	 */
    private IWorkbench workbench;

    /**
	 * @generated
	 */
    protected IStructuredSelection selection;

    /**
	 * @generated
	 */
    protected EJBTool.persistent.part.EJBToolCreationWizardPage diagramModelFilePage;

    /**
	 * @generated
	 */
    protected EJBTool.persistent.part.EJBToolCreationWizardPage domainModelFilePage;

    /**
	 * @generated
	 */
    protected Resource diagram;

    /**
	 * @generated
	 */
    private boolean openNewlyCreatedDiagramEditor = true;

    /**
	 * @generated
	 */
    public IWorkbench getWorkbench() {
        return workbench;
    }

    /**
	 * @generated
	 */
    public IStructuredSelection getSelection() {
        return selection;
    }

    /**
	 * @generated
	 */
    public final Resource getDiagram() {
        return diagram;
    }

    /**
	 * @generated
	 */
    public final boolean isOpenNewlyCreatedDiagramEditor() {
        return openNewlyCreatedDiagramEditor;
    }

    /**
	 * @generated
	 */
    public void setOpenNewlyCreatedDiagramEditor(boolean openNewlyCreatedDiagramEditor) {
        this.openNewlyCreatedDiagramEditor = openNewlyCreatedDiagramEditor;
    }

    /**
	 * @generated
	 */
    public void init(IWorkbench workbench, IStructuredSelection selection) {
        this.workbench = workbench;
        this.selection = selection;
        setWindowTitle(EJBTool.persistent.part.Messages.EJBToolCreationWizardTitle);
        setDefaultPageImageDescriptor(EJBTool.persistent.part.EJBToolDiagramEditorPlugin.getBundledImageDescriptor("icons/wizban/NewEJBToolWizard.gif"));
        setNeedsProgressMonitor(true);
    }

    /**
	 * @generated
	 */
    public void addPages() {
        diagramModelFilePage = new EJBTool.persistent.part.EJBToolCreationWizardPage("DiagramModelFile", getSelection(), "EJB Persistent Diagram");
        diagramModelFilePage.setTitle(EJBTool.persistent.part.Messages.EJBToolCreationWizard_DiagramModelFilePageTitle);
        diagramModelFilePage.setDescription(EJBTool.persistent.part.Messages.EJBToolCreationWizard_DiagramModelFilePageDescription);
        addPage(diagramModelFilePage);
        domainModelFilePage = new EJBTool.persistent.part.EJBToolCreationWizardPage("DomainModelFile", getSelection(), "ejbtool");
        domainModelFilePage.setTitle(EJBTool.persistent.part.Messages.EJBToolCreationWizard_DomainModelFilePageTitle);
        domainModelFilePage.setDescription(EJBTool.persistent.part.Messages.EJBToolCreationWizard_DomainModelFilePageDescription);
        addPage(domainModelFilePage);
    }

    /**
	 * @generated
	 */
    public boolean performFinish() {
        IRunnableWithProgress op = new WorkspaceModifyOperation(null) {

            protected void execute(IProgressMonitor monitor) throws CoreException, InterruptedException {
                diagram = EJBTool.persistent.part.EJBToolDiagramEditorUtil.createDiagram(diagramModelFilePage.getURI(), domainModelFilePage.getURI(), monitor);
                if (isOpenNewlyCreatedDiagramEditor() && diagram != null) {
                    try {
                        EJBTool.persistent.part.EJBToolDiagramEditorUtil.openDiagram(diagram);
                    } catch (PartInitException e) {
                        ErrorDialog.openError(getContainer().getShell(), EJBTool.persistent.part.Messages.EJBToolCreationWizardOpenEditorError, null, e.getStatus());
                    }
                }
            }
        };
        try {
            getContainer().run(false, true, op);
        } catch (InterruptedException e) {
            return false;
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof CoreException) {
                ErrorDialog.openError(getContainer().getShell(), EJBTool.persistent.part.Messages.EJBToolCreationWizardCreationError, null, ((CoreException) e.getTargetException()).getStatus());
            } else {
                EJBTool.persistent.part.EJBToolDiagramEditorPlugin.getInstance().logError("Error creating diagram", e.getTargetException());
            }
            return false;
        }
        return diagram != null;
    }
}
