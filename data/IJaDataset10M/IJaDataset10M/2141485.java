package jsesh.mdcDisplayer.swing.application.actions;

import java.awt.event.ActionEvent;
import jsesh.mdcDisplayer.swing.application.MDCDisplayerAppliWorkflow;
import jsesh.mdcDisplayer.swing.application.actions.generic.BasicAction;

public class EditPreferencesAction extends BasicAction {

    public EditPreferencesAction(String name, MDCDisplayerAppliWorkflow workflow) {
        super(name, workflow);
    }

    public void actionPerformed(ActionEvent e) {
        workflow.editPreferences();
    }
}
