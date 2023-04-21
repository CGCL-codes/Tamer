package nakayo.gameserver.network.loginserver.serverpackets;

import nakayo.gameserver.network.loginserver.LoginServerConnection;
import nakayo.gameserver.network.loginserver.LsServerPacket;
import java.nio.ByteBuffer;

/**
 * @author Aionchs-Wylovech
 */
public class SM_LS_CONTROL extends LsServerPacket {

    private final String accountName;

    private final String adminName;

    private final String playerName;

    private final int param;

    private final int type;

    public SM_LS_CONTROL(String accountName, String playerName, String adminName, int param, int type) {
        super(0x05);
        this.accountName = accountName;
        this.param = param;
        this.playerName = playerName;
        this.adminName = adminName;
        this.type = type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeImpl(LoginServerConnection con, ByteBuffer buf) {
        writeC(buf, getOpcode());
        writeC(buf, type);
        writeS(buf, adminName);
        writeS(buf, accountName);
        writeS(buf, playerName);
        writeC(buf, param);
    }
}
