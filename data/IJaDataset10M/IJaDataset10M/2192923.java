package openbiomind.gui.wizards;

import openbiomind.gui.data.AbstractTaskData;

/**
 * The class UtilityComputerWizard.
 * 
 * @author bsanghvi
 * @since Jul 9, 2008
 * @version Aug 18, 2008
 */
public class UtilityComputerWizard extends AbstractTaskWizard {

    /** The utility computer wizard page. */
    private final UtilityComputerWizardPage UTILITY_COMPUTER_WIZ_PAGE = new UtilityComputerWizardPage();

    /**
    * Instantiates a new utility computer wizard.
    */
    public UtilityComputerWizard() {
        super(Messages.UtilCompWiz_Title);
    }

    @Override
    public void addPages() {
        addPage(this.UTILITY_COMPUTER_WIZ_PAGE);
    }

    @Override
    protected AbstractTaskData[] getTaskData() {
        return new AbstractTaskData[] { this.UTILITY_COMPUTER_WIZ_PAGE.prepareTaskData() };
    }

    @Override
    protected AbstractTaskWizardPage getFirstWizardPage() {
        return this.UTILITY_COMPUTER_WIZ_PAGE;
    }
}
