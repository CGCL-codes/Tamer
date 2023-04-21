package org.simulare;

import java.util.*;

/**
 * The event on clock notifications.
 */
public class ClockEvent extends EventObject {

    /** The clock counter. */
    public long counter;

    /** Indicate that the event was generated after clock has stoped. */
    public boolean stoped;

    public ClockEvent(Object source) {
        super(source);
    }
}
