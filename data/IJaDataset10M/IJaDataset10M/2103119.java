package org.pandcorps.pandam.event.action;

import org.pandcorps.pandam.Panput;

public final class ActionStartEvent extends InputEvent {

    public static final ActionStartEvent getEvent(final Panput input) {
        return new ActionStartEvent(input);
    }

    private ActionStartEvent(final Panput input) {
        super(input);
    }
}
