package org.gameplate.event;

import java.util.*;

/**
 * Describe class <code>MoveEvent</code> here.
 *
 * @author <a href="mailto:kleiba@dfki.de">Thomas Kleinbauer</a>
 * @version 1.0
 */
public class MoveEvent extends EventObject {

    public static final int MOVE_STARTED = 1;

    public static final int MOVE_FINISHED = 2;

    public static final int MOVE_ABORTED = 3;

    private int type;

    public MoveEvent(Object source, int type) {
        super(source);
        if (type != MOVE_STARTED && type != MOVE_FINISHED && type != MOVE_ABORTED) {
            throw new IllegalArgumentException("Parameter 'type' must be one of " + "MOVE_STARTED, MOVE_FINISHED, MOVE_ABORTED.");
        }
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
