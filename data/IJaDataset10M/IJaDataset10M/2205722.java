package gameserver.network.loginserver.serverpackets;

import gameserver.configs.IPConfig;
import gameserver.configs.NetworkConfig;
import gameserver.network.loginserver.LoginServerConnection;
import gameserver.network.loginserver.LsServerPacket;
import java.nio.ByteBuffer;
import java.util.List;
import commons.network.IPRange;

/**
 * This is authentication packet that gs will send to login server for registration.
 */
public class SM_GS_AUTH extends LsServerPacket {

    /**
	* Constructs new instance of <tt>SM_GS_AUTH </tt> packet.
	* 
	*/
    public SM_GS_AUTH() {
        super(0x00);
    }

    /**
	* {@inheritDoc}
	*/
    @Override
    protected void writeImpl(LoginServerConnection con, ByteBuffer buf) {
        writeC(buf, getOpcode());
        writeC(buf, NetworkConfig.GAMESERVER_ID);
        writeC(buf, IPConfig.getDefaultAddress().length);
        writeB(buf, IPConfig.getDefaultAddress());
        List<IPRange> ranges = IPConfig.getRanges();
        int size = ranges.size();
        writeD(buf, size);
        for (int i = 0; i < size; i++) {
            IPRange ipRange = ranges.get(i);
            byte[] min = ipRange.getMinAsByteArray();
            byte[] max = ipRange.getMaxAsByteArray();
            writeC(buf, min.length);
            writeB(buf, min);
            writeC(buf, max.length);
            writeB(buf, max);
            writeC(buf, ipRange.getAddress().length);
            writeB(buf, ipRange.getAddress());
        }
        writeH(buf, NetworkConfig.GAME_PORT);
        writeD(buf, NetworkConfig.MAX_ONLINE_PLAYERS);
        writeD(buf, NetworkConfig.REQUIRED_ACCESS);
        writeS(buf, NetworkConfig.LOGIN_PASSWORD);
    }
}
