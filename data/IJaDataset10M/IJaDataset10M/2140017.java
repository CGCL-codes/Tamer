package uk.ac.essex.common.gui.panel;

/**
 * An interface for objects that gain and loss focus.  This interface is
 *  defined to process the focus-related events and get the focus status of
 *  the object.
 */
public interface Focusable {

    /** Process focus-related events. */
    void setFocused(boolean b);

    /** Get the focus status. */
    boolean isFocused();
}
