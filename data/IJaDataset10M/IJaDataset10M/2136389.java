package fd2.diagram.part;

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
public class Fd2CreationWizard extends Wizard implements INewWizard {

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
    protected Fd2CreationWizardPage diagramModelFilePage;

    /**
	 * @generated
	 */
    protected Fd2CreationWizardPage domainModelFilePage;

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
        setWindowTitle(Messages.Fd2CreationWizardTitle);
        setDefaultPageImageDescriptor(Fd2DiagramEditorPlugin.getBundledImageDescriptor("icons/wizban/NewFd2Wizard.gif"));
        setNeedsProgressMonitor(true);
    }

    /**
	 * @generated
	 */
    public void addPages() {
        diagramModelFilePage = new Fd2CreationWizardPage("DiagramModelFile", getSelection(), "fd2_diagram");
        diagramModelFilePage.setTitle(Messages.Fd2CreationWizard_DiagramModelFilePageTitle);
        diagramModelFilePage.setDescription(Messages.Fd2CreationWizard_DiagramModelFilePageDescription);
        addPage(diagramModelFilePage);
        domainModelFilePage = new Fd2CreationWizardPage("DomainModelFile", getSelection(), "fd2") {

            public void setVisible(boolean visible) {
                if (visible) {
                    String fileName = diagramModelFilePage.getFileName();
                    fileName = fileName.substring(0, fileName.length() - ".fd2_diagram".length());
                    setFileName(Fd2DiagramEditorUtil.getUniqueFileName(getContainerFullPath(), fileName, "fd2"));
                }
                super.setVisible(visible);
            }
        };
        domainModelFilePage.setTitle(Messages.Fd2CreationWizard_DomainModelFilePageTitle);
        domainModelFilePage.setDescription(Messages.Fd2CreationWizard_DomainModelFilePageDescription);
        addPage(domainModelFilePage);
    }

    /**
	 * @generated
	 */
    public boolean performFinish() {
        IRunnableWithProgress op = new WorkspaceModifyOperation(null) {

            protected void execute(IProgressMonitor monitor) throws CoreException, InterruptedException {
                diagram = Fd2DiagramEditorUtil.createDiagram(diagramModelFilePage.getURI(), domainModelFilePage.getURI(), monitor);
                if (isOpenNewlyCreatedDiagramEditor() && diagram != null) {
                    try {
                        Fd2DiagramEditorUtil.openDiagram(diagram);
                    } catch (PartInitException e) {
                        ErrorDialog.openError(getContainer().getShell(), Messages.Fd2CreationWizardOpenEditorError, null, e.getStatus());
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
                ErrorDialog.openError(getContainer().getShell(), Messages.Fd2CreationWizardCreationError, null, ((CoreException) e.getTargetException()).getStatus());
            } else {
                Fd2DiagramEditorPlugin.getInstance().logError("Error creating diagram", e.getTargetException());
            }
            return false;
        }
        return diagram != null;
    }
}
