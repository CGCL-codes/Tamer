package com.inetmon.jn.statistic.application.views;

import org.eclipse.jface.wizard.Wizard;

/**
 * SearchEntryWizardPage
 * @author   inetmon
 */
public class SetLineSpeedWizard extends Wizard {

    private SetLineSpeedPage sPage;

    /**
	 * SearchEntryWizard constructor
	 */
    public SetLineSpeedWizard() {
        sPage = new SetLineSpeedPage();
        addPage(sPage);
        setWindowTitle("Set Line Speed");
    }

    /**
	 * Called when user clicks Finish 
	 */
    public boolean performFinish() {
        return true;
    }
}
