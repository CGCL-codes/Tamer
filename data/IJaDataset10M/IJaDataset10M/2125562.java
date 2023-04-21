package com.ek.mitapp.ui.wizard;

import java.util.Map;
import org.netbeans.spi.wizard.WizardPanelProvider;
import com.ek.mitapp.system.Configuration;

/**
 * TODO: Class description.
 * <br>
 * Id: $Id: /cvsroot/jhomenet/files/src/jhomenet/ $
 * 
 * @author dirwin
 */
public class AccidentDataDescriptor implements IWizardDescriptor {

    /**
     * Define the available steps.
     */
    private AccidentDataSteps accidentDataSteps;

    /**
     * Reference to the application settings.
     */
    private final Configuration configuration;

    /**
     * @param configuration
     */
    public AccidentDataDescriptor(Configuration configuration) {
        super();
        this.configuration = configuration;
    }

    /** 
     * @see com.ek.mitapp.ui.wizard.IWizardDescriptor#getInitialSteps()
     */
    public WizardPanelProvider getInitialSteps() {
        return new AccidentDataInitialSteps(configuration);
    }

    /** 
     * @see com.ek.mitapp.ui.wizard.IWizardDescriptor#getPanelProviderForStep(java.lang.String, java.util.Map)
     */
    public WizardPanelProvider getPanelProviderForStep(String step, Map collectedData) {
        return getAccidentDataSteps();
    }

    /**
     * 
     * 
     * @return A reference to a newly created <code>WizardPanelProvider</code>
     */
    private WizardPanelProvider getAccidentDataSteps() {
        if (accidentDataSteps == null) accidentDataSteps = new AccidentDataSteps(configuration);
        return accidentDataSteps;
    }
}
