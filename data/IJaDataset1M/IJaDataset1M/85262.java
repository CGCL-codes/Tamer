package com.ssd.mdaworks.classdiagram.classDiagram.diagram.part;

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
public class ClassdiagramCreationWizard extends Wizard implements INewWizard {

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
    protected com.ssd.mdaworks.classdiagram.classDiagram.diagram.part.ClassdiagramCreationWizardPage diagramModelFilePage;

    /**
	 * @generated
	 */
    protected com.ssd.mdaworks.classdiagram.classDiagram.diagram.part.ClassdiagramCreationWizardPage domainModelFilePage;

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
        setWindowTitle(com.ssd.mdaworks.classdiagram.classDiagram.diagram.part.Messages.ClassdiagramCreationWizardTitle);
        setDefaultPageImageDescriptor(com.ssd.mdaworks.classdiagram.classDiagram.diagram.part.ClassdiagramDiagramEditorPlugin.getBundledImageDescriptor("icons/wizban/NewClassDiagramWizard.gif"));
        setNeedsProgressMonitor(true);
    }

    /**
	 * @generated
	 */
    public void addPages() {
        diagramModelFilePage = new com.ssd.mdaworks.classdiagram.classDiagram.diagram.part.ClassdiagramCreationWizardPage("DiagramModelFile", getSelection(), "classdiagram_diagram");
        diagramModelFilePage.setTitle(com.ssd.mdaworks.classdiagram.classDiagram.diagram.part.Messages.ClassdiagramCreationWizard_DiagramModelFilePageTitle);
        diagramModelFilePage.setDescription(com.ssd.mdaworks.classdiagram.classDiagram.diagram.part.Messages.ClassdiagramCreationWizard_DiagramModelFilePageDescription);
        addPage(diagramModelFilePage);
        domainModelFilePage = new com.ssd.mdaworks.classdiagram.classDiagram.diagram.part.ClassdiagramCreationWizardPage("DomainModelFile", getSelection(), "classdiagram");
        domainModelFilePage.setTitle(com.ssd.mdaworks.classdiagram.classDiagram.diagram.part.Messages.ClassdiagramCreationWizard_DomainModelFilePageTitle);
        domainModelFilePage.setDescription(com.ssd.mdaworks.classdiagram.classDiagram.diagram.part.Messages.ClassdiagramCreationWizard_DomainModelFilePageDescription);
        addPage(domainModelFilePage);
    }

    /**
	 * @generated
	 */
    public boolean performFinish() {
        IRunnableWithProgress op = new WorkspaceModifyOperation(null) {

            protected void execute(IProgressMonitor monitor) throws CoreException, InterruptedException {
                diagram = com.ssd.mdaworks.classdiagram.classDiagram.diagram.part.ClassdiagramDiagramEditorUtil.createDiagram(diagramModelFilePage.getURI(), domainModelFilePage.getURI(), monitor);
                if (isOpenNewlyCreatedDiagramEditor() && diagram != null) {
                    try {
                        com.ssd.mdaworks.classdiagram.classDiagram.diagram.part.ClassdiagramDiagramEditorUtil.openDiagram(diagram);
                    } catch (PartInitException e) {
                        ErrorDialog.openError(getContainer().getShell(), com.ssd.mdaworks.classdiagram.classDiagram.diagram.part.Messages.ClassdiagramCreationWizardOpenEditorError, null, e.getStatus());
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
                ErrorDialog.openError(getContainer().getShell(), com.ssd.mdaworks.classdiagram.classDiagram.diagram.part.Messages.ClassdiagramCreationWizardCreationError, null, ((CoreException) e.getTargetException()).getStatus());
            } else {
                com.ssd.mdaworks.classdiagram.classDiagram.diagram.part.ClassdiagramDiagramEditorPlugin.getInstance().logError("Error creating diagram", e.getTargetException());
            }
            return false;
        }
        return diagram != null;
    }
}
