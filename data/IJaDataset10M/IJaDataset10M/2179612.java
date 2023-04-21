package be.lassi.lanbox;

import be.lassi.lanbox.domain.ChannelChange;

public interface ChannelChangeProcessor {

    /**
     * Adds a channel change to the channel change queue.
     * 
     * @param change the channel change to be added
     */
    void change(final int layerId, final ChannelChange change);
}
