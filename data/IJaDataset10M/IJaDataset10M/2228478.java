package org.xmlhammer.gui.util.wizard;

import java.awt.LayoutManager;
import org.bounce.wizard.WizardPage;

public abstract class HelpEnabledWizardPage extends WizardPage {

    private String helpID = null;

    public HelpEnabledWizardPage(LayoutManager manager, String helpID) {
        super(manager);
        this.helpID = helpID;
    }

    public final String getHelpID() {
        return helpID;
    }
}
