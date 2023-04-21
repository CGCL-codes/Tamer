package hub.metrik.lang.eprovide.usertrace.step.diagram.part;

import hub.metrik.lang.eprovide.usertrace.step.diagram.edit.parts.TestModelEditPart;
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

/**
 * @generated
 */
public class MMUnitNewDiagramFileWizard extends Wizard {

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
    public MMUnitNewDiagramFileWizard(URI domainModelURI, EObject diagramRoot, TransactionalEditingDomain editingDomain) {
        assert domainModelURI != null : "Domain model uri must be specified";
        assert diagramRoot != null : "Doagram root element must be specified";
        assert editingDomain != null : "Editing domain must be specified";
        myFileCreationPage = new WizardNewFileCreationPage(Messages.MMUnitNewDiagramFileWizard_CreationPageName, StructuredSelection.EMPTY);
        myFileCreationPage.setTitle(Messages.MMUnitNewDiagramFileWizard_CreationPageTitle);
        myFileCreationPage.setDescription(NLS.bind(Messages.MMUnitNewDiagramFileWizard_CreationPageDescription, TestModelEditPart.MODEL_ID));
        IPath filePath;
        String fileName = URI.decode(domainModelURI.trimFileExtension().lastSegment());
        if (domainModelURI.isPlatformResource()) {
            filePath = new Path(domainModelURI.trimSegments(1).toPlatformString(true));
        } else if (domainModelURI.isFile()) {
            filePath = new Path(domainModelURI.trimSegments(1).toFileString());
        } else {
            throw new IllegalArgumentException("Unsupported URI: " + domainModelURI);
        }
        myFileCreationPage.setContainerFullPath(filePath);
        myFileCreationPage.setFileName(MMUnitDiagramEditorUtil.getUniqueFileName(filePath, fileName, "usertracestep"));
        diagramRootElementSelectionPage = new DiagramRootElementSelectionPage(Messages.MMUnitNewDiagramFileWizard_RootSelectionPageName);
        diagramRootElementSelectionPage.setTitle(Messages.MMUnitNewDiagramFileWizard_RootSelectionPageTitle);
        diagramRootElementSelectionPage.setDescription(Messages.MMUnitNewDiagramFileWizard_RootSelectionPageDescription);
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
        MMUnitDiagramEditorUtil.setCharset(diagramFile);
        affectedFiles.add(diagramFile);
        URI diagramModelURI = URI.createPlatformResourceURI(diagramFile.getFullPath().toString(), true);
        ResourceSet resourceSet = myEditingDomain.getResourceSet();
        final Resource diagramResource = resourceSet.createResource(diagramModelURI);
        AbstractTransactionalCommand command = new AbstractTransactionalCommand(myEditingDomain, Messages.MMUnitNewDiagramFileWizard_InitDiagramCommand, affectedFiles) {

            protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
                int diagramVID = MMUnitVisualIDRegistry.getDiagramVisualID(diagramRootElementSelectionPage.getModelElement());
                if (diagramVID != TestModelEditPart.VISUAL_ID) {
                    return CommandResult.newErrorCommandResult(Messages.MMUnitNewDiagramFileWizard_IncorrectRootError);
                }
                Diagram diagram = ViewService.createDiagram(diagramRootElementSelectionPage.getModelElement(), TestModelEditPart.MODEL_ID, MMUnitDiagramEditorPlugin.DIAGRAM_PREFERENCES_HINT);
                diagramResource.getContents().add(diagram);
                diagramResource.getContents().add(diagram.getElement());
                return CommandResult.newOKCommandResult();
            }
        };
        try {
            OperationHistoryFactory.getOperationHistory().execute(command, new NullProgressMonitor(), null);
            diagramResource.save(MMUnitDiagramEditorUtil.getSaveOptions());
            MMUnitDiagramEditorUtil.openDiagram(diagramResource);
        } catch (ExecutionException e) {
            MMUnitDiagramEditorPlugin.getInstance().logError("Unable to create model and diagram", e);
        } catch (IOException ex) {
            MMUnitDiagramEditorPlugin.getInstance().logError("Save operation failed for: " + diagramModelURI, ex);
        } catch (PartInitException ex) {
            MMUnitDiagramEditorPlugin.getInstance().logError("Unable to open editor", ex);
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
            return Messages.MMUnitNewDiagramFileWizard_RootSelectionPageSelectionTitle;
        }

        /**
		 * @generated
		 */
        protected boolean validatePage() {
            if (selectedModelElement == null) {
                setErrorMessage(Messages.MMUnitNewDiagramFileWizard_RootSelectionPageNoSelectionMessage);
                return false;
            }
            boolean result = ViewService.getInstance().provides(new CreateDiagramViewOperation(new EObjectAdapter(selectedModelElement), TestModelEditPart.MODEL_ID, MMUnitDiagramEditorPlugin.DIAGRAM_PREFERENCES_HINT));
            setErrorMessage(result ? null : Messages.MMUnitNewDiagramFileWizard_RootSelectionPageInvalidSelectionMessage);
            return result;
        }
    }
}
