package deprecated.navigator.actions;

import mySBML.Model;
import mySBML.SpeciesType;
import mySBML.lists.ListOfSpecies;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionDelegate;
import deprecated.SbmlProject;
import visualbiology.sbmlEditor.commands.SbmlAddCmd;
import visualbiology.wizards.SpeciesWizard;

public class NewSpeciesAction extends ActionDelegate {

    private Object selectedItem;

    @Override
    public void selectionChanged(IAction action, ISelection selection) {
        IStructuredSelection sel = (IStructuredSelection) selection;
        selectedItem = sel.getFirstElement();
    }

    @Override
    public void run(IAction action) {
        Model model = null;
        SpeciesWizard wizard = null;
        if (selectedItem instanceof ListOfSpecies) {
            model = ((ListOfSpecies) selectedItem).getModel();
            wizard = new SpeciesWizard(model);
        } else if (selectedItem instanceof SpeciesType) {
            model = ((SpeciesType) selectedItem).getModel();
            wizard = new SpeciesWizard((SpeciesType) selectedItem);
        } else if (selectedItem instanceof SbmlProject) {
            model = ((SbmlProject) selectedItem).getSbmlDocument().getModel();
            wizard = new SpeciesWizard(model);
        } else return;
        WizardDialog dialog = new WizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), wizard);
        dialog.create();
        dialog.setTitle("Species");
        dialog.setMessage("Create a new Species");
        if (dialog.open() == WizardDialog.OK) {
            SbmlAddCmd cmd = new SbmlAddCmd();
            cmd.setParent(model);
            cmd.setChild(wizard.getSpecies());
            cmd.execute();
        }
        model.notifyObservers();
    }
}
