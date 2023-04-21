package org.jdesktop.animation.timing.triggers;

/**
 * Focus In/Out events
 *
 * @author Chet
 */
public class FocusTriggerEvent extends TriggerEvent {

    /**
     * Event fired when Component receives focus
     */
    public static final FocusTriggerEvent IN = new FocusTriggerEvent("FocusIn");

    /**
     * Event fired when Component loses focus
     */
    public static final FocusTriggerEvent OUT = new FocusTriggerEvent("FocusOut");

    /**
     * Private constructor; this helps ensure type-safe use of 
     * pre-defined TriggerEvent objects.
     */
    private FocusTriggerEvent(String name) {
        super(name);
    }

    /**
     * This method finds the opposite of the current event.: IN ->
     * OUT and OUT -> IN.
     */
    public TriggerEvent getOppositeEvent() {
        if (this == FocusTriggerEvent.IN) {
            return FocusTriggerEvent.OUT;
        } else {
            return FocusTriggerEvent.IN;
        }
    }
}

;
