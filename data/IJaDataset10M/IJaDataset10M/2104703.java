package bpmn.diagram.part;

import java.io.InputStream;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.wizards.EditorWizardPage;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.util.DiagramFileCreator;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import bpmn.diagram.edit.parts.BPMNDiagramEditPart;

/**
 * @generated
 */
public class BpmnCreationWizardPage extends EditorWizardPage {

    /**
	 * @generated
	 */
    public BpmnCreationWizardPage(IWorkbench workbench, IStructuredSelection selection) {
        super("CreationWizardPage", workbench, selection);
        setTitle("Create Bpmn Diagram");
        setDescription("Create a new Bpmn diagram.");
    }

    /**
	 * @generated
	 */
    public IFile createAndOpenDiagram(IPath containerPath, String fileName, InputStream initialContents, String kind, IWorkbenchWindow dWindow, IProgressMonitor progressMonitor, boolean saveDiagram) {
        return BpmnDiagramEditorUtil.createAndOpenDiagram(getDiagramFileCreator(), containerPath, fileName, initialContents, kind, dWindow, progressMonitor, isOpenNewlyCreatedDiagramEditor(), saveDiagram);
    }

    /**
	 * @generated
	 */
    protected String getDefaultFileName() {
        return "default";
    }

    /**
	 * @generated
	 */
    public DiagramFileCreator getDiagramFileCreator() {
        return BpmnDiagramFileCreator.getInstance();
    }

    /**
	 * @generated
	 */
    protected String getDiagramKind() {
        return BPMNDiagramEditPart.MODEL_ID;
    }
}
