package com.bbn.vessel.author.util.wizard;

/**
 * a page that delivers a message then forces the user to hit back or cancel
 * @author jostwald
 *
 */
public class DeadEndPage extends MessagePage {

    /**
     * @param title title for the window
     * @param wizard the wizard for this page
     * @param message message to be delivered
     */
    public DeadEndPage(String title, Wizard wizard, String message) {
        super(title, wizard, message);
    }

    @Override
    public boolean isCompleted() {
        return false;
    }

    @Override
    public void clearMapFields(WizardState state) {
        super.clearMapFields(state);
        wizard.removePage(this);
    }
}
