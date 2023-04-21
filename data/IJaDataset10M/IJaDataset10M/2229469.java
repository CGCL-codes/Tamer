package eu.redseeds.sc.current.ui.validation.fixes;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import eu.redseeds.common.SCProjectHelper;
import eu.redseeds.sc.current.repository.SCProject;
import eu.redseeds.sc.current.repository.SCProjectContainer;
import eu.redseeds.sc.current.ui.Activator;
import eu.redseeds.sc.current.ui.validation.IValidate;
import eu.redseeds.sc.current.ui.validation.ValidationMarkerFactory;
import eu.redseeds.sc.editor.rsl.editors.UseCaseEditor;
import eu.redseeds.sc.editor.rsl.editors.UseCaseEditorInput;
import eu.redseeds.scl.model.rsl.rslrequirementrepresentations.descriptiverequirementrepresentations.ConstrainedLanguageScenarioDTO;
import eu.redseeds.scl.model.rsl.rslrequirements.requirementsspecifications.UseCaseDTO;

/**
 * A fix that just opens the UseCase editor and activates proper scenario tab
 *
 */
public class UseCaseScenFix implements IMarkerResolution {

    private static String ID_USECASE_EDITOR = "eu.redseeds.sc.editor.rsl.editors.UseCaseEditor";

    @Override
    public String getLabel() {
        return IValidate.FIX_MSG_UC_SCENARIOS;
    }

    @Override
    public void run(IMarker marker) {
        try {
            Integer elementID = (Integer) marker.getAttribute("sclElementID");
            if (marker.getResource() instanceof IProject) {
                SCProject scProject = SCProjectContainer.instance().getSCProject((IProject) marker.getResource());
                ConstrainedLanguageScenarioDTO scenario = scProject.getBusinessLayerFacade().getConstrainedLanguageScenarioByVertexID(elementID.intValue());
                if (scenario == null) {
                    resolveProblemWhenElementsDeleted(marker);
                    return;
                }
                UseCaseEditorInput useCaseInput = new UseCaseEditorInput();
                UseCaseDTO useCase = scenario.getParent();
                if (useCase == null) {
                    resolveProblemWhenElementsDeleted(marker);
                    return;
                }
                useCaseInput.setUseCaseDTO(useCase);
                IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
                UseCaseEditor useCaseEditor = (UseCaseEditor) activePage.openEditor(useCaseInput, ID_USECASE_EDITOR, false, 1);
                useCaseEditor.setFacade(scProject.getBusinessLayerFacade());
                useCaseEditor.setScProject(scProject);
                useCaseEditor.setUseCase(useCase);
                useCaseEditor.setTab(scenario);
                marker.delete();
            }
        } catch (CoreException e) {
            Activator.log("Error ccured during resolving validation problem: " + e.getMessage(), Status.WARNING);
        }
    }

    private void showMessageBoxElementsDeleted() {
        MessageBox confirmMB = new MessageBox(SCProjectHelper.getShell(), SWT.ICON_ERROR);
        confirmMB.setMessage("Some elements where deleted and validation problem can not be fixed. All other validation problems for the element will be deleted.");
        confirmMB.setText("Validation");
        confirmMB.open();
    }

    private void resolveProblemWhenElementsDeleted(IMarker marker) throws CoreException {
        showMessageBoxElementsDeleted();
        ValidationMarkerFactory.clearMarkersForElement(marker);
        marker.delete();
    }
}
