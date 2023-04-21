package gnu.java.zrtp.packets;

import gnu.java.zrtp.ZrtpConstants;
import gnu.java.zrtp.utils.ZrtpUtils;

/**
 * @author Werner Dittmann <Werner.Dittmann@t-online.de>
 *
 */
public class ZrtpPacketPing extends ZrtpPacketBase {

    private static final int ZRTP_PING_LENGTH = 3;

    private static final int VERSION_OFFSET = ZRTP_HEADER_LENGTH * ZRTP_WORD_SIZE;

    private static final int EP_OFFSET = VERSION_OFFSET + ZRTP_WORD_SIZE;

    private static final int PING_LENGTH = (ZRTP_HEADER_LENGTH + ZRTP_PING_LENGTH) * ZRTP_WORD_SIZE + CRC_SIZE;

    /**
     * Constructor for a new Ping message - currently not used for normal clients.
     * 
     *
     */
    protected ZrtpPacketPing() {
        super(new byte[PING_LENGTH]);
        setZrtpId();
        setVersion(ZrtpConstants.zrtpVersion);
        setLength(ZRTP_HEADER_LENGTH + ZRTP_PING_LENGTH);
        setMessageType(ZrtpConstants.PingMsg);
    }

    /**
     * Constructor for Ping message initialized with received data.
     * 
     * @param data received from the network.
     */
    public ZrtpPacketPing(final byte[] data) {
        super(data);
    }

    /**
     * Get the endpoint hash from Ping packet.
     * 
     * @return the endpoint hash.
     */
    public final byte[] getEpHash() {
        return ZrtpUtils.readRegion(packetBuffer, EP_OFFSET, 2 * ZRTP_WORD_SIZE);
    }

    /**
     * Set the endpoint hash.
     * 
     * @return the endpoint hash.
     */
    protected final void setEpHash(final byte[] data) {
        System.arraycopy(data, 0, packetBuffer, EP_OFFSET, 2 * ZRTP_WORD_SIZE);
    }

    private final void setVersion(final byte[] data) {
        System.arraycopy(data, 0, packetBuffer, VERSION_OFFSET, ZRTP_WORD_SIZE);
    }
}
