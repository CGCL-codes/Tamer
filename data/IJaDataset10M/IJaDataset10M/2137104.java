package gnu.java.zrtp.packets;

import gnu.java.zrtp.ZrtpConstants;

/**
 * @author Werner Dittmann <Werner.Dittmann@t-online.de>
 *
 */
public class ZrtpPacketConf2Ack extends ZrtpPacketBase {

    /**
     * Hello ack does not have any additional fields, just the header.
     */
    private static final int CONF2_ACK_LENGTH = ZRTP_HEADER_LENGTH * ZRTP_WORD_SIZE + CRC_SIZE;

    /**
     * Constructor for a new ErrorAck message.
     * 
     * ErrorAck does not have any specific fields, it is only
     * a simple message.
     *
     */
    public ZrtpPacketConf2Ack() {
        super(new byte[CONF2_ACK_LENGTH]);
        setZrtpId();
        setLength(ZRTP_HEADER_LENGTH);
        setMessageType(ZrtpConstants.Conf2AckMsg);
    }

    /**
     * Constructor for ErrorAck message initialized with received data.
     * 
     * @param data received from the network.
     */
    public ZrtpPacketConf2Ack(final byte[] data) {
        super(data);
    }
}
