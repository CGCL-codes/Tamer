package edu.hawaii.ics.ami.event.model;

import edu.hawaii.ics.ami.element.model.module.SourceModule;

/**
 * This message event object contains a message.
 *
 * @author   king
 * @since    July 16, 2002
 */
public class MessageEvent extends DataEvent {

    /** serial id to be compatible with older versions */
    private static final long serialVersionUID = 0L;

    /** The message of this object. */
    private String message;

    /**
   * Creates a new MessageEvent object.
   *
   * @param sourceModule  The module which created the event.
   * @param timeStamp     The time in milliseconds when the event got created.
   * @param name          The name of the DataEvent.
   * @param message       The message of this event.
   */
    public MessageEvent(SourceModule sourceModule, long timeStamp, String name, String message) {
        super(sourceModule, timeStamp, name);
        this.message = message;
    }

    /**
   * Creates a new MessageEvent object. Time will be assigned automatically.
   *
   * @param sourceModule  The module which created the event.
   * @param name          The name of the DataEvent.
   * @param message       The message of this event.
   */
    public MessageEvent(SourceModule sourceModule, String name, String message) {
        super(sourceModule, name);
        this.message = message;
    }

    /**
   * Gets the message contained in this event object.
   *
   * @return   The contained text message.
   */
    public String getMessage() {
        return message;
    }

    /**
   * Sets a message to this event object.
   *
   * @param message  The new message to set.
   */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
   * Returns the values stored in the event as text. E.g. (x, y) data for
   * current mouse position.
   *
   * @return   The value of the event as text.
   */
    public String getData() {
        return message;
    }
}
