package ch.javablog.mobile.trakkcor.controller.listener;

import java.util.Enumeration;

/**
 *
 * @author Heiko Maa�
 */
public interface TrackListListener {

    /**
     * Will be called if trackList was modified. 
     * @param ids an enumeration of Integer objects each representing a track id.
     */
    void trackListModified(Enumeration ids);
}
