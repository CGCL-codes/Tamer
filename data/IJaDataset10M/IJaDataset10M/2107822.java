package gameserver.network.loginserver.serverpackets;

import gameserver.network.loginserver.LoginServerConnection;
import gameserver.network.loginserver.LsServerPacket;
import java.nio.ByteBuffer;

/**
 * The universal packet for account/IP bans
 */
public class SM_BAN extends LsServerPacket {

    /**
	* Ban type
	*  1 = account
	*  2 = IP
	*  3 = Full ban (account and IP)
	*/
    private final byte type;

    /**
	* Account to ban
	*/
    private final int accountId;

    /**
	* IP or mask to ban
	*/
    private final String ip;

    /**
	* Time in minutes. 0 = infinity;
	* If time < 0 then it's unban command
	*/
    private final int time;

    /**
	* Object ID of Admin, who request the ban
	*/
    private final int adminObjId;

    public SM_BAN(byte type, int accountId, String ip, int time, int adminObjId) {
        super(0x06);
        this.type = type;
        this.accountId = accountId;
        this.ip = ip;
        this.time = time;
        this.adminObjId = adminObjId;
    }

    /**
	* {@inheritDoc}
	*/
    @Override
    protected void writeImpl(LoginServerConnection con, ByteBuffer buf) {
        writeC(buf, getOpcode());
        writeC(buf, type);
        writeD(buf, accountId);
        writeS(buf, ip);
        writeD(buf, time);
        writeD(buf, adminObjId);
    }
}
