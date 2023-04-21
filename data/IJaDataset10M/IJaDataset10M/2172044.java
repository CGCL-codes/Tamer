package org.gudy.azureus2.ui.swt.wizard;

/**
 * @author Olivier
 * 
 */
public abstract class AbstractWizardPanel<W extends Wizard> implements IWizardPanel {

    protected IWizardPanel previousPanel;

    protected W wizard;

    public AbstractWizardPanel(W wizard, IWizardPanel previousPanel) {
        this.previousPanel = previousPanel;
        this.wizard = wizard;
    }

    public boolean isPreviousEnabled() {
        return !(this.previousPanel == null);
    }

    public boolean isNextEnabled() {
        return false;
    }

    public boolean isFinishEnabled() {
        return false;
    }

    public IWizardPanel getPreviousPanel() {
        return previousPanel;
    }

    public IWizardPanel getNextPanel() {
        return null;
    }

    public IWizardPanel getFinishPanel() {
        return null;
    }

    public boolean isFinishSelectionOK() {
        return (true);
    }

    public void cancelled() {
    }

    public void finish() {
    }
}
