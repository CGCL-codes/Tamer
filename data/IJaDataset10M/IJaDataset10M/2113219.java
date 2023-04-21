package org.tigr.cloe.model.edit;

public interface EditCommand {

    /**
     * Execute the command.
     * @throws CommandFailedException if there was a problem executing the command.
     *
     */
    public void execute() throws CommandFailedException;

    /**
     * Undo the command.
     * Does not throw any exceptions because it
     * is assumed that the previous state is valid.
     *
     */
    public void undo();

    /**
     * Notify any and all listeners that
     * something has changed.
     *
     */
    public void notifyChangeListeners();
}
