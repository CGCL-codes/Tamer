package eu.future.earth.gwt.client.date.week.staend;

import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.allen_sauer.gwt.dnd.client.VetoDragException;

/**
 * Shared drag handler which display events as they are received by the various
 * drag controllers.
 */
public final class DayDragHandler implements DragHandler {

    private static final String BLUE = "#4444BB";

    private static final String GREEN = "#44BB44";

    private static final String RED = "#BB4444";

    public DayDragHandler() {
    }

    public void onDragEnd(DragEndEvent event) {
        log("onDragEnd: " + event, RED);
    }

    public void onDragStart(DragStartEvent event) {
        log("onDragStart: " + event, GREEN);
    }

    public void onPreviewDragEnd(DragEndEvent event) throws VetoDragException {
        log("<br>onPreviewDragEnd: " + event, BLUE);
    }

    public void onPreviewDragStart(DragStartEvent event) throws VetoDragException {
        clear();
        log("onPreviewDragStart: " + event, BLUE);
    }

    private void clear() {
    }

    private void log(String text, String color) {
    }
}
