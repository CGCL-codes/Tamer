package com.shimari.bot;

/**
 * A channel is an abstraction of something that can receive
 * a message.
 */
public interface Channel {

    /**
     * Send a notice to everyone on the channel
     */
    public void broadcastNotice(String notice);

    /**
     * Send a message to everyone on the channel
     */
    public void broadcastMessage(String message);
}
