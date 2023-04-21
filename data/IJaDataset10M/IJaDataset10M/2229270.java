package org.xaware.ide.xadev.wizard.bizview;

import org.eclipse.ui.wizards.newresource.BasicNewFileResourceWizard;
import org.xaware.common.AdvisorFactory;
import org.xaware.common.exception.XAwareAdvisorException;
import org.xaware.ide.xadev.wizard.BizComponentWizardHelper;
import org.xaware.shared.util.XAwareException;

/**
 * Base class for BizView wizards.
 * 
 * @author satishk
 * 
 */
public abstract class BasicBizViewSetupWizard extends BasicNewFileResourceWizard {

    /**
     * Default constructor.
     */
    public BasicBizViewSetupWizard() throws XAwareException {
        try {
            AdvisorFactory.getAdvisor(getAdvisorComponentName());
        } catch (XAwareAdvisorException exception) {
            throw new XAwareException(exception.getMessage(), exception);
        }
    }

    @Override
    public boolean performFinish() {
        if (BizComponentWizardHelper.isLaunchedUsingHelper()) {
            BizComponentWizardHelper.setDefinitionName(getBizViewDefinitionName());
            return true;
        } else {
            BizComponentWizardHelper.launchStandAloneLegacyWizard(getBizViewDefinitionName());
        }
        return true;
    }

    /**
     * Returns the BizView definition name.
     * 
     * @return Returns the BizView definition name.
     */
    protected abstract String getBizViewDefinitionName();

    /**
     * Returns the component name for getting the advisor. 
     * 
     * @return Advisor Component name.
     */
    protected abstract String getAdvisorComponentName();

    /**
     * Sets the name of the file to use .
     * 
     * @param fileNameToUse
     */
    public void setFileNameToUse(String fileNameToUse) {
    }
}
