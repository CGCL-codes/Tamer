package org.limewire.mojito.messages;

/**
 * An interface for StatsResponse implementations
 */
public interface StatsResponse extends ResponseMessage {

    /**
     * Returns the remote Node's Statistics
     */
    public byte[] getStatistics();
}
