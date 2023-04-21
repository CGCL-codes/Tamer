package nakayo.loginserver.network.aion.serverpackets;

import nakayo.loginserver.network.aion.AionConnection;
import nakayo.loginserver.network.aion.AionServerPacket;
import nakayo.loginserver.network.aion.SessionKey;
import java.nio.ByteBuffer;

/**
 * This packet is send to client to update sessionKey [for fast reconnection feature]
 *
 * @author -Nemesiss-
 */
public class SM_UPDATE_SESSION extends AionServerPacket {

    /**
     * accountId is part of session key - its used for security purposes
     */
    private final int accountId;

    /**
     * loginOk is part of session key - its used for security purposes
     */
    private final int loginOk;

    /**
     * Constructs new instance of <tt>SM_UPDATE_SESSION </tt> packet.
     *
     * @param key session key
     */
    public SM_UPDATE_SESSION(SessionKey key) {
        super(0x0c);
        this.accountId = key.accountId;
        this.loginOk = key.loginOk;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        writeC(buf, getOpcode());
        writeD(buf, accountId);
        writeD(buf, loginOk);
        writeC(buf, 0x00);
    }
}
