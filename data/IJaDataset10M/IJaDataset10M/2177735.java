package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.gametime.L1GameTimeClock;

public class S_GameTime extends ServerBasePacket {

    public S_GameTime(int time) {
        buildPacket(time);
    }

    public S_GameTime() {
        int time = L1GameTimeClock.getInstance().getGameTime().getSeconds();
        buildPacket(time);
    }

    private void buildPacket(int time) {
        writeC(Opcodes.S_OPCODE_GAMETIME);
        writeD(time);
    }

    @Override
    public byte[] getContent() {
        return getBytes();
    }
}
