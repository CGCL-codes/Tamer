package com.nokia.ats4.appmodel.main.event;

import com.nokia.ats4.appmodel.event.KendoEvent;

/**
 * SwitchPerspectiveEvent is passed to event queue when user wants to
 * change to another perspective.
 *
 * @author Kimmo Tuukkanen
 * @version $Revision: 2 $
 */
public class SwitchPerspectiveEvent extends KendoEvent {

    private int perspectiveIndex;

    /**
     * Creates a new instance of SwitchPerspectiveEvent
     *
     * @param source Source object from which this event originates
     * @param index Index of the perspective to bring visible
     */
    public SwitchPerspectiveEvent(Object source, int index) {
        super(source);
        this.perspectiveIndex = index;
    }

    /**
     * Get the index of the perspective to set visible.
     *
     * @return perspective index
     */
    public int getPerspectiveIndex() {
        return this.perspectiveIndex;
    }
}
