package aionjp.network.aion.serverpackets;

import aionjp.network.aion.AionServerPacket;
import aionjp.network.aion.SessionKey;

/**
 * @author -Nemesiss-
 */
public class PlayOk extends AionServerPacket {

    private final int playOk1;

    private final int playOk2;

    public PlayOk(SessionKey key) {
        this.playOk1 = key.playOkID1;
        this.playOk2 = key.playOkID2;
    }

    @Override
    protected void writeImpl() {
        writeC(0x07);
        writeD(playOk1);
        writeD(playOk2);
    }

    @Override
    public String getType() {
        return "0x07 PlayOk";
    }
}
