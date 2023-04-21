package de.matthiasmann.twl;

/**
 *
 * @author Matthias Mann
 */
public interface InputMap {

    /**
     * Maps the given key event to an action.
     * @param event the key event
     * @return the action or null if no mapping was found
     */
    public String mapEvent(Event event);
}
