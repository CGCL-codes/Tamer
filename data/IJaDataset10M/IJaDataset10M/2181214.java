package org.gudy.azureus2.plugins.peers;

/**
 * @author parg
 *
 */
public interface PeerManagerStats {

    public int getConnectedSeeds();

    public int getConnectedLeechers();

    public long getDownloaded();

    public long getUploaded();

    public long getDownloadAverage();

    public long getUploadAverage();

    public long getDiscarded();

    public long getHashFailBytes();

    /**
	 * For an external process receiving bytes on behalf of this download this gives the current
	 * rate-limited number of bytes that can be received. Update with actual send using 'received' below.
	 * @since 4.4.0.7 
	 * @return
	 */
    public int getPermittedBytesToReceive();

    /**
	 * The given number of data (payload) bytes have been received.
	 * This number gets added to the total and is used to calculate the rate.
	 * <p>
	 * Use this if you are talking to stuff outside of Azureus' API, and
	 * want your stats added into Azureus'
	 * 
	 * @param bytes
	 * 
	 *@since 4.4.0.7 
	 */
    public void permittedReceiveBytesUsed(int bytes);

    /**
	 * For an external process sending bytes on behalf of this download this gives the current
	 * rate-limited number of bytes that can be sent. Update with actual send using 'sent' below.
	 * @since 4.4.0.7 
	 * @return
	 */
    public int getPermittedBytesToSend();

    /**
	 * The given number of data (payload) bytes have been sent.
	 * This number gets added to the total and is used to calculate the rate.
	 * <p>
	 * Use this if you are talking to stuff outside of Azureus' API, and
	 * want your stats added into Azureus'
	 * 
	 * @param bytes
	 * 
	 * @since 4.4.0.7
	 */
    public void permittedSendBytesUsed(int bytes);
}
