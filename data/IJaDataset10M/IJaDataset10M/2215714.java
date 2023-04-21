package org.gudy.azureus2.ui.swt.ipchecker;

import org.gudy.azureus2.core3.ipchecker.extipchecker.ExternalIPCheckerService;
import org.gudy.azureus2.ui.swt.wizard.IWizardPanel;
import org.gudy.azureus2.ui.swt.wizard.Wizard;

/**
 * @author Olivier
 *  
 */
public class IpCheckerWizard extends Wizard {

    IpSetterCallBack callBack;

    ExternalIPCheckerService selectedService;

    public IpCheckerWizard() {
        super("ipCheckerWizard.title");
        IWizardPanel panel = new ChooseServicePanel(this, null);
        this.setFirstPanel(panel);
    }

    public void setIpSetterCallBack(IpSetterCallBack callBack) {
        this.callBack = callBack;
    }
}
