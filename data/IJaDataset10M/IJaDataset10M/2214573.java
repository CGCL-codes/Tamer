package net.sf.doolin.gui.wizard.model;

/**
 * Representation of the navigation possibilities of a wizard step.
 * 
 * @author Damien Coraboeuf
 * @version $Id: WizardNavigationState.java,v 1.1 2007/08/07 16:47:11 guinnessman Exp $
 */
public class WizardNavigationState {

    private boolean previousPossible;

    private boolean nextPossible;

    private boolean finishPossible;

    /**
	 * @return Returns the finishPossible.
	 */
    public boolean isFinishPossible() {
        return finishPossible;
    }

    /**
	 * @param finishPossible
	 *            The finishPossible to set.
	 */
    public void setFinishPossible(boolean finishPossible) {
        this.finishPossible = finishPossible;
    }

    /**
	 * @return Returns the nextPossible.
	 */
    public boolean isNextPossible() {
        return nextPossible;
    }

    /**
	 * @param nextPossible
	 *            The nextPossible to set.
	 */
    public void setNextPossible(boolean nextPossible) {
        this.nextPossible = nextPossible;
    }

    /**
	 * @return Returns the previousPossible.
	 */
    public boolean isPreviousPossible() {
        return previousPossible;
    }

    /**
	 * @param previousPossible
	 *            The previousPossible to set.
	 */
    public void setPreviousPossible(boolean previousPossible) {
        this.previousPossible = previousPossible;
    }
}
