package org.waveprotocol.wave.client.wavepanel.view;

/**
 * Reveals the primitive state in the view of a thread.
 * 
 * @see ThreadView for structural state.
 */
public interface IntrinsicThreadView {

    /** Gets the unique id for this thread view. */
    String getId();

    /** Sets the total number of blips in the rendered thread. */
    void setTotalBlipCount(int totalBlipCount);

    /** Sets the number of unread blips in the thread. */
    void setUnreadBlipCount(int unreadBlipCount);
}
