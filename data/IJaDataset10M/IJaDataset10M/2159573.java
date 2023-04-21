package org.gudy.azureus2.core3.tracker.server;

import java.util.Map;

/**
 * @author parg
 */
public interface TRTrackerServerPeer extends TRTrackerServerPeerBase {

    public static final byte NAT_CHECK_UNKNOWN = 0;

    public static final byte NAT_CHECK_DISABLED = 1;

    public static final byte NAT_CHECK_INITIATED = 2;

    public static final byte NAT_CHECK_OK = 3;

    public static final byte NAT_CHECK_FAILED = 4;

    public static final byte NAT_CHECK_FAILED_AND_REPORTED = 5;

    public static final byte CRYPTO_NONE = 0;

    public static final byte CRYPTO_SUPPORTED = 1;

    public static final byte CRYPTO_REQUIRED = 2;

    public long getUploaded();

    public long getDownloaded();

    public long getAmountLeft();

    public String getIPRaw();

    public byte[] getPeerID();

    /**
		 * returns the current NAT status of the peer
		 * @return
		 */
    public byte getNATStatus();

    public boolean isBiased();

    public void setBiased(boolean bias);

    public void setUserData(Object key, Object data);

    public Object getUserData(Object key);

    public Map export();
}
