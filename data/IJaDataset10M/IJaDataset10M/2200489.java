package ingenias.installergenerator.view.commands;

import ingenias.installergenerator.view.RequestSelectDeploymentView;
import ingenias.installergenerator.view.UpdateCommand;
import ingenias.installergenerator.view.WizardWindow;
import java.awt.BorderLayout;
import java.util.List;

/**
 *
 * @author Carlos
 */
class DeploymentsVC extends UpdateCommand {

    private WizardWindow wizardWindow;

    public DeploymentsVC(WizardWindow wizardWindow) {
        this.wizardWindow = wizardWindow;
    }

    @Override
    public void execute() {
        RequestSelectDeploymentView view = new RequestSelectDeploymentView();
        view.setDeployments((List<String>) this.getData());
        this.wizardWindow.setListener(view);
        this.wizardWindow.getWizardContentPanel().removeAll();
        this.wizardWindow.getWizardContentPanel().add(view, BorderLayout.CENTER);
        this.wizardWindow.getWizardContentPanel().updateUI();
        this.wizardWindow.getNextButton().setEnabled(true);
        this.wizardWindow.getNextButton().setText("Generate");
        this.wizardWindow.getCancelButton().setEnabled(true);
    }
}
