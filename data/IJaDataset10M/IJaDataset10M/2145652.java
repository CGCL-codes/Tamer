package com.realtime.crossfire.jxclient.quests;

import java.util.EventListener;

/**
 * Interface for listeners interested in {@link QuestsManager} events.
 * @author Nicolas Weeger
 */
public interface QuestsManagerListener extends EventListener {

    /**
     * A new quest.
     * @param index the current index of <code>quest</code> in the quests
     * manager
     */
    void questAdded(int index);
}
