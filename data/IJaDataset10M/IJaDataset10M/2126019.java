package com.hexapixel.widgets.ganttchart;

import java.util.List;
import org.eclipse.swt.events.MouseEvent;

public class GanttEventListenerAdapter implements IGanttEventListener {

    public void eventDoubleClicked(GanttEvent event, MouseEvent me) {
    }

    public void eventPropertiesSelected(GanttEvent event) {
    }

    public void eventsDeleteRequest(List<GanttEvent> events, MouseEvent me) {
    }

    public void eventSelected(GanttEvent event, List<GanttEvent> allSelectedEvents, MouseEvent me) {
    }

    public void eventsMoved(List<GanttEvent> events, MouseEvent me) {
    }

    public void eventsResized(List<GanttEvent> events, MouseEvent me) {
    }

    public void zoomedIn(int newZoomLevel) {
    }

    public void zoomedOut(int newZoomLevel) {
    }

    public void zoomReset() {
    }
}
