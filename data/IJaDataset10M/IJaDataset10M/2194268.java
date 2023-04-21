package com.volantis.mcs.interaction.event;

import com.volantis.mcs.interaction.ListProxy;

/**
 * The base of all list related events.
 */
public abstract class ListEvent extends InteractionEvent {

    /**
     * Initialise.
     *
     * @param source The list proxy that was the source of the event.
     * @param originator Indicats if this event is the originator.
     */
    public ListEvent(ListProxy source, boolean originator) {
        super(source, originator);
    }

    /**
     * Get the list proxy that was the source of the event.
     *
     * @return The list proxy that was the source of the event.
     */
    public ListProxy getListProxy() {
        return (ListProxy) getSource();
    }
}
