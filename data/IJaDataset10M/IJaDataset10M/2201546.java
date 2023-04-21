package skycastle.application.tool;

import skycastle.application.input.events.ButtonEvent;
import skycastle.application.input.events.AxisEvent;

/**
 * Describes some kind of input event done by the user to a view, which is then sent to a tool for processing.
 *
 * The event has a location, and possibly various button and other input states associated with it.
 *
 * The location is projected by the view to a document location, although the original view coordinates are also provided.
 *
 * The goal is that this event class could be used also to process the input from devices such as tablets with high
 * resolution and pressure sensitive input.
 *
 * @author Hans H�ggstr�m
 */
public interface ToolInputEvent extends ButtonEvent, AxisEvent {

    /**
     * @return the x location of the event projected to the document coordinate space.
     */
    double getDocumentX();

    /**
     * @return the y location of the event projected to the document coordinate space.
     */
    double getDocumentY();

    /**
     * @return the x location of the event in the view coordinate space.
     */
    double getViewX();

    /**
     * @return the y location of the event in the view coordinate space.
     */
    double getViewY();
}
