package gameserver.network.aion.serverpackets;

import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;

public class SM_DREDGION_INSTANCE extends AionServerPacket {

    private byte dredgiontype;

    private int players;

    private int instanceid;

    private int allowed;

    private int timer = 0;

    private boolean close = false;

    public SM_DREDGION_INSTANCE(byte dredgiontype, int players, int allowed, int timer) {
        this.dredgiontype = dredgiontype;
        this.players = players;
        this.allowed = allowed;
        this.timer = timer;
    }

    public SM_DREDGION_INSTANCE(int instanceid) {
        this.players = 5;
        this.allowed = 0;
        this.timer = 0;
        this.close = true;
        if (instanceid == 300110000) this.dredgiontype = 1; else this.dredgiontype = 2;
    }

    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        if (dredgiontype == 1) instanceid = 300110000; else instanceid = 300210000;
        writeD(buf, dredgiontype);
        writeC(buf, players);
        writeD(buf, instanceid);
        if (timer < 1 && dredgiontype == 1 && !close) {
            writeD(buf, 401193);
            writeD(buf, 401197);
        } else if (timer < 1 && dredgiontype == 2 && !close) {
            writeD(buf, 401675);
            writeD(buf, 401677);
        } else {
            writeD(buf, 0);
            writeD(buf, 0);
        }
        writeD(buf, allowed);
        writeH(buf, timer);
        writeC(buf, 0);
    }
}
