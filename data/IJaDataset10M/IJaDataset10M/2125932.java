package com.hp.hpl.guess.action;

public abstract class GAction {

    /**
	 * The description of the action
	 */
    private String actionDescription = null;

    /**
	 * Set the description for the action
	 * @param aDescription
	 */
    public void setDescription(String aDescription) {
        actionDescription = aDescription;
    }

    /**
	 * Get the description of the action
	 * @return
	 */
    public String getDescription() {
        return actionDescription;
    }

    /**
	 * Executes the action
	 */
    public void run() {
        actionContent();
    }

    /**
	 * The action to run
	 */
    protected abstract void actionContent();

    /**
	 * Undo the action and return a redo action
	 */
    public abstract GAction getUndoAction();

    /**
	 * How to delete the action if it is
	 * not needed anymore
	 */
    public abstract void dispose();
}
