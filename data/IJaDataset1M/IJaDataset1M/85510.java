package org.tockit.canvas.events;

import org.tockit.canvas.Canvas;
import org.tockit.events.Event;

/**
 * This event is send after a canvas is completely redrawn.
 * 
 * This is useful for animations and user feedback.
 */
public class CanvasDrawnEvent implements Event<Canvas> {

    private Canvas subject;

    public CanvasDrawnEvent(Canvas subject) {
        this.subject = subject;
    }

    public Canvas getSubject() {
        return subject;
    }

    /**
     * Same effect as calling getSubject().
     */
    @Deprecated
    public Canvas getCanvas() {
        return subject;
    }
}
