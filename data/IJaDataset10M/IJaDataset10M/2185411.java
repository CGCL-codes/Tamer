package uk.ac.lkl.migen.system.expresser.ui.uievent;

/**
 * Represents the end of a drag of a tied number
 * 
 * 
 * @author Ken Kahn
 *
 */
public class TiedNumberDragEndEvent extends DragEndEvent {

    public TiedNumberDragEndEvent(String tiedNumberPanelId, String canvasId) {
        super(tiedNumberPanelId, canvasId);
    }

    @Override
    public String logMessage() {
        return "Drag ended " + super.logMessage() + " (" + getTiedNumberPanelId() + ")";
    }

    public String getTiedNumberPanelId() {
        return getSource();
    }
}
