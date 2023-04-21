package de.sopra.controller;

/**
 * An Interface for generalisation of handlers which contains some basic methods
 * all handlers have in common.
 * Mostly used for perspective changing.
 * 
 * @author Reinhold Rumberger
 */
public interface HandlerInterface {

    /**
     * Clean up the perspective after leaving it.
     */
    public void cleanup();

    /**
     * Asks the handler if it allows to exit the current perspective.
     * 
     * @return <code>true</code> if perspective may be changed.
     */
    public boolean allowExit();

    /**
     * Asks the handler if it allows to exit the program.
     * 
     * @return <code>true</code> if perspective may be changed.
     */
    public boolean allowProgramClose();

    /**
     * Prepare the perspective before switching to it.
     */
    public void prepareForDisplay();
}
