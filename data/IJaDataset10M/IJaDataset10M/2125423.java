package com.realtime.crossfire.jxclient.map;

import java.util.EventListener;

/**
 * Interface for listeners interested on map scrolled events.
 * @author Lauwenmark
 */
public interface MapScrollListener extends EventListener {

    /**
     * The map contents have scrolled.
     * @param dx the x-distance
     * @param dy the y-distance
     */
    void mapScrolled(int dx, int dy);
}
