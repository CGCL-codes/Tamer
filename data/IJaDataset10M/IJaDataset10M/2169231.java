package net.sf.fmj.media.protocol;

/**
 * This interface allows a DataSource to notify its listener on the status of
 * the data flow in the buffers.
 */
public interface BufferListener {

    public void minThresholdReached(javax.media.protocol.DataSource ds);
}
