package com.continuent.tungsten.manager.fsm.event;

import com.continuent.tungsten.commons.patterns.fsm.Event;

/**
 * Signals that the manager should join the cluster, comprised of other
 * managers.
 */
public class JoinedClusterEvent extends Event {

    public JoinedClusterEvent() {
        super(null);
    }
}
