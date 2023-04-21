package ro.codemart.installer.wizard;

import ro.codemart.installer.wizard.exception.WizardException;
import ro.codemart.installer.core.InstallerException;

/**
 * Defines the behaviour for any button defined.
 * To add custom buttons to your installer, this is the interface your "custom-button" class has to implement
 */
public interface WizardButtonAction {

    /**
     * Defines what to do when the button is pressed
     *
     * @param wizard wizard runner to get useful data from. E.g. installer context
     * @throws InstallerException if any error occurs
     */
    public void execute(WizardRunner wizard) throws InstallerException;
}
