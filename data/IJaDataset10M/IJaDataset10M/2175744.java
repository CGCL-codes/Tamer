package org.neuroph.netbeans.ide.project.type;

import org.openide.WizardDescriptor;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * Panel just asking for basic info.
 */
public class NeurophProjectTemplateWizardPanelTCR extends NeurophProjectTemplateWizardPanel implements WizardDescriptor.Panel, WizardDescriptor.ValidatingPanel, WizardDescriptor.FinishablePanel {

    @Override
    public boolean isFinishPanel() {
        return true;
    }
}
