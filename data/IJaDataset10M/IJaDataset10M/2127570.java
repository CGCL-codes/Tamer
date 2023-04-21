package nakayo.loginserver.network.aion.serverpackets;

import nakayo.loginserver.network.aion.AionAuthResponse;
import nakayo.loginserver.network.aion.AionConnection;
import nakayo.loginserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;

/**
 * @author KID
 */
public class SM_LOGIN_FAIL extends AionServerPacket {

    /**
     * response - why login fail
     */
    private AionAuthResponse response;

    /**
     * Constructs new instance of <tt>SM_LOGIN_FAIL</tt> packet.
     *
     * @param response auth responce
     */
    public SM_LOGIN_FAIL(AionAuthResponse response) {
        super(0x01);
        this.response = response;
    }

    /**
     * {@inheritDoc}
     */
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        writeC(buf, getOpcode());
        writeD(buf, response.getMessageId());
    }
}
