package se.mdh.mrtc.saveccm.diagram.part;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.core.services.ViewService;
import org.eclipse.gmf.runtime.diagram.core.services.view.CreateDiagramViewOperation;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import se.mdh.mrtc.saveccm.diagram.edit.parts.SystemEditPart;

/**
 * @generated
 */
public class SaveccmNewDiagramFileWizard extends Wizard {

    /**
	 * @generated
	 */
    private WizardNewFileCreationPage myFileCreationPage;

    /**
	 * @generated
	 */
    private ModelElementSelectionPage diagramRootElementSelectionPage;

    /**
	 * @generated
	 */
    private TransactionalEditingDomain myEditingDomain;

    /**
	 * @generated
	 */
    public SaveccmNewDiagramFileWizard(URI domainModelURI, EObject diagramRoot, TransactionalEditingDomain editingDomain) {
        assert domainModelURI != null : "Domain model uri must be specified";
        assert diagramRoot != null : "Doagram root element must be specified";
        assert editingDomain != null : "Editing domain must be specified";
        myFileCreationPage = new WizardNewFileCreationPage(Messages.SaveccmNewDiagramFileWizard_CreationPageName, StructuredSelection.EMPTY);
        myFileCreationPage.setTitle(Messages.SaveccmNewDiagramFileWizard_CreationPageTitle);
        myFileCreationPage.setDescription(NLS.bind(Messages.SaveccmNewDiagramFileWizard_CreationPageDescription, SystemEditPart.MODEL_ID));
        IPath filePath;
        String fileName = domainModelURI.trimFileExtension().lastSegment();
        if (domainModelURI.isPlatformResource()) {
            filePath = new Path(domainModelURI.trimSegments(1).toPlatformString(true));
        } else if (domainModelURI.isFile()) {
            filePath = new Path(domainModelURI.trimSegments(1).toFileString());
        } else {
            throw new IllegalArgumentException("Unsupported URI: " + domainModelURI);
        }
        myFileCreationPage.setContainerFullPath(filePath);
        myFileCreationPage.setFileName(SaveccmDiagramEditorUtil.getUniqueFileName(filePath, fileName, "saveccm_diagram"));
        diagramRootElementSelectionPage = new DiagramRootElementSelectionPage(Messages.SaveccmNewDiagramFileWizard_RootSelectionPageName);
        diagramRootElementSelectionPage.setTitle(Messages.SaveccmNewDiagramFileWizard_RootSelectionPageTitle);
        diagramRootElementSelectionPage.setDescription(Messages.SaveccmNewDiagramFileWizard_RootSelectionPageDescription);
        diagramRootElementSelectionPage.setModelElement(diagramRoot);
        myEditingDomain = editingDomain;
    }

    /**
	 * @generated
	 */
    public void addPages() {
        addPage(myFileCreationPage);
        addPage(diagramRootElementSelectionPage);
    }

    /**
	 * @generated
	 */
    public boolean performFinish() {
        List affectedFiles = new LinkedList();
        IFile diagramFile = myFileCreationPage.createNewFile();
        SaveccmDiagramEditorUtil.setCharset(diagramFile);
        affectedFiles.add(diagramFile);
        URI diagramModelURI = URI.createPlatformResourceURI(diagramFile.getFullPath().toString(), true);
        ResourceSet resourceSet = myEditingDomain.getResourceSet();
        final Resource diagramResource = resourceSet.createResource(diagramModelURI);
        AbstractTransactionalCommand command = new AbstractTransactionalCommand(myEditingDomain, Messages.SaveccmNewDiagramFileWizard_InitDiagramCommand, affectedFiles) {

            protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
                int diagramVID = SaveccmVisualIDRegistry.getDiagramVisualID(diagramRootElementSelectionPage.getModelElement());
                if (diagramVID != SystemEditPart.VISUAL_ID) {
                    return CommandResult.newErrorCommandResult(Messages.SaveccmNewDiagramFileWizard_IncorrectRootError);
                }
                Diagram diagram = ViewService.createDiagram(diagramRootElementSelectionPage.getModelElement(), SystemEditPart.MODEL_ID, SaveccmDiagramEditorPlugin.DIAGRAM_PREFERENCES_HINT);
                diagramResource.getContents().add(diagram);
                return CommandResult.newOKCommandResult();
            }
        };
        try {
            OperationHistoryFactory.getOperationHistory().execute(command, new NullProgressMonitor(), null);
            diagramResource.save(SaveccmDiagramEditorUtil.getSaveOptions());
            SaveccmDiagramEditorUtil.openDiagram(diagramResource);
        } catch (ExecutionException e) {
            SaveccmDiagramEditorPlugin.getInstance().logError("Unable to create model and diagram", e);
        } catch (IOException ex) {
            SaveccmDiagramEditorPlugin.getInstance().logError("Save operation failed for: " + diagramModelURI, ex);
        } catch (PartInitException ex) {
            SaveccmDiagramEditorPlugin.getInstance().logError("Unable to open editor", ex);
        }
        return true;
    }

    /**
	 * @generated
	 */
    private static class DiagramRootElementSelectionPage extends ModelElementSelectionPage {

        /**
		 * @generated
		 */
        protected DiagramRootElementSelectionPage(String pageName) {
            super(pageName);
        }

        /**
		 * @generated
		 */
        protected String getSelectionTitle() {
            return Messages.SaveccmNewDiagramFileWizard_RootSelectionPageSelectionTitle;
        }

        /**
		 * @generated
		 */
        protected boolean validatePage() {
            if (selectedModelElement == null) {
                setErrorMessage(Messages.SaveccmNewDiagramFileWizard_RootSelectionPageNoSelectionMessage);
                return false;
            }
            boolean result = ViewService.getInstance().provides(new CreateDiagramViewOperation(new EObjectAdapter(selectedModelElement), SystemEditPart.MODEL_ID, SaveccmDiagramEditorPlugin.DIAGRAM_PREFERENCES_HINT));
            setErrorMessage(result ? null : Messages.SaveccmNewDiagramFileWizard_RootSelectionPageInvalidSelectionMessage);
            return result;
        }
    }
}
