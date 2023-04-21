package com.ivis.xprocess.ui.actions.workflow;

import java.util.Collection;
import org.eclipse.jface.wizard.WizardDialog;
import com.ivis.xprocess.ui.actions.XProcessAction;
import com.ivis.xprocess.ui.util.ViewUtil;
import com.ivis.xprocess.ui.wizards.workflow.NewStateSetWizard;

public class NewStateSetAction extends XProcessAction {

    @Override
    public void doAction(Collection<Object> objects) {
        NewStateSetWizard stateSetWizard = null;
        if (objects.size() == 1) {
            stateSetWizard = new NewStateSetWizard(objects.toArray()[0]);
        }
        if (stateSetWizard != null) {
            WizardDialog wizardDialog = new WizardDialog(ViewUtil.getCurrentShell(), stateSetWizard);
            wizardDialog.open();
        }
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
