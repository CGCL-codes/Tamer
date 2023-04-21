package org.ln.millesimus.gui.wizard.importing;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.ln.swingy.wizard.WizardPanelDescriptor;

/**
 * @author luke
 *
 */
public class ExportFirstStepDescriptor extends WizardPanelDescriptor implements PropertyChangeListener {

    protected static final String IDENTIFIER = "FIRST_PANEL";

    private ExportFirstStepPanel panel;

    /**
	 * 
	 */
    public ExportFirstStepDescriptor() {
        panel = new ExportFirstStepPanel();
        panel.addPropertyChangeListener("inputOk", this);
        setPanelDescriptorIdentifier(IDENTIFIER);
        setPanelComponent(panel);
    }

    @Override
    public void beforeDisplayPanel() {
        getWizard().setNextButtonEnabled(panel.getExportType() > -1);
    }

    @Override
    public void aboutToHidePanel() {
    }

    @Override
    public Object getNextPanelDescriptor() {
        if (panel.getExportType() == 0) return ExportTextPriceListDescriptor.IDENTIFIER;
        return ExportTextPriceListDescriptor.IDENTIFIER;
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("inputOk")) {
            getWizard().setNextButtonEnabled((Boolean) evt.getNewValue());
        }
    }
}
