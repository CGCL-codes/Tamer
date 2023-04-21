package ch.oblivion.comixviewer.rcpplugin.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.handlers.HandlerUtil;
import ch.oblivion.comixviewer.engine.model.ComixException;
import ch.oblivion.comixviewer.engine.model.ComixManager;
import ch.oblivion.comixviewer.engine.model.ComixProfile;
import ch.oblivion.comixviewer.rcpplugin.Activator;
import ch.oblivion.comixviewer.rcpplugin.wizard.ProfileWizard;

public class ShowProfileWizard extends AbstractHandler {

    private final String USE_SELECTION_PARAMETER = "useSelection";

    private final Boolean defaultParameterValue = true;

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        ComixProfile profile = null;
        ComixManager manager = Activator.getDefault().getComixManager();
        boolean useSelection = defaultParameterValue;
        String useSelectionParameter = event.getParameter(USE_SELECTION_PARAMETER);
        if (useSelectionParameter != null) {
            useSelection = Boolean.parseBoolean(event.getParameter(USE_SELECTION_PARAMETER));
        }
        if (useSelection) {
            ISelection currentSelection = HandlerUtil.getCurrentSelection(event);
            if (currentSelection instanceof IStructuredSelection) {
                IStructuredSelection structuredSelection = (IStructuredSelection) currentSelection;
                if (!structuredSelection.isEmpty()) {
                    Object firstElement = structuredSelection.getFirstElement();
                    if (firstElement instanceof ComixProfile) {
                        profile = (ComixProfile) firstElement;
                    }
                }
            }
        }
        try {
            if (profile == null) {
                profile = manager.createProfile("New profile");
            }
            ProfileWizard wizard = new ProfileWizard(manager, profile);
            WizardDialog dialog = new WizardDialog(HandlerUtil.getActiveShell(event), wizard);
            dialog.open();
        } catch (ComixException e) {
            Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Could not create profile.", e));
        }
        return null;
    }
}
