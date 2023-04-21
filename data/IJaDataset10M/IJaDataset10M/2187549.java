package org.ln.millesimus.gui.wizard;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.ln.millesimus.vo.PriceFamily;
import org.ln.swingy.wizard.Finishable;
import org.ln.swingy.wizard.WizardPanelDescriptor;

/**
 * @author luke
 *
 */
public class SavePriceFamily3Descriptor extends WizardPanelDescriptor implements Finishable, PropertyChangeListener {

    protected static final String IDENTIFIER = "THIRD_PANEL";

    private SavePriceFamily3Panel panel;

    /**
	 * 
	 */
    public SavePriceFamily3Descriptor() {
        panel = new SavePriceFamily3Panel();
        setPanelDescriptorIdentifier(IDENTIFIER);
        setPanelComponent(panel);
        panel.addPropertyChangeListener("saved", this);
    }

    @Override
    public void beforeDisplayPanel() {
        PriceFamily v = (PriceFamily) getWizardModel().getDataMap().get(SavePriceFamilyWizard.PRICE_FAMILY_KEY);
        panel.setPriceFamily(v);
        panel.updateView();
        getWizard().setFinishButtonEnabled(false);
    }

    @Override
    public void aboutToHidePanel() {
        putData(SavePriceFamilyWizard.PRICE_FAMILY_KEY, panel.getPriceFamily());
    }

    @Override
    public Object getNextPanelDescriptor() {
        return null;
    }

    public Object getBackPanelDescriptor() {
        return SavePriceFamily2Descriptor.IDENTIFIER;
    }

    public void onFinish() {
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("saved")) {
            getWizard().setFinishButtonEnabled(!(Boolean) evt.getNewValue());
            getWizard().setBackButtonEnabled((Boolean) evt.getNewValue());
            getWizard().setCancelButtonEnabled((Boolean) evt.getNewValue());
        }
    }
}
