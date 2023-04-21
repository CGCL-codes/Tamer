package com.ctb.diagram.part;

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
public class CtbCreationWizard extends Wizard implements INewWizard {

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
    protected CtbCreationWizardPage diagramModelFilePage;

    /**
	 * @generated
	 */
    protected CtbCreationWizardPage domainModelFilePage;

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
        setWindowTitle(Messages.CtbCreationWizardTitle);
        setDefaultPageImageDescriptor(CtbDiagramEditorPlugin.getBundledImageDescriptor("icons/wizban/NewCtbWizard.gif"));
        setNeedsProgressMonitor(true);
    }

    /**
	 * @generated
	 */
    public void addPages() {
        diagramModelFilePage = new CtbCreationWizardPage("DiagramModelFile", getSelection(), "ctb_diagram");
        diagramModelFilePage.setTitle(Messages.CtbCreationWizard_DiagramModelFilePageTitle);
        diagramModelFilePage.setDescription(Messages.CtbCreationWizard_DiagramModelFilePageDescription);
        addPage(diagramModelFilePage);
        domainModelFilePage = new CtbCreationWizardPage("DomainModelFile", getSelection(), "ctb");
        domainModelFilePage.setTitle(Messages.CtbCreationWizard_DomainModelFilePageTitle);
        domainModelFilePage.setDescription(Messages.CtbCreationWizard_DomainModelFilePageDescription);
        addPage(domainModelFilePage);
    }

    /**
	 * @generated
	 */
    public boolean performFinish() {
        IRunnableWithProgress op = new WorkspaceModifyOperation(null) {

            protected void execute(IProgressMonitor monitor) throws CoreException, InterruptedException {
                diagram = CtbDiagramEditorUtil.createDiagram(diagramModelFilePage.getURI(), domainModelFilePage.getURI(), monitor);
                if (isOpenNewlyCreatedDiagramEditor() && diagram != null) {
                    try {
                        CtbDiagramEditorUtil.openDiagram(diagram);
                    } catch (PartInitException e) {
                        ErrorDialog.openError(getContainer().getShell(), Messages.CtbCreationWizardOpenEditorError, null, e.getStatus());
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
                ErrorDialog.openError(getContainer().getShell(), Messages.CtbCreationWizardCreationError, null, ((CoreException) e.getTargetException()).getStatus());
            } else {
                CtbDiagramEditorPlugin.getInstance().logError("Error creating diagram", e.getTargetException());
            }
            return false;
        }
        return diagram != null;
    }
}
