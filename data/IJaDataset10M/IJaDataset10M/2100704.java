package com.ivis.xprocess.ui.actions.workflow;

import java.util.Collection;
import org.eclipse.jface.wizard.WizardDialog;
import com.ivis.xprocess.ui.actions.XProcessAction;
import com.ivis.xprocess.ui.util.ViewUtil;
import com.ivis.xprocess.ui.wizards.workflow.NewExternalEventTypeWizard;

public class NewExternalEventTypeAction extends XProcessAction {

    @Override
    public void doAction(Collection<Object> objects) {
        NewExternalEventTypeWizard externalEventTypeWizard = null;
        if (objects.size() == 1) {
            externalEventTypeWizard = new NewExternalEventTypeWizard(objects.toArray()[0]);
        }
        if (externalEventTypeWizard != null) {
            WizardDialog wizardDialog = new WizardDialog(ViewUtil.getCurrentShell(), externalEventTypeWizard);
            wizardDialog.open();
        }
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
