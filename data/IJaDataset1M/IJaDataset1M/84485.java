package net.sf.logsaw.ui.wizards.support;

/**
 * Interface for use by the extension point <code>net.sf.logsaw.ui.dialectWizardPage</code>.
 * 
 * @author Philipp Nanz
 */
public interface ILogResourceWizardPageFactory {

    /**
	 * Constructs a new wizard page instance.
	 * @return the newly constructed wizard page
	 */
    ILogResourceWizardPage newWizardPage();
}
