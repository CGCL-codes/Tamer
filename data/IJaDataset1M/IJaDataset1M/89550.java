package org.argouml.application.events;

/**
 * The Status Event is used to notify interested parties of a status
 * change.  The status can be arbitrary text, or the name of a project
 * which was saved or loaded.
 *
 * @author Tom Morris  <tfmorris@gmail.com>
 */
public class ArgoStatusEvent extends ArgoEvent {

    private String text;

    /**
     * @param eventType reported by this event.
     * @param src object that caused the event.
     * @param message the status text string (already translated) to be shown
     */
    public ArgoStatusEvent(int eventType, Object src, String message) {
        super(eventType, src);
        text = message;
    }

    /**
     * Indicates the start of the 100-digit range for status events.
     *
     * @return the first id reserved for events.
     */
    @Override
    public int getEventStartRange() {
        return ANY_STATUS_EVENT;
    }

    /**
     * @return Returns the event text containing either a status message or
     * the name of the project that was saved/loaded.
     */
    public String getText() {
        return text;
    }
}
